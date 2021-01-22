package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sound);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");



        //왔다감 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customer_sound.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                finish();
            }
        });

        //공유캘린더 버튼
        ImageButton go_calendar = (ImageButton) findViewById(R.id.calendar_btn);
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                finish();
            }
        });


        //고객의 소리함
        ImageButton go_setting = (ImageButton) findViewById(R.id.setting_btn);
        go_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customer_sound.this,customer_sound.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

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
                    Intent intent = new Intent(customer_sound.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("f_code",f_code);
                    startActivity(intent);
//                    overridePendingTransition(0, 0); //intent시 효과 없애기
                    Toast.makeText(customer_sound.this, "소중한 의견 감사합니감!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        Intent intent = new Intent(customer_sound.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }

    //화면 터치 시 키보드 내려가게
    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(edit_answer.getWindowToken(), 0);
    }
}