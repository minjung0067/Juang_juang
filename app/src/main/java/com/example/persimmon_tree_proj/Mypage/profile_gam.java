package com.example.persimmon_tree_proj.Mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class profile_gam extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private ImageView gam_1;
    private ImageView gam_2;
    private ImageView gam_3;
    private ImageView gam_4;
    private ImageView gam_5;
    private ImageView gam_6;
    private ImageView gam_7;
    private ImageView gam_8;
    private String clicked_gam = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gam);

        gam_1 = (ImageView) findViewById(R.id.gam1);
        gam_2 = (ImageView) findViewById(R.id.gam2);
        gam_3 = (ImageView) findViewById(R.id.gam3);
        gam_4 = (ImageView) findViewById(R.id.gam4);
        gam_5 = (ImageView) findViewById(R.id.gam5);
        gam_6 = (ImageView) findViewById(R.id.gam6);
        gam_7 = (ImageView) findViewById(R.id.gam7);
        gam_8 = (ImageView) findViewById(R.id.gam8);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();



        ImageButton ok = (ImageButton) findViewById(R.id.ok_btn);  //선택완료 버튼
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue(clicked_gam);
                Intent intent = new Intent(profile_gam.this, profile_color.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        gam_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_1);
            }
        });
        gam_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_2);
            }
        });
        gam_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_3);
            }
        });
        gam_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_4);
            }
        });
        gam_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_5);
            }
        });
        gam_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_6);
            }
        });
        gam_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_7);
            }
        });
        gam_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(gam_8);
            }
        });
    }

    public void Reset(){
        gam_1.setBackgroundResource(R.drawable.btn_drawable);
        gam_2.setBackgroundResource(R.drawable.btn_drawable);
        gam_3.setBackgroundResource(R.drawable.btn_drawable);
        gam_4.setBackgroundResource(R.drawable.btn_drawable);
        gam_5.setBackgroundResource(R.drawable.btn_drawable);
        gam_6.setBackgroundResource(R.drawable.btn_drawable);
        gam_7.setBackgroundResource(R.drawable.btn_drawable);
        gam_8.setBackgroundResource(R.drawable.btn_drawable);
    }

    public void Click ( final View view){  //버튼 클릭시마다 switch문으로 다른 감 프로필 선택
        Reset();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //선택한 감 변수에 담기
                switch (view.getId()) {
                    case R.id.gam1:
                        clicked_gam = "1";
                        gam_1.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam2:
                        clicked_gam = "2";
                        gam_2.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam3:
                        clicked_gam = "3";
                        gam_3.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam4:
                        clicked_gam = "4";
                        gam_4.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam5:
                        clicked_gam = "5";
                        gam_5.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam6:
                        clicked_gam = "6";
                        gam_6.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam7:
                        clicked_gam = "7";
                        gam_7.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                    case R.id.gam8:
                        clicked_gam = "8";
                        gam_8.setBackgroundResource(R.drawable.btn_clicked);
                        break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MakeProfile.class); //코드 생성 activity로 이동
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}