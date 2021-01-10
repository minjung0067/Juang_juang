package com.example.persimmon_tree_proj;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;


import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import java.text.SimpleDateFormat;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Calendar_activity extends AppCompatActivity {
    private Context context;
    private int count = 0;
    private int count_gam = 0;
    private int count_color = 0;
    private int count_introduce = 0;
    private LinearLayout container;
    private ArrayList<String> gam_arr = new ArrayList<String>();
    private ArrayList<String> color_arr = new ArrayList<String>();
    private ArrayList<String> introduce_arr = new ArrayList<String>();
    private CalendarView calendarView;
    private CheckBox multiple_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initViews();
//
//        container = findViewById(R.id.con); //가족들 프로필 보여줄 layout
//        context = this;
//

//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
//                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");
//
//                reference1.child(myfcode).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //현재 가족으로 묶인 member 수 세기
//                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator();
//                        while (members.hasNext()) {
//                            String member_num = members.next().getKey();  //이 줄 있어야함, 없으면 에러
//                            count++;
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        throw databaseError.toException();
//                    }
//                });
//
//                //현재 구성원들 데이터베이스 하나씩 돌면서 color gam introduce 각각 배열에 저장 -> 따로 해야함
//                reference1.child(myfcode).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Iterator<DataSnapshot> members1 = dataSnapshot.getChildren().iterator();
//                        while (members1.hasNext()) { //전체 다 돌기
//                            String color_number = members1.next().child("user_color").getValue(String.class);
//                            if (color_number != null) { //있으면 담기, 없으면 패스
//                                color_arr.add(count_color++, color_number); //묶인 현재 가족구성원이 선택한 색깔 차례대로 COLOR_ARR 배열에 담기
////                                Log.i("lala color ", color_arr.get(count_color-1));
//                            }
//                        }
//                        Iterator<DataSnapshot> members2 = dataSnapshot.getChildren().iterator();
//                        while (members2.hasNext()) {
//                            String gam_number = members2.next().child("user_gam").getValue(String.class);
//                            if (gam_number != null) { //있으면 담기, 없으면 패스
//                                gam_arr.add(count_gam++, gam_number);  //묶인 현재 가족구성원이 선택한 감 프로필 사진 차례대로 GAM_ARR 배열에 담기
////                                Log.i("lala gam ", gam_arr.get(count_gam-1));
//                            }
//                        }
//                        Iterator<DataSnapshot> members3 = dataSnapshot.getChildren().iterator();
//                        while (members3.hasNext()) {
//                            String introduce = members3.next().child("introduce").getValue(String.class);
//                            if (introduce != null) {  //있으면 담기, 없으면 패스
//                                introduce_arr.add(count_introduce++, introduce); //묶인 현재 가족구성원이 선택한 소개 메세지 INTRODUCE_ARR 배열에 담기
////                                Log.i("lala introduce", introduce_arr.get(count_introduce-1));
//                            }
//
//                        }
//                        //저장해 준 것들 하나씩 꺼내서 캘린더 상단에 모든 가족 구성원 프로필 표시
//                        //현재 묶여있는 구성원 수만큼 동적으로 layout 생성하고 각각 TEXT랑 IMAGE 각자가 선택한 걸로 적용시킨다
//                        for (int i = 0; i < count; i++) {
//                            String gamgam = gam_arr.get(i); //감 프로필 사진 번호가 뭐냐에 따라 나눴삼
//                            if (gamgam.equals("1")) {    //1번 감 프로필 선택한 경우
//                                Sub n_layout1 = new Sub(getApplicationContext());  //동적 layout 생성
//                                ImageView iv = n_layout1.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout1.findViewById(R.id.user_name);  //각각 ID 찾아서
//                                iv.setImageResource(R.drawable.gam1);  //이미지 적용
//                                iv.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
//                                GradientDrawable gd1 = (GradientDrawable) iv.getBackground(); //동적으로 테두리 색 바꿈
//                                gd1.setStroke(23, Color.parseColor(color_arr.get(i))); //배열에 담긴 색깔로 테두리 설정
//                                user_name.setText(introduce_arr.get(i));   //소개 띄우는 부분
//                                container.addView(n_layout1); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("2")) {  //이하동일
//                                Sub n_layout2 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout2.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout2.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam2);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd2 = (GradientDrawable) iv.getBackground();
//                                gd2.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout2); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("3")) {
//                                Sub n_layout3 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout3.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout3.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam3);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd3 = (GradientDrawable) iv.getBackground();
//                                gd3.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout3); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("4")) {
//                                Sub n_layout4 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout4.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout4.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam4);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd4 = (GradientDrawable) iv.getBackground();
//                                gd4.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout4); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("5")) {
//                                Sub n_layout5 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout5.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout5.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam5);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd5 = (GradientDrawable) iv.getBackground();
//                                gd5.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout5); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("6")) {
//                                Sub n_layout6 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout6.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout6.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam6);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd6 = (GradientDrawable) iv.getBackground();
//                                gd6.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout6); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else if (gamgam.equals("7")) {
//                                Sub n_layout7 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout7.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout7.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam7);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd7 = (GradientDrawable) iv.getBackground();
//                                gd7.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout7); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            } else {
//                                Sub n_layout8 = new Sub(getApplicationContext());
//                                ImageView iv = n_layout8.findViewById(R.id.gam_image);
//                                TextView user_name = n_layout8.findViewById(R.id.user_name);
//                                iv.setImageResource(R.drawable.gam8);
//                                iv.setBackgroundResource(R.drawable.profile_outline);
//                                GradientDrawable gd8 = (GradientDrawable) iv.getBackground();
//                                gd8.setStroke(23, Color.parseColor(color_arr.get(i)));
//                                user_name.setText(introduce_arr.get(i));
//                                container.addView(n_layout8); // 기존 layout에 방금 동적으로 생성한 n_layout추가
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        throw databaseError.toException();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                throw databaseError.toException();
//            }
//        });


        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn); //왔다감 버튼
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 왔다감으로 이동
                Intent intent = new Intent(Calendar_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton go_calendar = (ImageButton) findViewById(R.id.calender_btn); //캘린더 버튼
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더 새로고침
                Intent intent = new Intent(Calendar_activity.this, Calendar_activity.class);
                startActivity(intent);
                finish();
            }
        });

        multiple_check = (CheckBox) findViewById(R.id.rb_multiple);  //여러날짜 선택 누르게 된다
        // 여러 날짜 선택 체크박스가 선택된다면 여러개, 체크박스 해제하면 자동으로 Range 선택
        multiple_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {   //체크 되어있으면
                    calendarView.setSelectionType(SelectionType.MULTIPLE);
                } else {   //체크 안되어있으면
                    calendarView.setSelectionType(SelectionType.RANGE);
                }
            }
        });

        ImageButton add_calendar = (ImageButton)findViewById(R.id.btn_addcal); //일정 추가 버튼
        add_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);

        Toast.makeText(this,"Date: "+dateMessage,Toast.LENGTH_SHORT).show();
    }


    private void initViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.VERTICAL);
        calendarView.setSelectedDayBackgroundColor(Color.parseColor("#FE8738"));
        calendarView.setSelectedDayBackgroundStartColor(Color.parseColor("#92C44B"));
        calendarView.setSelectedDayBackgroundEndColor(Color.parseColor("#92C44B"));
        calendarView.setSelectionType(SelectionType.RANGE);
    }

    //툴바 나중에 지울 것
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //툴바 속 지우기 버튼이랑 선택한 거 보여주는 부분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.clear_selections:
                clearSelectionsMenuClick();
                return true;

            case R.id.show_selections:
                List<java.util.Calendar> days = calendarView.getSelectedDates();
                String result="";
                for( int i=0; i<days.size(); i++)
                {

                    java.util.Calendar calendar = days.get(i);
                    final int day = calendar.get(calendar.DAY_OF_MONTH);
                    final int month = calendar.get(calendar.MONTH);
                    final int year = calendar.get(calendar.YEAR);
                    String week = new SimpleDateFormat("EE").format(calendar.getTime());
                    String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";
                    result += (day_full + "\n");
                }
                Toast.makeText(Calendar_activity.this, result, Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearSelectionsMenuClick() {
        calendarView.clearSelections();
    }

}
