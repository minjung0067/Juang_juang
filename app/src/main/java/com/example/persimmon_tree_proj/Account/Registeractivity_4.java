package com.example.persimmon_tree_proj.Account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.html.privacyhtml;
import com.example.persimmon_tree_proj.Account.html.servicehtml;
import com.example.persimmon_tree_proj.Main.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;  //database에 올릴 때 HashMap이란 걸 사용한다구 함!
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registeractivity_4 extends AppCompatActivity {
    private static final String TAG = "Registeractivity";
    private FirebaseAuth firebaseAuth; //회원가입 로직에 사용!
    private DatabaseReference mDatabase;   //database 사용 시 필요함
    private EditText editTextBirth; //생일 칸
    private EditText editTextName; //이름 칸
    private Button buttonJoin; //회원가입 버튼


    //체크박스 체크 여부 확인하는 변수들들 // No Check = 0, Check = 1
    public int TERMS_AGREE_1 = 0;   //첫번째 약관 동의 버튼
    public int TERMS_AGREE_2 = 0;   //두번째 약관 동의 버튼
    public int TERMS_AGREE_3 = 0;   //세번째 약관 동의 버튼
    public int TERMS_AGREE_all = 0;   //전체 약관 동의

    //체크박스
    CheckBox check1; // 첫번째 체크박스
    CheckBox check2; // 두번째 체크박스
    CheckBox check3; // 두번째 체크박스
    CheckBox checkall; // 전체 체크박스

    //약관 확인 html로 이동
    private Button btn_view1;
    private Button btn_view2;


    //유효성 판단
    private Integer ok1 = 0; //생년월일 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4); //registeractivity 와 연결

        Intent intent = getIntent();
        String id = intent.getStringExtra("id"); //register_3에서 받아온 이메일
        String pwd = intent.getStringExtra("pwd"); //register_3에서 받아온 비밀번호
        Log.i("eunseocheck",id);
        Log.i("eunseocheck",pwd);


        firebaseAuth = FirebaseAuth.getInstance();  //auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database 초기화

        //xml 속 id값과 연결 & 변수할당
        editTextName = (EditText) findViewById(R.id.editText_name);   //name
        editTextBirth = (EditText) findViewById(R.id.edit_birth);    //birth

        //xml 속 체크박스 id값과 연결 & 변수할당
        check1 = (CheckBox) findViewById(R.id.check_1);
        check2 = (CheckBox) findViewById(R.id.check_2);
        check3 = (CheckBox) findViewById(R.id.check_3);
        checkall = (CheckBox) findViewById(R.id.check_all);  //모두 체크하는 체크 박스!

        buttonJoin = (Button) findViewById(R.id.btn_join);  //회원가입 버튼

        btn_view1 = (Button)findViewById(R.id.btn_view1);
        btn_view2 = (Button)findViewById(R.id.btn_view2); //개인정보 확인 버튼튼


        // 첫번째 항 동의
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {   //체크 안되어있으면
                    TERMS_AGREE_1 = 1;    //0을 1로
                } else {   //이미 체크 되어있으면
                    TERMS_AGREE_1 = 0;    //1을 0으로
                    if(TERMS_AGREE_2 == 1 && TERMS_AGREE_3== 1){
                        checkall.setChecked(false);
                        TERMS_AGREE_all = 0;
                        check2.setChecked(true);
                        TERMS_AGREE_2 = 1;
                        check3.setChecked(true);
                        TERMS_AGREE_3 = 1;
                    }
                }
                if (TERMS_AGREE_1 == 1 && TERMS_AGREE_2 == 1) { //모두 동의 체크 안 하고 직접 세 개 각각 체크했으면 모두 동의도 체크로 바뀌게
                    if( TERMS_AGREE_3==1){
                        checkall.setChecked(true);
                        TERMS_AGREE_all = 1;
                    }
                }
            }
        });

        // 두번째 항 동의
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TERMS_AGREE_2 = 1;
                } else {
                    TERMS_AGREE_2 = 0;
                    if(TERMS_AGREE_1 == 1 && TERMS_AGREE_3 == 1){
                        checkall.setChecked(false);
                        TERMS_AGREE_all = 0;
                        check1.setChecked(true);
                        TERMS_AGREE_1 = 1;
                        check3.setChecked(true);
                        TERMS_AGREE_3 = 1;

                    }
                }
                if (TERMS_AGREE_1 == 1 && TERMS_AGREE_2 == 1) { //모두 동의 체크 안 하고 직접 세 개 각각 체크했으면 모두 동의도 체크로 바뀌게
                    if( TERMS_AGREE_3==1){
                        checkall.setChecked(true);
                        TERMS_AGREE_all = 1;
                    }
                }
            }
        });
         //세번째 항 동의
        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TERMS_AGREE_3 = 1;
                } else {
                    TERMS_AGREE_3 = 0;
                    if(TERMS_AGREE_1 == 1 && TERMS_AGREE_2 == 1){
                        checkall.setChecked(false);
                        TERMS_AGREE_all = 0;
                        check1.setChecked(true);
                        TERMS_AGREE_1 = 1;
                        check2.setChecked(true);
                        TERMS_AGREE_2 = 1;
                    }
                }
                if (TERMS_AGREE_1 == 1 && TERMS_AGREE_2 == 1) { //모두 동의 체크 안 하고 직접 세 개 각각 체크했으면 모두 동의도 체크로 바뀌게
                    if( TERMS_AGREE_3==1){
                        checkall.setChecked(true);
                        TERMS_AGREE_all = 1;
                    }
                }
            }
        });


        // 전체동의
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {    //모두 동의 체크 안되어 있으면
                    check1.setChecked(true);   //싹 다 true로 바꾸기
                    check2.setChecked(true);
                    check3.setChecked(true);
                    checkall.setChecked(true);
                    TERMS_AGREE_all = 1;   //모두 동의 체크 변수를 0에서 1로
                } else {
                    check1.setChecked(false);    //한번에 싹 다 체크 해제
                    check2.setChecked(false);
                    check3.setChecked(false);
                    checkall.setChecked(false);
                    TERMS_AGREE_all = 0;  //모두 동의 체크 변수를 1에서 0으로
                }
            }
        });

        btn_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeractivity_4.this, servicehtml.class);
                startActivity(intent);
            }
        });

        btn_view2.setOnClickListener(new View.OnClickListener() {//개인정보 연결
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeractivity_4.this, privacyhtml.class);
                startActivity(intent);
            }
        });


        //회원가입 버튼을 누르면
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //모든 정보 입력했는지 확인
                //email, pwd, birth, name 변수에 입력된 내용을 string으로 바꿔서 각각의 변수에 넣기
                //자동로그인 안되게 기기에 저장된 거 지움
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장

                String name = editTextName.getText().toString();
                String birth = editTextBirth.getText().toString();

                // 모든 칸이 공백이 아닐때 = 모든 칸이 입력되어있을 때
                if (!name.equals("") && !birth.equals("")) {
                    // 전체 약관 체크여부
                    if (TERMS_AGREE_1 == 0 || TERMS_AGREE_2 == 0) {
                        // 첫번째 약관 체크여부
                            Toast.makeText(Registeractivity_4.this, "필수 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                            return;
                    }
                    // 전체 약관 체크된경우
                    else {
                        check_validation(birth);
                        if(ok1 == 1){//비밀번호가 최소 8자 , 영어 대소문 , 숫자, 특수문자 사용 가능
                            //hashmap 만들기
                            HashMap result = new HashMap<>();  //database 올릴 때 사용
                            result.put("name", name);
                            result.put("birth", birth);
                            result.put("fcode", "");
                            result.put("introduce","");


                            createUser(id, pwd, birth, name);   //새로운 유저 만들기 함수로 넘어감!
                        }
                        else{//비밀번호 조건에 맞지 않는 경우 ok1 = 0 인 경우
                            Toast.makeText(Registeractivity_4.this, "생년월일은 8자로 써주세요 ex)20200912", Toast.LENGTH_LONG).show();   //비밀번호를 다시 입력하는 알림

                        }
                    }

                    // 약관 동의 내용 끝

                } else {
                    // 하나라도 공백이 있는 경우 = 사용자가 입력 안 한 칸이 하나라도 있으면
                    Toast.makeText(Registeractivity_4.this, "회원정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();   //알림 메세지 띄움
                }
            }
        });
    }





    void check_validation(String birth) {
        //생일 8자리 수로
        if(birth.length() == 8){
            ok1 = 1;
        }
        else{
            ok1 = 0;
        }

    }

    //회원가입 로직 : id/pwd는 firebase auth에, 나머지 회원정보는 firebase database에 올리는 함수
    private void createUser(String id, String pwd, final String birth, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(id, pwd)  //id와 pwd는 firebase auth에 업로드
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  //auth에 올리는 task
                        if (task.isSuccessful()) {   //auth에 업로드에 성공했다면
                            // 회원가입 성공
                            final String uid = task.getResult().getUser().getUid(); //생성된 사람의 id를 uid라는 변수에 저장
                            Log.i("uid1111111", uid);
                            UserModel usermodel = new UserModel(name, uid, birth);  //usermodel.java에서 새로운 UserModel 만듦
                            Log.i("usermodel", String.valueOf(usermodel));
                            mDatabase.child("users").child(uid).setValue(usermodel)//database에 users 안에 usermodel의 내용으로 업로드
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) { //database에 올리기 성공했으면
                                            Toast.makeText(Registeractivity_4.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                                            //activity간에 계속 인텐트로 데이터 주고 받는거보다 이름을 파일에 저장하게 되면 접근하기 쉬울거라고 생각해서 이 방식 채택함 근데 추후에 이름 바꾸는 경우 생각안해봄
                                            SharedPreferences saveprofile = getSharedPreferences("saveprofile", MODE_PRIVATE); //sharedpreferences를 saveprofile이름, 기본모드로 설정함
                                            SharedPreferences.Editor editor = saveprofile.edit();//저장하기 위해 editor를 이용하여 값 저장
                                            editor.putString("name", name);//이름 저장
                                            editor.commit(); //최종 커밋 커밋 안하면 저장 안됨


                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {  //database에 올리기 실패했으면
                                            Toast.makeText(Registeractivity_4.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            // database에 저장
                            finish();

                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(Registeractivity_4.this, "중복되는 계정이 있습니다.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}