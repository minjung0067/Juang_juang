//package com.example.persimmon_tree_proj;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.DialogFragment;
//import androidx.recyclerview.widget.OrientationHelper;
//import androidx.appcompat.widget.Toolbar;
//
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//import java.util.Calendar;
//
//
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
//import com.applikeysolutions.cosmocalendar.view.CalendarView;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.Juang_juang.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class Calendar_activity extends AppCompatActivity {
//    private CalendarView calendarView;
//    private CheckBox multiple_check;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//
//        initViews();
//
//
//
//        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn); //왔다감 버튼
//        go_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //누르면 왔다감으로 이동
//                Intent intent = new Intent(Calendar_activity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        ImageButton go_calendar = (ImageButton) findViewById(R.id.calender_btn); //캘린더 버튼
//        go_calendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //누르면 캘린더 새로고침
//                Intent intent = new Intent(Calendar_activity.this, Calendar_activity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        multiple_check = (CheckBox) findViewById(R.id.rb_multiple);  //여러날짜 선택 누르게 된다
//        // 여러 날짜 선택 체크박스가 선택된다면 여러개, 체크박스 해제하면 자동으로 Range 선택
//        multiple_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {   //체크 되어있으면
//                    calendarView.setSelectionType(SelectionType.MULTIPLE);
//                } else {   //체크 안되어있으면
//                    calendarView.setSelectionType(SelectionType.RANGE);
//                }
//            }
//        });
//
//        //일정 추가 이미지 버튼
//        ImageButton add_calendar = (ImageButton)findViewById(R.id.btn_addcal);
//        add_calendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), "datePicker");
//            }
//        });
//
//    }
//
//    public void processDatePickerResult(int year, int month, int day){
//        String month_string = Integer.toString(month+1);
//        String day_string = Integer.toString(day);
//        String year_string = Integer.toString(year);
//        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
//
//        Toast.makeText(this,"Date: "+dateMessage,Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void initViews() {
//        calendarView = (CalendarView) findViewById(R.id.calendar_view);
//        calendarView.setCalendarOrientation(OrientationHelper.VERTICAL);
//        calendarView.setSelectedDayBackgroundColor(Color.parseColor("#FE8738"));
//        calendarView.setSelectedDayBackgroundStartColor(Color.parseColor("#92C44B"));
//        calendarView.setSelectedDayBackgroundEndColor(Color.parseColor("#92C44B"));
//        calendarView.setSelectionType(SelectionType.RANGE);
//    }
//
//    //툴바 나중에 지울 것
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//    //툴바 속 지우기 버튼이랑 선택한 거 보여주는 부분
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.clear_selections:
//                clearSelectionsMenuClick();
//                return true;
//
//            case R.id.show_selections:
//                List<java.util.Calendar> days = calendarView.getSelectedDates();
//                String result="";
//                for( int i=0; i<days.size(); i++)
//                {
//
//                    java.util.Calendar calendar = days.get(i);
//                    final int day = calendar.get(calendar.DAY_OF_MONTH);
//                    final int month = calendar.get(calendar.MONTH);
//                    final int year = calendar.get(calendar.YEAR);
//                    String week = new SimpleDateFormat("EE").format(calendar.getTime());
//                    String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";
//                    result += (day_full + "\n");
//                }
//                Toast.makeText(Calendar_activity.this, result, Toast.LENGTH_LONG).show();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void clearSelectionsMenuClick() {
//        calendarView.clearSelections();
//    }
//
//}
