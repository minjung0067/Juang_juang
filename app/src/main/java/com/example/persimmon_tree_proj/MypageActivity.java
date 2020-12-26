package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class MypageActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView my_id;
    private TextView my_introduce;
    private TextView my_fcode;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        my_id = (TextView) findViewById(R.id.my_id);
        my_introduce = (TextView) findViewById(R.id.my_introduce);
        my_fcode = (TextView) findViewById(R.id.my_fcode);

        //현재 로그인한 user uid로 접근해서 현재 유저의 id,fcode,한 줄 소개 가져오기
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final SharedPreferences autologin = getSharedPreferences("auto",AppCompatActivity.MODE_PRIVATE);//users에서 현 uid 가진 사람 찾기
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                String myintroduce = dataSnapshot.child("introduce").getValue(String.class);
                String LoginId = autologin.getString("inputId", "");
                my_introduce.setText(myintroduce);
                my_fcode.setText(myfcode);
                my_id.setText(LoginId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        ImageButton revise = (ImageButton)findViewById(R.id.edit_btn);
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, profile_gam.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton goback = (ImageButton)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button logout = (Button) findViewById(R.id.btn_logout); //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPregerences값을 불러온다.
                Intent intent = new Intent(MypageActivity.this, log_inactivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장
                Toast.makeText(MypageActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
