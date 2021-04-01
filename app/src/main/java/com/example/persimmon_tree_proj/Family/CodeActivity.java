package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Juang_juang.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class CodeActivity extends AppCompatActivity {
    private TextView tv_code;
    private Button ok;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String str_code = "";
    private int tf = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기


        ok = (Button)findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Make_FamilyProfile.class);
                intent.putExtra("f_code",str_code);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //----코드 생성-----
        // str_code에 6자리 숫자를 기록 할당하고 makecode안에서 checkDatabase를 돌리기 때문에 똑같은 코드가 아니면 업로드 까지
        do {
            str_code = makeCode();
            checkDatabase(str_code);
        }while(tf == 1);

        tv_code.setText(str_code);//화면에 code출력하기
    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.tv_code); //초기화
        Random ran = new Random();
        str_code = "";
        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int num1 = (int) 48 + (int) (ran.nextDouble() * 74);
            str_code = str_code + (char) num1;
            //int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            //char random = ((char)((int)(Math.random()*26)+65)); // 랜덤 한 대문자
            //str_code += Integer.toString(randomNum);
        }

        return str_code;
    }



    public void checkDatabase(final String str_code) {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("groups");

        FirebaseDatabase.getInstance().getReference("groups").addValueEventListener(new ValueEventListener() {

            ;     @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tf = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if ((snapshot.getValue()).equals(str_code)){//str_code랑 원래 기존에 있던 코드랑 같다면
                        tf = 1; //있는지 없는지 true false 알려줌 있으면 1 없으면 0(기존 설정 값)
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}