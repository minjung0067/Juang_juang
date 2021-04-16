package com.example.persimmon_tree_proj.To_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Todolist_show_select_memo extends AppCompatActivity {

    private String style_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_show_select_memo);
        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");

        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String this_title = intent.getStringExtra("this_title");
        final String this_content = intent.getStringExtra("this_content");
        final String this_style = intent.getStringExtra("this_style");
        final String this_date = intent.getStringExtra("this_date");
        final String introduce = intent.getStringExtra("introduce");
        final String this_key = intent.getStringExtra("this_key");
        final String this_uid = intent.getStringExtra("this_uid");
        final String this_writer = intent.getStringExtra("this_writer");

        style_num = this_style;
        //선택한 메모 보여주기
        EditText title = (EditText) findViewById(R.id.title);
        EditText content = (EditText) findViewById(R.id.content);
        TextView date = (TextView) findViewById(R.id.date);
        TextView writer = (TextView) findViewById(R.id.writer);
        title.setText(this_title);
        content.setText(this_content);
        date.setText(this_date);

        //처음엔 수정 안되게, 수정 버튼 누르고 나서 수정되게
        title.setTag(title.getKeyListener());
        title.setKeyListener(null);
        content.setTag(content.getKeyListener());
        content.setKeyListener(null);

        LinearLayout memo = (LinearLayout) findViewById(R.id.memo);
        if (this_style.equals("1")) { memo.setBackgroundResource(R.drawable.todo_style1);}
        else if (this_style.equals("2")) { memo.setBackgroundResource(R.drawable.todo_style2);}
        else if (this_style.equals("3")) { memo.setBackgroundResource(R.drawable.todo_style3);}
        else{ memo.setBackgroundResource(R.drawable.todo_style4);}


        //뒤로가기
        TextView goback = (TextView)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Button color = (Button)findViewById(R.id.color);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                content.setHeight(100);
                RelativeLayout color_pan = (RelativeLayout)findViewById(R.id.color_pan);
                color_pan.setVisibility(View.VISIBLE);
            }

        });
        //업로드 그냥 체크 버튼 -> 다시 메모 리스트 쪽으로 돌아감
        Button ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
        //메모 색상 누르면 색상 바뀌게
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
        //수정하기
        Button revise = (Button)findViewById(R.id.revise);
        Button upload = (Button)findViewById(R.id.upload);
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "화면을 눌러 메모를 수정해보세요!", Toast.LENGTH_LONG).show();
                Button color = (Button)findViewById(R.id.color);
                ok.setVisibility(View.INVISIBLE);
                upload.setVisibility(View.VISIBLE);
                color.setVisibility(View.VISIBLE);
                title.setKeyListener((KeyListener) title.getTag());
                content.setKeyListener((KeyListener) content.getTag());
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
                formatH = new SimpleDateFormat("yyyy년 MM월 dd일"); //formatH에 현재 시간 넣어줌 대소문자 중요함
                Date today = new Date(); //today 변수에 Date 부르기
                String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326
                String title_text = title.getText().toString();
                String contents_text = content.getText().toString();


                //제목 입력 안 하고 업로드 버튼 눌렀을 때
                if(title_text.equals("")) { //제목 X
                    title.setHint("제목을 입력해주세요");
                    title.setHintTextColor(Color.parseColor("#FFAB47"));
                    title.setTextSize(20);
                }
                if(contents_text.equals("")){
                    content.setHint("메모를 작성해주세요");
                    content.setHintTextColor(Color.parseColor("#FFAB47"));
                }
                if(!contents_text.equals("")&&!title_text.equals("")) {


                    //업로드
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist").child(f_code);

                    reference.child(this_key).child("writer").setValue(introduce);
                    reference.child(this_key).child("uid").setValue(this_uid);
                    reference.child(this_key).child("title").setValue(title_text);
                    reference.child(this_key).child("contents").setValue(contents_text);
                    reference.child(this_key).child("date").setValue(strDate);
                    reference.child(this_key).child("style").setValue(style_num);
                    reference.child(this_key).child("writer").setValue(this_writer);

                    Toast.makeText(getApplicationContext(), "메모를 성공적으로 수정했어요!", Toast.LENGTH_LONG).show();

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

        //삭제하기
        Button close = (Button)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist").child(f_code);

                reference.child(this_key).removeValue();

                Toast.makeText(getApplicationContext(), "메모를 성공적으로 삭제했어요!", Toast.LENGTH_LONG).show();

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
        });


        //마이페이지 버튼
        TextView mypage = (TextView) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
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
        finish();
    }

    //배경 선택시 키보드 내려가도록
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputmethodmanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmethodmanager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        RelativeLayout color_pan = (RelativeLayout)findViewById(R.id.color_pan);
        color_pan.setVisibility(View.INVISIBLE);
        return true;
    }
}
