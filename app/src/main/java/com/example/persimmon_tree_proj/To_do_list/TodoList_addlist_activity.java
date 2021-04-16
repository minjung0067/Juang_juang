package com.example.persimmon_tree_proj.To_do_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.To_do_list.Todolist_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TodoList_addlist_activity extends AppCompatActivity {

    private String style_num = "4";
    InputMethodManager inputmethodmanager;
    private EditText edit_contents;
    private EditText edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_addlist_activity);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        Log.i("checking f_code", f_code);
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");


        SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
        formatH = new SimpleDateFormat("yyyy년 MM월 dd일"); //formatH에 현재 시간 넣어줌 대소문자 중요함
        Date today = new Date(); //today 변수에 Date 부르기
        String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326

        //오늘 날짜와 작성자 setText
        TextView today_date = (TextView) findViewById(R.id.date);
        today_date.setText(strDate);
        TextView writer = (TextView) findViewById(R.id.writer);
        writer.setText(introduce);
        edit_title = (EditText) findViewById(R.id.title);
        edit_contents = (EditText) findViewById(R.id.contents);



        //메모 색상 누르면 색상 바뀌게
        LinearLayout memo = (LinearLayout) findViewById(R.id.memo);
        Button style1 = (Button) findViewById(R.id.style1);
        style1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                memo.setBackgroundResource(R.drawable.todo_style1);
                style_num = "1";
            }
        });
        Button style2 = (Button) findViewById(R.id.style2);
        style2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                memo.setBackgroundResource(R.drawable.todo_style2);
                style_num = "2";
            }
        });
        Button style3 = (Button) findViewById(R.id.style3);
        style3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                memo.setBackgroundResource(R.drawable.todo_style3);
                style_num = "3";
            }
        });
        Button style4 = (Button) findViewById(R.id.style4);
        style4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                memo.setBackgroundResource(R.drawable.todo_style4);
                style_num = "4";

            }
        });


        //새 메모 업로드
        Button upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
                formatH = new SimpleDateFormat("yyyy년 MM월 dd일"); //formatH에 현재 시간 넣어줌 대소문자 중요함
                Date today = new Date(); //today 변수에 Date 부르기
                String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326
                String title_text = edit_title.getText().toString();
                String contents_text = edit_contents.getText().toString();


                //제목 입력 안 하고 업로드 버튼 눌렀을 때
                if(title_text.equals("")) { //제목 X
                    edit_title.setHint("제목을 입력해주세요");
                    edit_title.setHintTextColor(Color.parseColor("#FFAB47"));
                    edit_title.setTextSize(20);
                }
                if(contents_text.equals("")){
                    edit_contents.setHint("메모를 작성해주세요");
                    edit_contents.setHintTextColor(Color.parseColor("#FFAB47"));
                }
                if(!contents_text.equals("")&&!title_text.equals("")) {


                    //업로드
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist");
                    Todo_new newtodo = new Todo_new(user.getUid(), introduce, title_text, contents_text, style_num, strDate);
                    Map<String, Object> about_memo = newtodo.toMap();
                    reference.child(f_code).push().setValue(about_memo);


                    //todo로 이동
                    Intent intent = new Intent(getApplicationContext(), Todolist_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("f_code", f_code);
                    intent.putExtra("introduce", introduce);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_color", user_color);
                    intent.putExtra("user_gam", user_gam);
                    startActivity(intent);
                    finish();
                }
            }
        });




        //뒤로가기
        TextView goback = (TextView)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intentt = new Intent(getApplicationContext(), Todolist_Activity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code",f_code);
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intentt);
            }
        });


        //마이페이지 버튼
        TextView mypage = (TextView) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(getApplicationContext(), MypageActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                startActivity(intentt);
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
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
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
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                intent.putExtra("introduce",introduce);
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
                Intent intent = new Intent(getApplicationContext(), com.example.persimmon_tree_proj.Game_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("introduce",introduce);
                intent.putExtra("user_color",user_color);
                intent.putExtra("user_gam",user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

    }


    //배경 선택시 키보드 내려가도록
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputmethodmanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmethodmanager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}