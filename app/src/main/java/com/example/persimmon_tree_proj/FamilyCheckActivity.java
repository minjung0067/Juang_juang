package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Juang_juang.R;

import java.util.ArrayList;

public class FamilyCheckActivity extends AppCompatActivity {

    private EditText et_introduce;
    private Button btn_profileok;
    private TextView tv_gohome; //홈으로 가기 링크 걸어야됨

    private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요
    mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_check); //초기 프로필 설정 xml 보여주기

        et_introduce = findViewById(R.id.et_introduce); //한줄 자기 소개 찾아오기


        Button btn_profileok = (Button) findViewById(R.id.btn_profileok);
        btn_profileok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), familyactivity.class);
                startActivity(intent);
            }
        });


    }
}