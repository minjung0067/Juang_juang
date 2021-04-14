package com.example.persimmon_tree_proj.Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_color extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private ImageView c_1;
    private ImageView c_2;
    private ImageView c_3;
    private ImageView c_4;
    private ImageView c_5;
    private ImageView c_6;
    private ImageView c_7;
    private ImageView c_8;
    private ImageView c_9;
    private String clicked_color = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_color);

        c_1 = (ImageView) findViewById(R.id.c1);
        c_2 = (ImageView) findViewById(R.id.c2);
        c_3 = (ImageView) findViewById(R.id.c3);
        c_4 = (ImageView) findViewById(R.id.c4);
        c_5 = (ImageView) findViewById(R.id.c5);
        c_6 = (ImageView) findViewById(R.id.c6);
        c_7 = (ImageView) findViewById(R.id.c7);
        c_8 = (ImageView) findViewById(R.id.c8);
        c_9 = (ImageView) findViewById(R.id.c9);


        c_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_1);
            }
        });
        c_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_2);
            }
        });
        c_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_3);
            }
        });
        c_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_4);
            }
        });
        c_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_5);
            }
        });
        c_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_6);
            }
        });
        c_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_7);
            }
        });
        c_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_8);
            }
        });
        c_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(c_9);
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        ImageButton ok = (ImageButton) findViewById(R.id.ok_btn);  //선택완료 버튼
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_color").setValue(clicked_color);
                Intent intent = new Intent(profile_color.this, MakeProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    public void Reset(){
        c_1.setBackgroundResource(R.drawable.btn_drawable);
        c_2.setBackgroundResource(R.drawable.btn_drawable);
        c_3.setBackgroundResource(R.drawable.btn_drawable);
        c_4.setBackgroundResource(R.drawable.btn_drawable);
        c_5.setBackgroundResource(R.drawable.btn_drawable);
        c_6.setBackgroundResource(R.drawable.btn_drawable);
        c_7.setBackgroundResource(R.drawable.btn_drawable);
        c_8.setBackgroundResource(R.drawable.btn_drawable);
        c_9.setBackgroundResource(R.drawable.btn_drawable);
    }

    public void Click ( final View view){  //버튼 클릭시마다 switch문으로 다른 감 프로필 선택
        Reset();
        switch (view.getId()) {
            case R.id.c1:
                clicked_color = "#F06262";
                c_1.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c2:
                clicked_color = "#FFAB47";
                c_2.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c3:
                clicked_color = "#F2D256";
                c_3.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c4:
                clicked_color = "#92C44B";
                c_4.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c5:
                clicked_color = "#4EBDEF";
                c_5.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c6:
                clicked_color = "#4F8DCB";
                c_6.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c7:
                clicked_color = "#B07DD1";
                c_7.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c8:
                clicked_color = "#B9B3BD";
                c_8.setBackgroundResource(R.drawable.btn_clicked);
                break;
            case R.id.c9:
                clicked_color = "#817889";
                c_1.setBackgroundResource(R.drawable.btn_clicked);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), profile_gam.class); //코드 생성 activity로 이동
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}