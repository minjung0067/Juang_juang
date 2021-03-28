package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.log_inactivity;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Profile.MakeProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private String myfcode;
    private String introduce;

    private boolean autoboolean = false; //현재 uid = auth에 등록된 uid가 true false로 구분함.
    //구분된 autoboolean을 가지고 true라면 자동로그인 false라면 log_activity로 인텐트
    //만약 회원가입 절차 중 out되었다면, true라면에 들어간 if문에서 걸려서 해당 activity로 intent!


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SharedPreferences auto = getSharedPreferences("auto", AppCompatActivity.MODE_PRIVATE);
//        final SharedPreferences.Editor autoLogin = auto.edit();
//        //자동로그인을 위한 파일명 auto SharedPreference 선언
//
//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {  //로그인 되는 경우 MainActivity로 이동
//                FirebaseUser user = firebaseAuth.getCurrentUser();    //파이어베이스에서 user 가져와서
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");//users에서 현 uid 가진 사람 찾기
//                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        myfcode = dataSnapshot.child("fcode").getValue(String.class);
//                        introduce = dataSnapshot.child("introduce").getValue(String.class);
//                        if (myfcode == null) {//코드가 없으면
//                            Intent intentt = new Intent(getApplicationContext(), familyactivity.class);
//                            intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intentt);
//                            finish();
//                        } else { //코드 있으면
//                            if (introduce == null) {//한줄 소개 없으면
//                                Intent intentt = new Intent(getApplicationContext(), MakeProfile.class);
//                                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intentt);
//                                finish();
//                            } else { //한줄소개까지 있으면
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                //자동로그인이 되었다면, Mainactivity로 바로 이동
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        throw databaseError.toException();
//                        //아니라면?에 대한 것 추가해야함
//                    }
//                });
//
//
//            }
//        };

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), log_inactivity.class);
                startActivity(intent);
                finish();
            }
        },3000); //splash screen 이 등장하는 시간
    }

    protected void onPause(){
        super.onPause();
        finish();
    }
}