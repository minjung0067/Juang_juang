package com.example.persimmon_tree_proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.kakao.auth.ISessionCallback;


import com.kakao.auth.ISessionCallback;

public class log_inactivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextEmail;   //아이디용
    private EditText editTextPassword;   //비밀번호용
    private String loginId; //자동 로그인 아이디
    private String loginPwd; //자동 로그인 비밀번호
    private String loginUid; //자동 로그인 비밀번호
    private Button buttonLogIn;//로그인 버튼
    private Button buttonSignUp; //회원가입 버튼
    private Button buttonfind;
    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900; //RC_SIGN_IN 상수는 구글로그인 버튼을 클릭하여 startactivityforresult 응답 코드로 사용
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 구글  로그인 버튼
    private SignInButton buttonGoogle;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String myfcode;
    private String introduce;
    private String myfam_count;
    private String myfam_introduce;


    //네이버로그인
    ImageView ll_naver_login;
    Button btn_logout;

    OAuthLogin mOAuthLoginModule;
    Context mContext;


    //카카오로그인
    private com.kakao.usermgmt.LoginButton kakao_login;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inactivity);

        //카카오톡 로그인
        kakao_login = (com.kakao.usermgmt.LoginButton) findViewById(R.id.btn_kakao_login);

        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, log_inactivity.this);
            }
        });




        firebaseAuth = FirebaseAuth.getInstance();
        buttonGoogle = findViewById(R.id.btn_googleSignIn); //구글 로그인 버튼
        editTextEmail = (EditText) findViewById(R.id.edittext_email);   //이메일 입력한 거 변수에 저장
        editTextPassword = (EditText) findViewById(R.id.edittext_password);   //비번 저장한 거 pwd에 저장

        //google 로그인 버튼 텍스트 바꾸기
        TextView textView = (TextView) buttonGoogle.getChildAt(0);
        textView.setText("Google signin");



        buttonSignUp = (Button) findViewById(R.id.btn_signup);  //회원가입으로 연결하는 버튼
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(log_inactivity.this, Registeractivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences auto = getSharedPreferences("auto", AppCompatActivity.MODE_PRIVATE);
        final SharedPreferences.Editor autoLogin = auto.edit();
        //자동로그인을 위한 파일명 auto SharedPreference 선언
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        loginUid = auto.getString("inputUid", null);
        //키 값은 자유, 값은 null
        //login된 값(설정값을)저장하기 위한 변수

        //검사하면서 자동로그인!!!!!!
        FirebaseUser user = firebaseAuth.getCurrentUser();    //파이어베이스에서 user 가져와서
        if (FirebaseAuth.getInstance().getCurrentUser() != null){  //아예 생전 로그인 안해본 애면 바로 넘어가게 함
            if (user.getUid().equals(loginUid) && (loginId != null)) {
                //한번 로그인한 적 있고
                //log_inaxtivity로 들어왔을 때 loginID와 loginPwd값을 가져와서 null이 아니라면,
                //현재 로그인한 user uid로 접근해서 fcode 랑 한줄 소개 있는 애만 자동로그인 되게
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");//users에서 현 uid 가진 사람 찾기
                reference.child(loginUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myfcode = dataSnapshot.child("fcode").getValue(String.class);
                        introduce = dataSnapshot.child("introduce").getValue(String.class);
                        firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("family");
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //myfam_count = dataSnapshot.child(myfcode).child("count").getValue(String.class);
                                //myfam_introduce = dataSnapshot.child(myfcode).child("family_name").getValue(String.class);
                                if (myfcode == null) {//코드가 없으면
                                    Intent intent = new Intent(getApplicationContext(),familyactivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(log_inactivity.this, "아직 가족코드가 없어요!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else { //코드 있으면
                                    myfam_count = dataSnapshot.child(myfcode).child("count").getValue(String.class);
                                    myfam_introduce = dataSnapshot.child(myfcode).child("family_name").getValue(String.class);
                                    if (myfam_introduce==null || myfam_count.equals("0")==true){ //코드 만드는 사람이 아예 가족 프로필 안 만들었으면
                                        Intent intenttt = new Intent(getApplicationContext(), Make_FamilyProfile.class);
                                        intenttt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intenttt);
                                        Toast.makeText(log_inactivity.this, "아직 가족 프로필을 만들지 않았어요!!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else{
                                        if (myfam_count.equals("")==true || myfam_introduce.equals("")==true){ //코드 만든 사람이 가족 프로필 안 만들었는데 다른 사람이 코드 치고 들어온 경우
                                            Intent intenttt = new Intent(getApplicationContext(), Make_FamilyProfile.class);
                                            intenttt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intenttt);
                                            Toast.makeText(log_inactivity.this, "아직 가족 프로필을 만들지 않았어요!!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else{
                                            if (introduce == null) {//한줄 소개 없으면
                                                Intent intentt = new Intent(getApplicationContext(), MakeProfile.class);
                                                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intentt);
                                                Toast.makeText(log_inactivity.this, "아직 소개를 입력하지 않았어요!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else { //한줄소개까지 있으면
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                //자동로그인이 되었다면, Mainactivity로 바로 이동
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                Toast.makeText(log_inactivity.this, "자동로그인 성공.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }

                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

            }
        }

        buttonfind = (Button)findViewById(R.id.btn_find);
        buttonfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), findaccount.class);
                //계정을 잊어버렸다면 계정 찾기 페이지로 이동
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

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
                                        Intent intentt = new Intent(getApplicationContext(), familyactivity.class);
                                        intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intentt);
                                        finish();
                                    } else { //코드 있으면
                                        if (introduce == null) {//한줄 소개 없으면
                                            Intent intentt = new Intent(getApplicationContext(), MakeProfile.class);
                                            intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intentt);
                                            finish();
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
                    Toast.makeText(log_inactivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();   //입력하라고 토스트 띄움
                }
            }
        });


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



        mContext = getApplicationContext();

        ll_naver_login = findViewById(R.id.naverlogin);
        btn_logout = findViewById(R.id.btn_logout);

        ll_naver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(
                        mContext
                        ,getString(R.string.naver_client_id)
                        ,getString(R.string.naver_client_secret)
                        ,getString(R.string.naver_client_name)
                        //,OAUTH_CALLBACK_INTENT
                        // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
                );

                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                            String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                            String tokenType = mOAuthLoginModule.getTokenType(mContext);

                            Log.i("LoginData","accessToken : "+ accessToken);
                            Log.i("LoginData","refreshToken : "+ refreshToken);
                            Log.i("LoginData","expiresAt : "+ expiresAt);
                            Log.i("LoginData","tokenType : "+ tokenType);

                        } else {
                            String errorCode = mOAuthLoginModule
                                    .getLastErrorCode(mContext).getCode();
                            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                            Toast.makeText(mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                        }
                    };
                };

                mOAuthLoginModule.startOauthLoginActivity(log_inactivity.this, mOAuthLoginHandler);
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
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);   //파이어베이스에 사용자 추가
                        } else {
                            // 로그인 실패
                            Toast.makeText(log_inactivity.this, "아이디 또는 비밀번호를 다시 한번 확인해달라감!", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(log_inactivity.this, MainActivity.class);  //구글 로그인 성공시 familyactivity로 넘어가게
                            startActivity(intent);
                            finish();
                        } else {
                            // 구글로그인 실패
                            Toast.makeText(log_inactivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
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
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        finish();
        return;
    }

}