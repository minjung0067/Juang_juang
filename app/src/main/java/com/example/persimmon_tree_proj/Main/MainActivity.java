package com.example.persimmon_tree_proj.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private String f_code;
    static int count;
    static int member_count;
    private String family_name;
    private DatabaseReference a_Reference;
    private FirebaseDatabase a_Database;
    private Random rnd;
    private String[] gam_interaction = {"오늘도 화이팅이라감","감 잡았쓰 ~","당신은 다정다감","감감 무슨감 쟁반같이 둥근감"};
    private Handler mHandler = new Handler(); //1초후 작동 같은 지연 함수
    private Runnable mMyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //감과 상호작용할 수 있는 메인 페이지

        /*
        part 1
            - 로딩페이지에서 가져온 intent 값들을 저장
                => f_code, user_gam, user_color, user_name, family_name, introduce

        part 2 - 가족이름, 사용자 이름, 감 프로필 띄우기


        part 3 - 감 인터렉션
            - 문구 랜덤하게 가져옴
            - 1초후 문구 사라짐

        part 4 - 각 카테고리에 해당하는 버튼들로 이동하는 코드



        * */



        //part 1 - f_code, 사용자 이름, count 로딩에서 받아오기

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("user_fcode");
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");




        //part 2 - 가족이름, 사용자 이름, 감 프로필 띄우기

        TextView my_family_name = (TextView) findViewById(R.id.my_family_name);
        my_family_name.setText(family_name);
        TextView my_introduce = (TextView) findViewById(R.id.my_introduce);
        my_introduce.setText(introduce);
        ImageView profile_image = (ImageView) findViewById(R.id.profile_image);

        //감 + 이름 띄우기
        switch (user_gam) {
            case "1":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd1 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam1);
                break;
            case "2":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd2 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam2);
                break;
            case "3":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd3 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam3);
                break;
            case "4":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd4 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam4);
                break;
            case "5":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd5 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam5);
                break;
            case "6":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd6 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam6);
                break;
            case "7":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd7 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam7);
                break;
            case "8":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd8 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                profile_image.setImageResource(R.drawable.gam8);
                break;
            default:
                profile_image.setBackgroundColor(Color.parseColor("#ffffff"));
                profile_image.setImageResource(R.drawable.gam1);
                break;
        }



        //part 3 - 감 인터렉션
        ImageButton btn_gam = (ImageButton) findViewById(R.id.gam_btn);
        Button click_gam_msg = (Button) findViewById(R.id.btn_clickgam);
        Button gam_say = (Button) findViewById(R.id.gam_say);
        rnd = new Random(); //랜덤클래스로부터 랜덤 값 받아오는 변수 작성.
        btn_gam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = rnd.nextInt(4); //랜덤 숫자 생성
                gam_say.setText(""); //초기화
                gam_say.setVisibility(View.INVISIBLE);
                gam_say.setText(gam_interaction[num]); //위에서 담아놓은 문구 중 랜덤하게 가져옴
                gam_say.setVisibility(View.VISIBLE); //랜덤 문구 보여지게

                mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
                mMyTask = new Runnable() {
                    @Override
                    public void run() {
                        gam_say.setVisibility(View.INVISIBLE);
                    }
                };

            }
        });



        //part 4 - 각 카테고리에 해당하는 버튼들로 이동하는 코드

        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                intent.putExtra("introduce",introduce);
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
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });



    }
}