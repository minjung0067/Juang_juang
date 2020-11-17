package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class log_inactivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextEmail;   //아이디용
    private EditText editTextPassword;   //비밀번호용
    private Button buttonLogIn; //로그인 버튼
    private Button buttonSignUp; //회원가입 버튼
    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900; //RC_SIGN_IN 상수는 구글로그인 버튼을 클릭하여 startactivityforresult 응답 코드로 사용
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 구글  로그인 버튼
    private SignInButton buttonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonGoogle = findViewById(R.id.btn_googleSignIn); //구글 로그인 버튼
        editTextEmail = (EditText) findViewById(R.id.edittext_email);   //이메일 입력한 거 변수에 저장
        editTextPassword = (EditText) findViewById(R.id.edittext_password);   //비번 저장한 거 pwd에 저장

        buttonSignUp = (Button) findViewById(R.id.btn_signup);  //회원가입으로 연결하는 버튼
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpActivity 연결
                Intent intent = new Intent(log_inactivity.this, Registeractivity.class);
                startActivity(intent);
            }
        });

        buttonLogIn = (Button) findViewById(R.id.btn_login);   //로그인 버튼
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {   //둘 다 비어있지 않으면
                    loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());    //그 email이랑 pwd 갖는 유저로 로그인
                } else {   //아니라면
                    Toast.makeText(log_inactivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();   //입력하라고 토스트 띄움
                }
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {  //로그인 안되면 회원가입으로 넘어가게
                FirebaseUser user = firebaseAuth.getCurrentUser();    //파이어베이스에서 user 가져와서
                if (user != null) {   //user 이 null이 아니라면
                    Intent intent = new Intent(log_inactivity.this, Registeractivity.class);   //회원가입으로 넘어가게함
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        };

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions); // 구글 로그인을 위한 변수지정

        buttonGoogle.setOnClickListener(new View.OnClickListener() {  //구글로그인버튼 클릭하면
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();    //구글 창이 띄워짐
                startActivityForResult(signInIntent, RC_SIGN_IN);
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
                            Toast.makeText(log_inactivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);   //파이어베이스에 사용자 추가됨
                        } else {
                            // 로그인 실패
                            Toast.makeText(log_inactivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); //로그인되어있는지 확인
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {   //구글 로그인함수
        super.onActivityResult(requestCode, resultCode, data);

        // 구글로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);   //구글 회원 데이터 가져옴
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);     //파이어베이스에 로그인한 사람 정보가 올라감!

            } catch (ApiException e) {

            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증어쩌구,,, 한다
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);   //id 토큰 가져와서
        firebaseAuth.signInWithCredential(credential)   //firebaseAuth에 내장된 함수 사용해 로그인 시킴
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {    //구글 로그인 성공과 실패 시 각각에 맞는 toast 띄우는 함수
                        if (task.isSuccessful()) {
                            // 구글로그인 성공
                            Toast.makeText(log_inactivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(log_inactivity.this, familyactivity.class);  //구글 로그인 성공시 familyactivity로 넘어가게
                                startActivity(intent);
                                finish();
                        } else {
                            // 구글로그인 실패
                            Toast.makeText(log_inactivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

}