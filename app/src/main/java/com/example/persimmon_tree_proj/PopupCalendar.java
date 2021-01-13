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
    private String day;
    private int set_position;
    private int set_month_lastday;
    private String plan; //일정

    //파이어베이스에 올리는 일정 관련
    private String f_code;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_calendar);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //취소 버튼
        ImageButton cancel = (ImageButton) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.i("check3",day);

                        FirebaseDatabase.getInstance().getReference("family").child(f_code).child("calendar").child(year).child(month).child(day).child(user_name).child("time").setValue(plan);
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

                if(position < set_position-1){
                    Toast.makeText(PopupCalendar.this, "해당 날짜는 이번달이 아닙니다!", Toast.LENGTH_SHORT).show();
                }
                //뒤에 회색 부분
                else if((set_month_lastday+set_position-2) < position){
                    Toast.makeText(PopupCalendar.this, "해당 날짜는 이번달이 아닙니다!", Toast.LENGTH_SHORT).show();
                }
                //이번달에 포함된 날짜
                else{

                    day = String.valueOf(Integer.valueOf(position)-set_position+2);
                    TextView txt_View = (TextView) findViewById(R.id.txt_view); //question 을 나타내는 textView
                    txt_View.setText(year+"년"+ month+"월"+day+"일");




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
        mCalendarAdapter = new CalendarAdapter2(this, R.layout.day, mDayList);
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