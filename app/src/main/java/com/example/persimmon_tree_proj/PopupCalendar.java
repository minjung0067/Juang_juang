package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.adapter.CalendarAdapter2;
import com.example.persimmon_tree_proj.domain.DayInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class PopupCalendar extends Activity  {

    private GestureDetectorCompat detector;
    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;           //그리드뷰

    private ArrayList<DayInfo> mDayList; //일 저장할 리스트
    private CalendarAdapter2 mCalendarAdapter; //그리드뷰 어댑터

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;
    final static int DISTANCE = 200;
    final static int VELOCITY = 350;

    private String year;
    private String month;
    private String day1;
    private String day2;
    private int set_position;
    private int set_month_lastday;
    private String plan; //일정

    //파이어베이스에 올리는 일정 관련
    private String f_code;
    private String user_name;
    private Integer click;
    private Integer point_1_index;
    private Integer point_2_index;
    private Integer firstmonth = 0;//일정 추가시 처음 선택한 일의 월
    private Integer firstyear = 0;//일정 추가시 나중에 선택한 일의 월
    private String firstposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_calendar);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code");

        final TextView text_start = (TextView) findViewById(R.id.text_start); //question 을 나타내는 textView
        final TextView text_end = (TextView) findViewById(R.id.text_end); //question 을 나타내는 textView


        //onclick
        click = 0;
        point_1_index = 0;
        point_2_index = 0;
        day1 = String.valueOf(0);
        day2 = String.valueOf(0);


        //취소 버튼
        ImageButton cancel = (ImageButton) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                startActivity(intent);
                finish();
            }
        });

        //일정 추가 확인 버튼
        Button add = (Button)findViewById(R.id.btn_addok);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txt_plan = (EditText) findViewById(R.id.txt_plan);
                plan = txt_plan.getText().toString();
                Log.i("plan",plan);

                //일정 파이어베이스에 올리기
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        f_code = snapshot.child("fcode").getValue().toString();
                        user_name = snapshot.child("userName").getValue().toString();
                        Log.i("check",year);
                        Log.i("check2",month);
                        int i = 0;

                        if(Integer.parseInt(day2)> 0){
                            for(i = Integer.parseInt(day1); i <= Integer.parseInt(day2) ; i++){
                                FirebaseDatabase.getInstance().getReference("family").child(f_code).child("calendar").child(year).child(month).child(String.valueOf(i)).child(user_name).push().setValue(plan);

                            }

                        }
                        else{
                            FirebaseDatabase.getInstance().getReference("family").child(f_code).child("calendar").child(year).child(month).child(String.valueOf(day1)).child(user_name).push().setValue(plan);

                        }



                        Toast.makeText(PopupCalendar.this, "일정이 추가되었다감", Toast.LENGTH_SHORT).show();



                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        detector = new GestureDetectorCompat(this, new MyGestureListener());

        mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar); //달력
        GestureDetector gestureDetector = null;


        mGvCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });



        mDayList = new ArrayList<DayInfo>(); //일수를 저장하는 리스트


        mGvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (click){
                    // 1번 클릭
                    case 0:
                        //첫번째 클릭 전 달력 초기화
                        Log.i("check0","첫번쨰 클릭");
                        text_start.setText("날짜를 선택해주세요.");
                        text_end.setVisibility(View.INVISIBLE);
                        point_1_index = 0;
                        point_2_index = 0;
                        //for(int i = 0; i < mDayList.size(); i++){
                        //   mGvCalendar.getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
                        //}

                        if(position < set_position-1){
                            Toast.makeText(PopupCalendar.this, "일정을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        //뒤에 회색 부분
                        else if((set_month_lastday+set_position-2) < position){
                            Toast.makeText(PopupCalendar.this, "일정을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        //이번달에 포함된 날짜
                        else{
                            /*
                            if((firstmonth == String.valueOf(0)) && (firstyear == String.valueOf(0))){ //처음 선택하는 경우
                                click++; //이번달에 포함된 날짜를 클릭했다면, case1로 이동
                                point_1_index = position;
                                // text_start 및 gridView1 배경변경
                                day1 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                text_start.setText(" 시작일 : "+year+"년"+ month+"월"+day1+"일");
                                firstmonth = month;
                                firstyear = year;
                                mGvCalendar.getChildAt(position).setBackgroundColor(Color.parseColor("#52912E"));
                                break;
                            }

                             */
                            click++; //이번달에 포함된 날짜를 클릭했다면, case1로 이동
                            point_1_index = position;
                            // text_start 및 gridView1 배경변경
                            day1 = String.valueOf(Integer.valueOf(position)-set_position+2);
                            text_start.setText(" 시작일 : "+year+"년"+ month+"월"+day1+"일");
                            firstmonth = Integer.valueOf(month);
                            firstyear = Integer.valueOf(year);
                            //mGvCalendar.getChildAt(position).setBackgroundColor(Color.parseColor("#52912E"));
                            break;

                        }

                        // 2번 클릭
                    case 1:
                        if(position < set_position-1){
                            Toast.makeText(PopupCalendar.this, "일정을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        //뒤에 회색 부분
                        else if((set_month_lastday+set_position-2) < position){
                            Toast.makeText(PopupCalendar.this, "일정을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //이번달에 포함된 날짜
                            Log.i("check2",year);
                            Log.i("check2-1", String.valueOf(firstyear));
                            Log.i("check3",month);
                            Log.i("check3-1", String.valueOf(firstmonth));
                            Log.i("checkcheck", String.valueOf((year.equals(String.valueOf(firstyear)))));
                            Log.i("checkcheck", String.valueOf((Integer.valueOf(month) == firstmonth)));

                            if((year.equals(String.valueOf(firstyear)))){
                                if((Integer.valueOf(month) == firstmonth)){ //같은 년 같은 달일 경우
                                    Log.i("check","check1");
                                    if(point_1_index > position){
                                        Log.i("check 어디","요기1");
                                        //mGvCalendar.getChildAt(point_1_index).setBackgroundColor(Color.parseColor("#00000000"));
                                        //mGvCalendar.getChildAt(position).setBackgroundColor(Color.parseColor("#52912E"));
                                        point_1_index = position;
                                        day1 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                        text_start.setText(" 시작일 : "+year+"년"+ month+"월"+day1+"일");
                                        click = 1;
                                        break;

                                    }
                                    else{ //이번달에 포함된 날짜와 더 큰 숫자를 선택했다면,
                                        //click++;
                                        Log.i("check 어디","요기2");
                                        point_2_index = position;
                                        day2 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                        text_end.setVisibility(View.VISIBLE);
                                        text_end.setText(" 종료일 : "+year+"년"+ month+"월"+day2+"일");
                                        //for(int i = point_1_index+1; i <= point_2_index-1; i++){
                                        //    mGvCalendar.getChildAt(i).setBackgroundColor(Color.parseColor("#92C44B"));
                                        //}
                                        //mGvCalendar.getChildAt(point_2_index).setBackgroundColor(Color.parseColor("#52912E"));
                                        //종료일을 나타내고, click = 0을 만들어 다음번에 click을 하게 되다면 새로 시작할 수 있도록 한다.
                                        click=0;
                                        firstyear = 0;
                                        firstmonth = 0;
                                        break;

                                    }

                                }
                                //두번쨰 선택이지만, 다른 달이거나 다른 년도 일 경우
                                else if( (Integer.valueOf(firstmonth) < Integer.valueOf(month))){ //같은 년인데 더큰 달일 경우
                                    Log.i("check 어디","요기3");
                                    Log.i("check","check2");
                                    day2 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                    text_end.setVisibility(View.VISIBLE);
                                    text_end.setText(" 종료일 : "+year+"년"+ month+"월"+day2+"일");
                                    //for(int i = set_position ; i <= point_2_index-1; i++){
                                    //    mGvCalendar.getChildAt(i).setBackgroundColor(Color.parseColor("#92C44B"));
                                    //}
                                    //mGvCalendar.getChildAt(point_2_index).setBackgroundColor(Color.parseColor("#52912E"));
                                    //종료일을 나타내고, click = 0을 만들어 다음번에 click을 하게 되다면 새로 시작할 수 있도록 한다.
                                    click=0;
                                    firstmonth =0;
                                    firstyear = 0;
                                    break;

                                }
                                else if((Integer.valueOf(firstmonth) > Integer.valueOf(month))){ //같은 년인데 더 작은 달일 경우
                                    Log.i("check 어디","요기4");
                                    //mGvCalendar.getChildAt(point_1_index).setBackgroundColor(Color.parseColor("#00000000"));
                                    //mGvCalendar.getChildAt(position).setBackgroundColor(Color.parseColor("#52912E"));
                                    point_1_index = position;
                                    day1 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                    text_start.setText(" 시작일 : "+year+"년"+ month+"월"+day1+"일");
                                    click = 1;
                                    break;

                                }

                            }
                            else if((Integer.valueOf(firstyear) < Integer.valueOf(year)) ){
                                Log.i("check 어디","요기5");
                                day2 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                text_end.setVisibility(View.VISIBLE);
                                text_end.setText(" 종료일 : "+year+"년"+ month+"월"+day2+"일");
                                //for(int i = set_position ; i <= point_2_index-1; i++){
                                //    mGvCalendar.getChildAt(i).setBackgroundColor(Color.parseColor("#92C44B"));
                                //}
                                //mGvCalendar.getChildAt(point_2_index).setBackgroundColor(Color.parseColor("#52912E"));
                                //종료일을 나타내고, click = 0을 만들어 다음번에 click을 하게 되다면 새로 시작할 수 있도록 한다.
                                click=0;
                                firstmonth = 0;
                                firstyear = 0;
                                break;

                            }
                            else if((Integer.valueOf(firstyear) > Integer.valueOf(year)) ){
                                Log.i("check 어디","요기6");
                                //mGvCalendar.getChildAt(point_1_index).setBackgroundColor(Color.parseColor("#00000000"));
                                //mGvCalendar.getChildAt(position).setBackgroundColor(Color.parseColor("#52912E"));
                                point_1_index = position;
                                day1 = String.valueOf(Integer.valueOf(position)-set_position+2);
                                text_start.setText(" 시작일 : "+year+"년"+ month+"월"+day1+"일");
                                click = 1;
                                break;


                            }


                        }





                    default :
                        click = 0;
                        break;
                }


            }

        });




    }





    @Override
    protected void onResume()
    {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
        year = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
        month = String.valueOf(Integer.valueOf(mThisMonthCalendar.get(Calendar.MONTH)+1));
    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar)
    {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        if(dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth+"");
        set_position = dayOfMonth;
        set_month_lastday = thisMonthLastDay;
        for(int i=0; i<dayOfMonth-1; i++)
        {
            int date = lastMonthStartDay+i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            mDayList.add(day);
        }
        for(int i=1; i <= thisMonthLastDay; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }

        initCalendarAdapter();

    }

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(calendar.get(Calendar.YEAR) + "년 "
                + (calendar.get(Calendar.MONTH) + 1) + "월");
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(Integer.valueOf(calendar.get(Calendar.MONTH)+1));
        return calendar;
    }

    /**
     * 다음달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(calendar.get(Calendar.YEAR) + "년 "
                + (calendar.get(Calendar.MONTH)+1) + "월");
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(Integer.valueOf(calendar.get(Calendar.MONTH)+1));
        return calendar;
    }



    private void initCalendarAdapter()
    {
        mCalendarAdapter = new CalendarAdapter2(this, R.layout.dayforpopup, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();

                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
            }
            return true;
        }
    }


    //swipe 시작
    GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener(){

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX()-e2.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
                Log.i("ss","좌->우");
            }
            if(e2.getX()-e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
                Log.i("ss","우->좌");
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    private void onSwipeLeft() {
        mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
        getCalendar(mThisMonthCalendar);
    }

    private void onSwipeRight() {
        mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
        getCalendar(mThisMonthCalendar);

    }

    private void onSwipeTop() {

    }

    private void onSwipeBottom() {

    }



    //popup 관련
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}