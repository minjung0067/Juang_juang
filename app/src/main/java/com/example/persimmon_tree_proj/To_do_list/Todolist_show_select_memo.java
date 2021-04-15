package com.example.persimmon_tree_proj.To_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;

public class Todolist_show_select_memo extends AppCompatActivity {

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
        final String this_uid = intent.getStringExtra("this_uid");

        //선택한 메모 보여주기
        TextView title = (TextView) findViewById(R.id.title);
        TextView content = (TextView) findViewById(R.id.content);
        TextView date = (TextView) findViewById(R.id.date);
        TextView writer = (TextView) findViewById(R.id.writer);
        title.setText(this_title);
        content.setText(this_content);
        date.setText(this_date);
        writer.setText(this_uid);

        LinearLayout memo = (LinearLayout) findViewById(R.id.memo);
        if (this_style.equals("1")) { memo.setBackgroundResource(R.drawable.todo_style1);}
        else if (this_style.equals("2")) { memo.setBackgroundResource(R.drawable.todo_style2);}
        else if (this_style.equals("3")) { memo.setBackgroundResource(R.drawable.todo_style3);}
        else{ memo.setBackgroundResource(R.drawable.todo_style4);}


//        //뒤로가기
//        ImageButton goback = (ImageButton)findViewById(R.id.go_back);
//        goback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                finish();
//            }
//        });


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
        finish();
    }


}
