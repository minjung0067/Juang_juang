package com.example.persimmon_tree_proj.To_do_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.customer_sound;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Todolist_Activity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    //자기 프로필 표시를 위함
    String user_name = "";
    String user_gam = "";
    String user_color = "";
    RecyclerView recyclerView;
    InputMethodManager inputmethodmanager;


    // 배열리스트
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> contents = new ArrayList<String>();
    private ArrayList<String> writer = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> style = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        Log.i("checking f_code", f_code);
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");


        //본문의 메모장 띄우는 부분
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //가져온 f_code에 해당하는 member 수 세기

                //각 배열 초기화
                title.clear();
                contents.clear();
                writer.clear();
                date.clear();
                style.clear();

                //차례로 돌면서 메모장에 대한 여러 정보들을 각 배열에 순서대로 담기
                Iterator<DataSnapshot> memos = dataSnapshot.child(f_code).getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                while (memos.hasNext()) { //boolean hasNext() 메소드는 읽어 올 요소가 남아있는지 확인하는 메소드. 있으면 true, 없으면 false를 반환
                    String this_memo = memos.next().getKey();
                    String this_title = dataSnapshot.child(f_code).child(this_memo).child("title").getValue(String.class);
                    String this_contents = dataSnapshot.child(f_code).child(this_memo).child("contents").getValue(String.class);
                    String this_style = dataSnapshot.child(f_code).child(this_memo).child("style").getValue(String.class);
                    String this_date = dataSnapshot.child(f_code).child(this_memo).child("date").getValue(String.class);
                    String this_uid = dataSnapshot.child(f_code).child(this_memo).child("writer").getValue(String.class);

                    title.add(this_title);
                    contents.add(this_contents);
                    style.add(this_style);
                    date.add(this_date);
                    writer.add(this_uid);
                }

                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

                // 그리드 세로 줄 세팅
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                // 어댑터와 연결
                RecyclerView.Adapter adapter = new list_adapter(Todolist_Activity.this,title,contents,date,writer,style);

                // 어댑터를 리사이클뷰랑 연결
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        //전체 질문 가져오기 끝




        //버튼
        //새 메모 추가 버튼
        Button new_list = (Button) findViewById(R.id.list_add);
        new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Todolist_Activity.this, TodoList_addlist_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                finish();
            }
        });

        //수정 버튼
        Button edit_list = (Button) findViewById(R.id.list_edit);
        edit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Todolist_Activity.this, Todolist_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
            }
        });








        //뒤로가기
        ImageButton goback = (ImageButton)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intentt = new Intent(getApplicationContext(), LodingPage_Activity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code",f_code);
                startActivity(intentt);
            }
        });


        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
            }
        });


        //왔다감 버튼
        ImageButton go_qna = (ImageButton) findViewById(R.id.qna_btn);
        go_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //공유캘린더 버튼
        ImageButton go_calendar = (ImageButton) findViewById(R.id.calendar_btn);
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(), ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //메인 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //뭐할감
        ImageButton go_todo = (ImageButton) findViewById(R.id.to_do_btn);
        go_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Todolist_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //게임
        ImageButton go_game = (ImageButton) findViewById(R.id.game_btn);
        go_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        Intent intent = new Intent(Todolist_Activity.this, QNA_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }



}