package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyCheckActivity extends AppCompatActivity {

    private EditText et_introduce;
    private Button btn_profileok;
    private TextView tv_gohome; //홈으로 가기 링크 걸어야됨

    private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_check); //초기 프로필 설정 xml 보여주기


        mDatabase = FirebaseDatabase.getInstance().getReference(); //database초기화라서 oncreate밑으로 내려가야함

        et_introduce = findViewById(R.id.et_introduce); //xml 에서 한줄 자기 소개 id 찾아서 변수에 할당하기
        final String introduce = et_introduce.getText().toString(); //변수에 입력된 내용을 string으로 바꿔서 introduce에 넣기



        Button btn_profileok = (Button) findViewById(R.id.btn_profileok);
        btn_profileok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users");
                SharedPreferences comefile = getSharedPreferences("saveprofile",MODE_PRIVATE);//저장된 값을 불러오기 위해 네임파일 saveprofile을 찾음
                String name = comefile.getString("name","");//key에 저장된 값이 있는지 확인 없으면 ""반환
                mDatabase.child("users").child(name).child("introduce").setValue(introduce);  //database에 users안에 본인 이름 name 아래 항목의 introduece의 내용 업로드
                //mDatabase.child("users").child(name).setValue(usermodel) //database에 users 안에 usermodel의 내용으로 업로드
                //HashMap result = new HashMap<>();  //database 올릴 때 사용
                //result.put("introduce", introduce);
                //Intent intent = new Intent(getApplicationContext(), familyactivity.class);
                //startActivity(intent);
            }
        });


        //만약 초기 프로필 작성이 이미 완료된 사람이라면 바로 main 화면으로 넘기기
        //if(et_introduce!=null)
    }
}
