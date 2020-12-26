package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_color);
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
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("1");
                        break;
                    case R.id.c2:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("2");
                        break;
                    case R.id.c3:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("3");
                        break;
                    case R.id.c4:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("4");
                        break;
                    case R.id.c5:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("5");
                        break;
                    case R.id.c6:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("6");
                        break;
                    case R.id.c7:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("7");
                        break;
                    case R.id.c8:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("8");
                        break;
                    case R.id.c9:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("9");
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
