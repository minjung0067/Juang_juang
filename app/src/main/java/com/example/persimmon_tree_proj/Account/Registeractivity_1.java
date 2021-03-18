package com.example.persimmon_tree_proj.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Profile.MakeProfile;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Registeractivity_1 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private Button buttonLogIn;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private String loginId; //자동 로그인 아이디
    private String loginPwd; //자동 로그인 비밀번호
    private String loginUid; //자동 로그인 비밀번호

    private String myfcode;
    private String introduce;
    private Button buttonRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        firebaseAuth = firebaseAuth.getInstance();
        editTextEmail = (EditText)findViewById(R.id.editText_email);
        editTextPassword = (EditText)findViewById(R.id.editText_passWord);

        SharedPreferences auto = getSharedPreferences("auto", AppCompatActivity.MODE_PRIVATE);
        final SharedPreferences.Editor autoLogin = auto.edit();
        //자동로그인을 위한 파일명 auto SharedPreference 선언
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        loginUid = auto.getString("inputUid", null);
        //키 값은 자유, 값은 null
        //login된 값(설정값을)저장하기 위한 변수

        buttonLogIn = (Button) findViewById(R.id.btn_login);   //로그인 버튼
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {   //둘 다 비어있지 않으면
                    loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    //inputId와 inputPwd에 이메일, 비밀번호 저장
                    autoLogin.putString("inputId", editTextEmail.getText().toString());
                    autoLogin.putString("inputPwd", editTextPassword.getText().toString());
                    if(user != null){
                        autoLogin.putString("inputUid", firebaseAuth.getCurrentUser().getUid());
                        autoLogin.commit(); //값 저장
                    }
                    else{

                    }


                    firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {  //로그인 되는 경우 MainActivity로 이동
                            FirebaseUser user = firebaseAuth.getCurrentUser();    //파이어베이스에서 user 가져와서
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");//users에서 현 uid 가진 사람 찾기
                            reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    myfcode = dataSnapshot.child("fcode").getValue(String.class);
                                    introduce = dataSnapshot.child("introduce").getValue(String.class);
                                    if (myfcode == null) {//코드가 없으면
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    } else { //코드 있으면
                                        if (introduce == null) {//한줄 소개 없으면
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                        } else { //한줄소개까지 있으면
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            //자동로그인이 되었다면, Mainactivity로 바로 이동
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException();
                                }
                            });


                        }
                    };

                } else {   //아니라면
                    Toast.makeText(Registeractivity_1.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();   //입력하라고 토스트 띄움
                }
            }
        });

        buttonRegister = (Button)findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeractivity_1.this, Registeractivity_2.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)   //이메일 패스워드 방식으로 로그인하는 함수
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(Registeractivity_1.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);   //파이어베이스에 사용자 추가
                        } else {
                            // 로그인 실패
                            Toast.makeText(Registeractivity_1.this, "아이디 또는 비밀번호를 다시 한번 확인해달라감!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}