package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
    private int clicked_1 = 0;
    private int clicked_2 = 0;
    private int clicked_3 = 0;
    private int clicked_4 = 0;
    private int clicked_5 = 0;
    private int clicked_6 = 0;
    private int clicked_7 = 0;
    private int clicked_8 = 0;
    private int clicked_9 = 0;

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


        Button gam = (Button) findViewById(R.id.gam);     //감캐릭터 고르기 버튼

        gam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_color.this, profile_gam.class);
                startActivity(intent);
            }
        });

        ImageButton go_back = (ImageButton) findViewById(R.id.go_back);    //뒤로가기
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_color.this, MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void Click(final View view) {  //버튼 클릭시마다 switch문으로 다른 색깔 선택
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final SharedPreferences autologin = getSharedPreferences("auto",AppCompatActivity.MODE_PRIVATE);//users에서 현 uid 가진 사람 찾기
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                String user_name = dataSnapshot.child("name").getValue(String.class);
                switch (view.getId()){
                    case R.id.c1:
                        if(clicked_1 == 0){
                            clicked_1 = 1;
                            c_1.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#9FFFBB33");
                        }
                        break;
                    case R.id.c2:
                        if(clicked_2 == 0) {
                            clicked_2 = 1;
                            c_2.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#A98BC34A");
                        }
                        break;
                    case R.id.c3:
                        if(clicked_3 == 0) {
                            clicked_3 = 1;
                            c_3.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#9FFFBB33");
                        }
                        break;
                    case R.id.c4:
                        if(clicked_4 == 0) {
                            clicked_4 = 1;
                            c_4.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#8DFF4B3B");
                        }
                        break;
                    case R.id.c5:
                        if(clicked_5 == 0) {
                            clicked_5 = 1;
                            c_5.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#C3CDDC39");
                        }
                        break;
                    case R.id.c6:
                        if(clicked_6 == 0) {
                            clicked_6 = 1;
                            c_6.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#8D3BC7FF");
                        }
                        break;
                    case R.id.c7:
                        if(clicked_7 == 0) {
                            clicked_7 = 1;
                            c_7.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#7CF4C466");
                        }
                        break;
                    case R.id.c8:
                        if(clicked_8 == 0) {
                            clicked_8 = 1;
                            c_8.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#8A0F371E");
                        }
                        break;
                    case R.id.c9:
                        if(clicked_9 == 0) {
                            clicked_9 = 1;
                            c_9.setBackgroundResource(R.drawable.btn_clicked);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#527B03F4");
                        }
                        break;

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }
}
