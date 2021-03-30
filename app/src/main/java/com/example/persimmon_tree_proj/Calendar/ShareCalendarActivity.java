package com.example.persimmon_tree_proj.Calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.Profile.MypageActivity;
import com.example.persimmon_tree_proj.Calendar.Popup_Cal.PopupcalActivity;
import com.example.persimmon_tree_proj.Calendar.adapter.CalendarAdapter;
import com.example.persimmon_tree_proj.customer_sound;
import com.example.persimmon_tree_proj.Calendar.domain.DayInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * 그리드뷰를 이용한 달력 예제
 *
 * @blog http://croute.me
 * @link http://croute.me/335
 *
 * @author croute
 * @since 2011.03.08
 */
public class ShareCalendarActivity extends Activity implements OnItemClickListener
{
    private GestureDetectorCompat detector;
    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;
    private ImageButton add_calendar;

    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;
    final static int DISTANCE = 200;
    final static int VELOCITY = 350;

    private String year;
    private String month;
    private int set_position;
    private String f_code;
    private int set_month_lastday;
    private int dayOfMonth;
    private ArrayList<String> have_plan_day =  new ArrayList<String>();
    private ArrayList<String> when_whos_what_plan_arr =  new ArrayList<String>();
    private HashMap<String,String> name_color_map = new HashMap<String,String>();

