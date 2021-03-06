package com.example.persimmon_tree_proj.Family;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode; //가족코드생성 버튼
    private Button btn_ok; //코드 확인 버튼
    private EditText et_code; //코드 입력 text
    private String str; //입력한 코드 str로 바꿀 string 변수
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private int exist = 0;
    private int move = 0;
    private String user_name;
    private String valid;
    private FirebaseDatabase mDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyactivity);



        //가족 생성 버튼 -> Codeactivity로 이동
        btn_makecode = findViewById(R.id.ok_btn);
        btn_makecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Make_FamilyProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Inputcode.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


            }
        });




    }

}