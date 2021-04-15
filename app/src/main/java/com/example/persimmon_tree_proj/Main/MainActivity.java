package com.example.persimmon_tree_proj.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.To_do_list.Todolist_Activity;
import com.example.persimmon_tree_proj.customer_sound;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference a_Reference;
    private FirebaseDatabase a_Database;
    private Random rnd;
    private String[] gam_interaction = {"오늘도 행복한 하루 보내!",
            "오늘도 와줘서 고마워! 두근두근" ,
            "당신과 함께 할 수 있어 감사하당!" ,
            "앞으로 꽃길만 걷길 바랄게~" ,
            "감사합니다 감사합니다" ,
            "감동이야 감동~",
            "또 만났네! 너무 좋아~",
            "유후~ 감 잡았다!",
            "내일도 와줄 거지?",
            "아잇! 간지러워~",
            "나 불렀어? 만나서 반가워~",
            "나들이하기 좋은 날씨다~",
            "내가 그렇게 좋아?",
            "자꾸 누르면 나 터질지도 몰라!"
    };
    private Handler mHandler = new Handler(); //1초후 작동 같은 지연 함수
    private Runnable mMyTask;

    // Frame 단위로 이미지를 바꿔서 그려주는 Drawable 객체
    AnimationDrawable ani;
    private ActionBarDrawerToggle toggle;


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

        part 4 - 각 카테고리에 해당하는 버튼들로 이동하는 코드 */


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle(textTitle)
//                .setContentText(textContent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        //part 1 - f_code, 사용자 이름, count 로딩에서 받아오기

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("user_fcode");
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");
        final String count = intent.getStringExtra("count");


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
                gd1.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam1);
                break;
            case "2":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd2 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd2.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam2);
                break;
            case "3":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd3 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd3.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam3);
                break;
            case "4":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd4 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd4.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam4);
                break;
            case "5":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd5 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd5.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam5);
                break;
            case "6":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd6 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd6.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam6);
                break;
            case "7":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd7 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd7.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam7);
                profile_image.setTop(1);
                break;
            case "8":
                profile_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                GradientDrawable gd8 = (GradientDrawable) profile_image.getBackground(); //동적으로 테두리 색 바꿈
                gd8.setStroke(8, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정
                profile_image.setImageResource(R.drawable.gam8);
                break;
            default:
                profile_image.setBackgroundColor(Color.parseColor("#ffffff"));
                profile_image.setImageResource(R.drawable.gam1);
                break;
        }


        //part 3 - 감 인터렉션
        ImageView btn_gam = (ImageView) findViewById(R.id.gam_btn);

        ani = (AnimationDrawable) btn_gam.getDrawable();

        Button click_gam_msg = (Button) findViewById(R.id.btn_clickgam);
        Button gam_say = (Button) findViewById(R.id.gam_say);
        rnd = new Random(); //랜덤클래스로부터 랜덤 값 받아오는 변수 작성.
        btn_gam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //감이 움직이는 애니메이션
                if (ani.isRunning()) ani.stop();
                //지금 움직이고 있으면 멈춰
                ani.setOneShot(true);
                //AnimationDrawable 객체 Frame 변경을 시작
                ani.start();

                int num = rnd.nextInt(gam_interaction.length); //랜덤 숫자 생성
                click_gam_msg.setVisibility(View.INVISIBLE);
                gam_say.setText(""); //초기화
                gam_say.setText(gam_interaction[num]); //위에서 담아놓은 문구 중 랜덤하게 가져옴
                gam_say.setVisibility(View.VISIBLE); //랜덤 문구 보여지게


                mHandler.postDelayed(mMyTask, 4000); // 4초후에 감 문구 안 보이게
                mMyTask = new Runnable() {
                    @Override
                    public void run() {
                        gam_say.setVisibility(View.INVISIBLE);
                    }
                };

            }
        });

        //오늘 시간 띄우는 부분 !
        SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
        formatH = new SimpleDateFormat("yyyy년 MM월 dd일"); //formatH에 현재 시간 넣어줌 대소문자 중요함
        Date today = new Date(); //today 변수에 Date 부르기
        String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326
        TextView today_date = (TextView) findViewById(R.id.today_date);
        today_date.setText(strDate);


        //part 4 - 각 카테고리에 해당하는 버튼들로 이동하는 코드
        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);


        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //네비게이션 메뉴 ( 고객의 소리함 , 마이페이지 버튼 담고 있음)
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.setDisplayHomeAsUpEnabled(true);
//        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId()==R.id.mypage) {
                Log.i("successclick", "nav_mypage");
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                startActivity(intent);
                finish();
            }
            else if(menuItem.getItemId()==R.id.customsound){
                    Log.i("successclick","nav_customersound");
                    Intent intentt =new Intent(getApplicationContext(), customer_sound.class);
                    intentt.putExtra("f_code", f_code);
                    intentt.putExtra("introduce", introduce);
                    intentt.putExtra("user_name", user_name);
                    intentt.putExtra("user_color", user_color);
                    intentt.putExtra("user_gam", user_gam);
                    startActivity(intentt);
                    finish();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
            drawer.closeDrawer(GravityCompat.END);
            return true;
        }
        });



        //왔다감 버튼
        ImageButton go_qna = (ImageButton) findViewById(R.id.qna_btn);
        go_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
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
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_gam", user_gam);
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
                intent.putExtra("f_code", f_code);
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
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("user_name", user_name);
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
                intent.putExtra("f_code", f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}