package com.example.persimmon_tree_proj.Family;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RevisepopupActivity extends AppCompatActivity {
    private String f_code;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseDatabase aDatabase;
    private DatabaseReference aReference;

    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        setContentView(R.layout.activity_revisepopup);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));





        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code");

        //확인 누르면 파이어베이스에 count 올라감.
        Button btn_ok = (Button)findViewById(R.id.btn_okay);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //감나무 시작하기를 누르면, 가족 int move = 0; //파이어베이스에 저장되면 이동하도록 함.
                int move = 0;

                revisecode();

                move = 1;

                if (move == 1){
                    Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
            }
        });

        Button btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //감나무 시작하기를 누르면, 가족 int move = 0; //파이어베이스에 저장되면 이동하도록 함.
                Intent intent = new Intent(getApplicationContext(), Waitactivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    public void revisecode(){

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser(); //파이어베이스에서 user를 가져와서

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");

        mReference.child(user.getUid()).child("captain").removeValue();
        mReference.child(user.getUid()).child("fcode").removeValue();


        aDatabase = FirebaseDatabase.getInstance();
        aReference = aDatabase.getReference("groups");

        aReference.child(f_code).child("members").child(user.getUid()).removeValue();

    }
}