    //자기 프로필 표시를 위함
    String user_name = "";
    String user_gam = "";
    String user_color = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_calendar);

        //자기 프로필 가져오기
        FirebaseUser profileuser = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference referenced = FirebaseDatabase.getInstance().getReference("users");
        referenced.child(profileuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name = snapshot.child("userName").getValue().toString();
                FirebaseDatabase a_Database = FirebaseDatabase.getInstance();
                DatabaseReference a_Reference = a_Database.getReference("family");
                a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //본인의 감프로필과 컬러 오른쪽 상단 프로필 맵에 띄우기

                        //가져온 f_code에 해당하는 member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                        while (members.hasNext()) {
                            String member_num = members.next().getKey();
                            if (user_name.equals(member_num)) { //현재 로그인된 userid의 이름 == 우리가족 fcode > member > 이름 과 같다면
                                user_gam = snapshot.child("members").child(user_name).child("user_gam").getValue(String.class); //자신의 gam과 컬러를
                                user_color = snapshot.child("members").child(user_name).child("user_color").getValue(String.class);
                                ImageView profile = (ImageView) findViewById(R.id.btn_mypage2);

                                if (user_gam.equals("1")) {
                                    profile.setImageResource(R.drawable.gam1);
                                } else if (user_gam.equals("2")) {
                                    profile.setImageResource(R.drawable.gam2);
                                } else if (user_gam.equals("3")) {
                                    profile.setImageResource(R.drawable.gam3);
                                } else if (user_gam.equals("4")) {
                                    profile.setImageResource(R.drawable.gam4);
                                } else if (user_gam.equals("5")) {
                                    profile.setImageResource(R.drawable.gam5);
                                } else if (user_gam.equals("6")) {
                                    profile.setImageResource(R.drawable.gam6);
                                } else if (user_gam.equals("7")) {
                                    profile.setImageResource(R.drawable.gam7);
                                } else if (user_gam.equals("8")) {
                                    profile.setImageResource(R.drawable.gam8);
                                } else {
                                    profile.setImageResource(R.drawable.gam1);
                                }

                                profile.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd1 = (GradientDrawable) profile.getBackground(); //동적으로 테두리 색 바꿈
                                gd1.setStroke(50, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        detector = new GestureDetectorCompat(this, new MyGestureListener());

        mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);
        GestureDetector gestureDetector = null;

        mGvCalendar.setOnItemClickListener(this);



        mGvCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });

        //일정 추가 이미지 버튼
        add_calendar = (ImageButton)findViewById(R.id.btn_addcal);
        add_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCalendarActivity.this, PopupCalendar.class);
                intent.putExtra("f_code",f_code);
                startActivityForResult(intent, 1);
            }
        });

        //다음달, 이전달로 이동하는 버튼 !
        ImageButton last_month = (ImageButton) findViewById(R.id.last_month_btn);
        ImageButton next_month = (ImageButton) findViewById(R.id.next_month_btn);
        last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
            }
        });
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
            }
        });

        //왔다감 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                finish();
            }
        });

        //공유캘린더 버튼
        ImageButton go_calendar = (ImageButton) findViewById(R.id.calendar_btn);
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
                Intent intentt = getIntent();
                f_code = intentt.getStringExtra("f_code");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //고객의 소리함
        ImageButton go_setting = (ImageButton) findViewById(R.id.setting_btn);
        go_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCalendarActivity.this, customer_sound.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                finish();
            }
        });

        //마이페이지
        ImageButton go_mypage = (ImageButton) findViewById(R.id.btn_mypage);
        go_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
            }
        });

        //왔다감에서 intent로 보낸 가족코드 받아옴
        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code");
        //일정 가져와서 띄우는 부분~~~~~//
        //1. 가족들 이름:색깔 map 형성 ex) 민정: #232323 //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("family");
        //현재 구성원들 데이터베이스 하나씩 돌면서 user_name:color_number
        reference.child(f_code).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String user_name = data.getKey();
                    String color_number = dataSnapshot.child(user_name).child("user_color").getValue(String.class);
                    if (color_number != null) { //있으면 담기, 없으면 패스
                        name_color_map.put(user_name,color_number); //민정:#121212 이런식으로 들어감, 파이썬의 dictionaryr같은 거

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });    //이름:색깔 map 부분 끝


        //3. 멤버별 해당 색깔의 동적 imageview형성//

        //동적 imageview형성 끝//

        //~~~일정 가져와서 띄우는 부분 끝//



        mDayList = new ArrayList<DayInfo>();


        //swipe 시작
        GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener(){

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getX()-e2.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
                }
                if(e2.getX()-e1.getX() < DISTANCE && Math.abs(velocityX) > VELOCITY){
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
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);

        if(dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;
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

        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(Integer.valueOf(calendar.get(Calendar.MONTH)+1));
        //왔다감에서 intent로 보낸 가족코드 받아옴
        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");


        // 2. 파이어베이스 돌면서 일정이 있는 날짜 배열에 담기 //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("family");
        reference.child(f_code).child("calendar").child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //datasnapshot은 month
                int count = (int) dataSnapshot.getChildrenCount();
                Iterator<DataSnapshot> day = dataSnapshot.getChildren().iterator(); //날짜 하나씩델고옴
                have_plan_day.clear();
                while (day.hasNext()){
                    int i = 0;
                    have_plan_day.add(i,day.next().getKey());  //계획이 있는 날짜 담음
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        when_whos_what_plan_arr.clear();
        // 3. 파이어베이스 돌면서 멤버별 사람이름:일정이름 map 형성해 해당 날짜에 띄우기 //
        reference.child(f_code).child("calendar").child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //datasnapshot은 month
                for(int i=0; i<have_plan_day.size();i++){
                    String day_num = String.valueOf(have_plan_day.get(i));
                    Iterator<DataSnapshot> plan = dataSnapshot.child(day_num).getChildren().iterator();
                    while (plan.hasNext()){
                        String whos_plan = plan.next().getKey();
                        Iterator<DataSnapshot> one_plan = dataSnapshot.child(day_num).child(whos_plan).getChildren().iterator();
                        while (one_plan.hasNext()) {
                            String plan_name = one_plan.next().getValue().toString();
                            when_whos_what_plan_arr.add(have_plan_day.get(i));  //when = 날짜
                            when_whos_what_plan_arr.add(whos_plan);   //who's = 누구의
                            when_whos_what_plan_arr.add(plan_name);   //what_plan = 어떤 일정이냐!
                        }
//                        //arraylist에 [2,민정,연날리기] 이렇게 들어감
//                        make_bar(when_whos_what_plan_arr);   //날짜 view에 집어 넣는 함수로 이동
                    }
                }
                //파이어베이스 돌면서 멤버별 시간: 일정 map 형성 끝//
                initCalendarAdapter(when_whos_what_plan_arr,name_color_map,dayOfMonth);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

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

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
    { //앞에 회색 부분
        if(position < set_position-1){
            Toast.makeText(ShareCalendarActivity.this, "해당 날짜는 이번달이 아닙니다!", Toast.LENGTH_SHORT).show();
        }
        //뒤에 회색 부분
        else if((set_month_lastday+set_position-2) < position){
            Toast.makeText(ShareCalendarActivity.this, "해당 날짜는 이번달이 아닙니다!", Toast.LENGTH_SHORT).show();
        }
        //이번달에 포함된 날자
        else{
            Intent intent = new Intent(this, PopupcalActivity.class);
            intent.putExtra("day", String.valueOf(Integer.valueOf(position)-set_position+2));
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("f_code",f_code);
            startActivityForResult(intent, 1);
        }


    }

    private void initCalendarAdapter(ArrayList when_whos_what_plan_arr,HashMap name_color_map,int dayOfMonth)
    {
        mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList,when_whos_what_plan_arr,name_color_map,dayOfMonth);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 120;

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

    public void onBackPressed() {
        finish();

        //안드로이드 백버튼 막기
        return;
    }

}