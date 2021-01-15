package com.example.persimmon_tree_proj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.adapter.Plan_listview_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PopupcalActivity extends Activity {

    TextView day_text;
    TextView yearmonth_text;
    LinearLayout plan_view;
    Context context;



    private String f_code;
    private String day;
    private String month;
    private String year;
    private HashMap<String,String> name_color_map = new HashMap<String,String>();
    private HashMap<String,String> name_introduce_map = new HashMap<String,String>();
    private ListView listview;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popupcal);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //UI 객체생성
        yearmonth_text = (TextView)findViewById(R.id.yearmonth_text);
        day_text = (TextView)findViewById(R.id.day_text);

        //데이터 가져오기
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        f_code = intent.getStringExtra("f_code");

        //년 월 보여주기
        yearmonth_text.setText( year + "년 " + month + "월 ");
        //날짜 보여주기
        day_text.setText( day +"일");

        //1. 가족들 이름:색깔 map 형성 ex) 민정: #232323 //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("family");
        //현재 구성원들 데이터베이스 하나씩 돌면서 user_name:color_number
        reference.child(f_code).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String user_name = data.getKey();
                    String color_number = dataSnapshot.child(user_name).child("user_color").getValue(String.class);
                    String introduce = dataSnapshot.child(user_name).child("introduce").getValue(String.class);
                    if (color_number != null) { //있으면 담기, 없으면 패스
                        name_color_map.put(user_name,color_number); //민정:#121212 이런식으로 들어감, 파이썬의 dictionaryr같은 거
                        name_introduce_map.put(user_name,introduce);

                    }
                    else if (color_number.equals("")){
                        name_color_map.put(user_name,color_number); //민정:#121212 이런식으로 들어감, 파이썬의 dictionaryr같은 거
                        name_introduce_map.put(user_name,"");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });    //이름:색깔 map 부분 끝
        listview = (ListView) findViewById(R.id.plan_vview);
        //intent 받아온 년월일 사용해서 해당 날짜의 일정 한 줄씩 띄우기
        reference.child(f_code).child("calendar").child(year).child(month).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Plan_listview_Adapter adapterr;
                adapterr = new Plan_listview_Adapter();
                for (DataSnapshot data : dataSnapshot.getChildren()) { //data는 사람 이름 각각
                    String user_name = data.getKey();
                    for (DataSnapshot one_plan : dataSnapshot.child(user_name).getChildren()) {
                        String plan_id = one_plan.getKey();
                        String plan_name = one_plan.getValue().toString();
                        listview.setAdapter(adapterr);
//                        GradientDrawable gd = (GradientDrawable) member_color.getBackground(); //앞에 뜨는 동그라미 부분 색깔 바꾸기
//                        gd.setColor(Color.parseColor()); //해당 일정의 주인 색깔로 색깔 설정
                        // 아이템 추가.
                        adapterr.addItem( name_color_map.get(user_name), name_introduce_map.get(user_name), plan_name,user_name,plan_id);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });    //이름:색깔 map 부분

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
//        Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("f_code",f_code);
//        startActivity(intent);
        //일정은 그냥 창만 닫으면 되는 거라 주석처리 했어용
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
}