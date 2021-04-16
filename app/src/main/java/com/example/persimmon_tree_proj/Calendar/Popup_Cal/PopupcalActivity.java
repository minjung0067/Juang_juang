package com.example.persimmon_tree_proj.Calendar.Popup_Cal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.adapter.Plan_listview_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.Juang_juang.R.drawable.btn_brightgray_rounded;
import static com.example.Juang_juang.R.drawable.line_dungle;

public class PopupcalActivity extends Activity {

    TextView day_text;
    TextView yearmonth_text;

    private String f_code;
    private String day;
    private String month;
    private String year;
    private HashMap<String,String> name_color_map = new HashMap<String,String>();
    private HashMap<String,String> name_introduce_map = new HashMap<String,String>();
    private HashMap<String,String> name_gam_map = new HashMap<String, String>();
    private SwipeMenuListView listview;
    List<Object> Array = new ArrayList<Object>();
    private ArrayList<String> user_list;

    private Plan_listview_Adapter adapterr = new Plan_listview_Adapter();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private int i; //user_list index
    private int count; //user_list size





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popupcal);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //UI 객체생성
        yearmonth_text = (TextView) findViewById(R.id.yearmonth_text);
        day_text = (TextView) findViewById(R.id.day_text);

        //데이터 가져오기
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        f_code = intent.getStringExtra("f_code");

        //년 월 보여주기
        yearmonth_text.setText(year + "년 " + month + "월 ");
        //날짜 보여주기
        day_text.setText(day + "일");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = String.valueOf(snapshot.child("fcode").getValue());

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("groups");
                reference2.child(f_code).child("members").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_list = new ArrayList<>();
                        user_list.clear();

                        for (DataSnapshot membersData : dataSnapshot.getChildren()) {
                            String user = membersData.getKey();
                            user_list.add(user);
                            count++;
                        }

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //전체 user 가져오기


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //1. 가족들 이름:색깔 map 형성 ex) 민정: #232323 //
        //현재 구성원들 데이터베이스 하나씩 돌면서 user_name:color_number
        for(i = 0 ; i< count ; i++){
            reference.child(user_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String color_number = snapshot.child("user_color").getValue(String.class);
                    String user_gam = snapshot.child("user_gam").getValue(String.class);
                    String user_name = snapshot.child("user_name").getValue(String.class);
                    String introduce = snapshot.child("introduce").getValue(String.class);
                    if (color_number != null) { //있으면 담기, 없으면 패스
                        name_color_map.put(user_name, color_number); //민정:#121212 이런식으로 들어감, 파이썬의 dictionaryr같은 거
                        name_introduce_map.put(user_name, introduce);
                        name_gam_map.put(user_name,user_gam);

                    } else if (color_number.equals("")) {
                        name_color_map.put(user_name, color_number); //민정:#121212 이런식으로 들어감, 파이썬의 dictionaryr같은 거
                        name_introduce_map.put(user_name, "");
                        name_gam_map.put(user_name,user_gam);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        listview = (SwipeMenuListView) findViewById(R.id.plan_vview);
        DatabaseReference referencee = FirebaseDatabase.getInstance().getReference("calendar");
        referencee.child(f_code).child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //일정 아무것도 없으면
                if (dataSnapshot.child(day).exists() == false) {
                    adapterr.addItem("", "", "현재 등록된 일정이 없감..", "", "" ,"");
                    listview.setAdapter(adapterr); //리스트뷰에 adapterr 넣기
                }
                //일정이 있는 경우
                else{
                    for (DataSnapshot data : dataSnapshot.getChildren()) { //data는 사람 이름 각각
                        String user_name = data.getKey();
                        for (DataSnapshot one_plan : dataSnapshot.child(user_name).getChildren()) {
                            String plan_id = one_plan.getKey();
                            String plan_name = one_plan.child("plan_name").getValue().toString();
                            adapterr.addItem("")
                            listview.setAdapter(adapterr);
//                        GradientDrawable gd = (GradientDrawable) member_color.getBackground(); //앞에 뜨는 동그라미 부분 색깔 바꾸기
//                        gd.setColor(Color.parseColor()); //해당 일정의 주인 색깔로 색깔 설정
                            SwipeMenuCreator creator = new SwipeMenuCreator() {

                                @Override
                                public void create(SwipeMenu menu) {
                                    // Create different menus depending on the view type
                                    createMenu1(menu);
                                }
                                private void createMenu1(SwipeMenu menu) {
                                    SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                                    item1.setBackground(new ColorDrawable(Color.rgb(255,255,255)));
                                    item1.setWidth((190));
                                    item1.setIcon(R.drawable.calendar_revise2x);
                                    menu.addMenuItem(item1);
                                    SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                                    item2.setBackground(new ColorDrawable(Color.rgb(255,255,255)));
                                    item2.setWidth(190);
                                    item2.setIcon(R.drawable.calendar_delete2x);
                                    menu.addMenuItem(item2);
                                }
                            };
                            // set creator
                            listview.setMenuCreator(creator);
                            adapterr.addItem(name_color_map.get(user_name), name_gam_map.get(user_name),name_introduce_map.get(user_name), plan_name, user_name, plan_id);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        // step 2. listener item click event
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        popup_revise(position);
                        break;
                    case 1:
                        popup_delete(position);
                        break;
                }
                return false;
            }
        });
    }
        //swipe해서 수정/삭제

    public void popup_revise(int position){
        Intent intent = new Intent(this, popup_RevisePlan.class);
        ArrayList arr = adapterr.getItem((position));
        intent.putExtra("arr", arr);
        intent.putExtra("day", day);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("f_code",f_code);
        startActivityForResult(intent, 1);

    }

    public void popup_delete(int position){
        Intent intent = new Intent(this, popup_DeletePlan.class);
        ArrayList<String> arr = adapterr.getItem((position));
        intent.putExtra("arr", arr);
        intent.putExtra("day", day);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("f_code",f_code);
        startActivityForResult(intent, 1);

    }


    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        finish();

    }

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

    //px->dp로 바꾸는 함수가 이건가봐!!!
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
