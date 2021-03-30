package com.example.persimmon_tree_proj.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.html.privacyhtml;
import com.example.persimmon_tree_proj.Account.html.servicehtml;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registeractivity_1 extends AppCompatActivity {


    //xml 관련
    private EditText Email; //이메일 입력란
    private TextView checktext; //이메일 조건 확인 txt
    private EditText editTextPassword; //비번 칸
    private EditText editTextPassword2; //비번 확인 칸
    private TextView password1; //비번 조건 확인 txt
    private TextView password2; //비번 일치 확인 txt
    private Button buttonJoin; //회원가입 버튼


    //회원가입 확인 변수
    private Integer ok1 = 0; //비밀번호 확인
    private Integer ok2 = 0; //비밀번호 일치 확인


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


    //Database 확인 시 사용함.
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);


        firebaseAuth = FirebaseAuth.getInstance();  //auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database 초기화
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        auth.setLanguageCode("fr"); //인증 인스턴스의 언어 코드를 업데이트 하여 확인 메일을 현지화



        Email = (EditText)findViewById(R.id.editText_email); //이메일 입력란
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checktext.setText("이메일은 계정을 잃어버렸을 때 사용되니 정확히 기입해주세요. ");
            }
        });
        checktext = (TextView)findViewById(R.id.txt_check); //이메일 입력 시 안내 텍스트


        editTextPassword = (EditText) findViewById(R.id.editTextPassword);    //pwd 연결
        editTextPassword2 = (EditText)findViewById(R.id.editTextPassword2); //pwd 일치 연결
        password1 = (TextView)findViewById(R.id.password1); //pwd 확인 txt
        password2 = (TextView)findViewById(R.id.password2); //pwd 일치 확인 txt

        //비밀번호 입력란
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
                password1.setTextColor(Color.parseColor("#DB4455"));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = editTextPassword.getText().toString();
                ok1 = 0;
                check_validation1(input);
                Integer inputcount = input.length();
                if(inputcount < 8){
                    password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
                    password1.setTextColor(Color.parseColor("#DB4455"));
                }
                else{
                    if(ok1 == 0){
                        password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
                        password1.setTextColor(Color.parseColor("#DB4455"));
                    }
                    else{
                        password1.setTextColor(Color.parseColor("#4CAF50"));
                        password1.setText("사용할 수 있습니다.");


                        if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                            password2.setText("일치합니다.");
                            password2.setTextColor(Color.parseColor("#4CAF50"));
                        }
                        else{
                            password2.setText("비밀번호를 확인해주세요.");
                            password2.setTextColor(Color.parseColor("#DB4455"));
                        }

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //비밀번호 일치 확인 입력란
        editTextPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    password2.setTextColor(Color.parseColor("#4CAF50"));
                }
                else{
                    password2.setText("비밀번호를 확인해주세요.");
                    password2.setTextColor(Color.parseColor("#DB4455"));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    password2.setTextColor(Color.parseColor("#4CAF50"));
                }
                else{
                    password2.setText("일치하지 않습니다.");
                    password2.setTextColor(Color.parseColor("#DB4455"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    password2.setTextColor(Color.parseColor("#4CAF50"));
                }
                else{
                    password2.setText("일치하지 않습니다.");
                    password2.setTextColor(Color.parseColor("#DB4455"));
                }
            }
        });





        //xml 속 체크박스 id값과 연결 & 변수할당
        check1 = (CheckBox) findViewById(R.id.check_1);
        check2 = (CheckBox) findViewById(R.id.check_2);
        check3 = (CheckBox) findViewById(R.id.check_3);
        checkall = (CheckBox) findViewById(R.id.check_all);  //모두 체크하는 체크 박스!
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

        //개인 정보 html 연결
        btn_view1 = (Button)findViewById(R.id.btn_view1);
        btn_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeractivity_1.this, servicehtml.class);
                startActivity(intent);
            }
        });

        btn_view2 = (Button)findViewById(R.id.btn_view2);
        btn_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeractivity_1.this, privacyhtml.class);
                startActivity(intent);
            }
        });


        //회원가입 버튼을 누르면
        buttonJoin = (Button)findViewById(R.id.btn_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //자동로그인 안되게 기기에 저장된 거 지움
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장

                //email, pwd변수에 입력된 내용을 string으로 바꿔서 각각의 변수에 넣기
                String id = Email.getText().toString();
                String pwd = editTextPassword.getText().toString();
                String pwd2 = editTextPassword2.getText().toString();


                // 모든 칸이 공백이 아닐때 = 모든 칸이 입력되어있을 때
                if (!id.equals("") && !pwd.equals("") && !pwd2.equals("")) {
                    // 전체 약관 체크여부
                    if (TERMS_AGREE_1 == 0 || TERMS_AGREE_2 == 0) {
                        // 첫번째 약관 체크여부
                        Toast.makeText(Registeractivity_1.this, "필수 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 전체 약관 체크된경우
                    else {
                        check_validation1(pwd);
                        check_validation2(pwd,pwd2);
                        if(ok1 == 1){//비밀번호가 최소 8자 , 영어 대소문 , 숫자, 특수문자 사용 가능
                            if(ok2 == 1){

                                //파이어베이스 auth에 id,pwd 올리기
                                createUser(id ,pwd);
                            }
                            else{
                                //생년월일 조건에 맞지 않는 경우, ok2 = 0 인 경우
                                Toast.makeText(Registeractivity_1.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();

                            }

                        }
                        else{//비밀번호 조건에 맞지 않는 경우 ok1 = 0 인 경우
                            Toast.makeText(Registeractivity_1.this, "비밀번호는 영어,숫자,특수문자($@$!%*#?&.)를 포함한 8자 이상 입력해주세요.", Toast.LENGTH_LONG).show();   //비밀번호를 다시 입력하는 알림

                        }
                    }
                } else {
                    // 하나라도 공백이 있는 경우 = 사용자가 입력 안 한 칸이 하나라도 있으면
                    Toast.makeText(Registeractivity_1.this, "회원정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();   //알림 메세지 띄움
                }
            }
        });

    }

    private void createUser(String id, String pwd){
        firebaseAuth.createUserWithEmailAndPassword(id,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //auth에 업로드가 성공했다면 -> 회원가입 성공

                    //회원가입 완료시 log_in acitivity로 이동
                    Intent intent = new Intent(getApplicationContext(),log_inactivity.class);
                    Toast.makeText(Registeractivity_1.this, "회원가입이 되었습니다.", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
                else{
                    try{
                        task.getResult();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("Fail_register_email",e.getMessage());
                        //계정이 중복되는 경우
                        Toast.makeText(Registeractivity_1.this,"중복되는 계정이 있습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }

    void check_validation1(String password) {
        // 비밀번호 유효성 검사식1 : 숫자, 특수문자가 포함되어야 한다.

        String val_symbol = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.]).{8,}";
        // 정규표현식 컴파일
        Pattern pattern_symbol = Pattern.compile(val_symbol);


        Matcher matcher_symbol = pattern_symbol.matcher(password);


        if (matcher_symbol.find()) {
            // 최소 8자, 특수문자, 숫자, 대소문자 포함.
            Log.i("matcher_symblo.find()", String.valueOf(matcher_symbol.find()));
            ok1 = 1;
        }else {
            ok1 = 0;
        }

    }

    void check_validation2(String pwd ,String pwd2){
        //비밀번호 일치 확인
        if(pwd.equals(pwd2)){
            ok2 = 1;
        }
        else{
            ok2 = 0;
        }
    }

    //배경 선택시 키보드 내려감.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}