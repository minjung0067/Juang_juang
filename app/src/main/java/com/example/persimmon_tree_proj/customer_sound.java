package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
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

public class customer_sound extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    InputMethodManager imm;
    private String msg;
    EditText edit_answer;

    //자기 프로필 표시를 위함
    String user_name = "";
    String user_gam = "";
    String user_color = "";

    AnimationDrawable ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sound);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        Log.i("checking f_code", f_code);
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");
        final String count = intent.getStringExtra("count");


        ImageView btn_gam = (ImageView) findViewById(R.id.gam_btn);


        ani = (AnimationDrawable) btn_gam.getDrawable();
        ani.start();
        ani.setOneShot(false);

        //답변 올리는 부분~
        edit_answer = (EditText)findViewById(R.id.edit_answer);
        firebaseAuth = FirebaseAuth.getInstance();

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);


        final FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 user 확인
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Feedback");  //users에서 현 uid 가진 사람 찾기
        Button answer = (Button)findViewById(R.id.btn_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = edit_answer.getText().toString();//edit_answer에 작성한 text msg에 저장
                if(msg.isEmpty()){
                    Toast.makeText(customer_sound.this, "아직 아무것도 작성되지 않았다감!", Toast.LENGTH_SHORT).show();
                }
                else{
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.push().setValue(msg);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(customer_sound.this, QNA_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("f_code",f_code);
                    startActivity(intent);
//                    overridePendingTransition(0, 0); //intent시 효과 없애기
                    Toast.makeText(customer_sound.this, "소중한 의견 감사합니감!", Toast.LENGTH_SHORT).show();
                    finish();
                }

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
                intent.putExtra("count", count);
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
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
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
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
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
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
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
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
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
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("user_gam", user_gam);
                intent.putExtra("count", count);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기

        finish();
        return;
    }

    //화면 터치 시 키보드 내려가게
    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(edit_answer.getWindowToken(), 0);
    }
}