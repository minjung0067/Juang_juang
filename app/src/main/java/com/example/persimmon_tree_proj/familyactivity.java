package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode; //가족코드생성 버튼
    private Button btn_profileok; //코드 확인 버튼
    private EditText et_code; //코드 입력 text
    private String str; //입력한 코드 str로 바꿀 string 변수
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성

    private FirebaseDatabase mDatabase;


    //입력한 코드가 맞는지 확인하고 맞으면 메인으로 회원가입 완료 + main으로 넘어가는 부분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyactivity);

        et_code = findViewById(R.id.et_code);
        btn_makecode = findViewById(R.id.btn_makecode);
        btn_profileok = findViewById(R.id.btn_profileok);
        //findViewById : activity_familyactivity.xml에서 위에 선언한 친구들을 찾아라

        btn_profileok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = et_code.getText().toString(); //str에다가 code넣어줌
                mDatabase = FirebaseDatabase.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인한 사람이 user
                mDatabase.getReference("users").child(user.getUid()).child("fcode").setValue(str); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기
                Intent intent = new Intent(getApplicationContext(), MakeProfile.class); //바로 프로필 만들러 ㄱㄱ
                startActivity(intent);
                //+추가로 만든사람 이름 입력하거나 그런 거 해서 남의 가족 거에 안 들어가게 해야할 듯!?
            }
        });

        //초대 코드 맞으면 메인으로 넘어가는 부분 끝

        //초대 코드 새로 만들러 가는 부분 시작
        btn_makecode.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v){  //새로운 코드 만들러 가는 부분
                Intent intent = new Intent(getApplicationContext(), CodeActivity.class); //코드 생성 xml로 이동
                    startActivity(intent);
                }
        //초대코드 생성 부분 끝
                });
            }
    }