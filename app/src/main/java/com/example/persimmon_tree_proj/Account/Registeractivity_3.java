package com.example.persimmon_tree_proj.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registeractivity_3 extends AppCompatActivity {

    private EditText editTextPassword; //비번 칸
    private EditText editTextPassword2; //비번 확인 칸
    private TextView password1;
    private TextView password2; //비번 일치 확인
    private Button btn_next;

    private TextView txt_check;

    private Integer ok1 = 0; //비밀번호 확인
    private Integer ok2 = 0; //비밀번호 일치 확인

    private DatabaseReference mDatabase;   //database 사용 시 필요함
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity_3);

        firebaseAuth = FirebaseAuth.getInstance();  //auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database 초기화

        Intent intent = getIntent();

        //xml 속 id값과 연결 & 변수할당
        String id = intent.getStringExtra("id"); //register_2에서 받아온 이메일
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);    //pwd
        editTextPassword2 = (EditText)findViewById(R.id.editTextPassword2); //pwd 확인
        password1 = (TextView)findViewById(R.id.password1);
        password2 = (TextView)findViewById(R.id.password2);
        String pwd = editTextPassword.getText().toString();
        String pwd2 = editTextPassword2.getText().toString();
        btn_next = (Button)findViewById(R.id.btn_next);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok1 = 0;
                ok2 = 0;
                check_validation1(String.valueOf(editTextPassword));
                check_validation2(pwd,pwd2);
                Log.i("checkcehck", String.valueOf(ok1));
                Log.i("checkcheck2",String.valueOf(ok2));
                if(ok1 == 1){//비밀번호가 최소 8자 , 영어 대소문 , 숫자, 특수문자 사용 가능
                    if(ok2 == 1){
                        Intent intent = new Intent(Registeractivity_3.this,Registeractivity_4.class);
                        intent.putExtra("pwd",pwd); //선택한 question을 갖고 감.
                        intent.putExtra("id",id);
                        startActivity(intent);
                        overridePendingTransition(0, 0); //intent시 효과 없애기

                    }
                    else{
                        //생년월일 조건에 맞지 않는 경우, ok2 = 0 인 경우
                        Toast.makeText(Registeractivity_3.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show();

                    }

                }
                else{//비밀번호 조건에 맞지 않는 경우 ok1 = 0 인 경우
                    Toast.makeText(Registeractivity_3.this, "비밀번호는 영어,숫자,특수문자($@$!%*#?&.)를 포함한 8자 이상 입력해주세요.", Toast.LENGTH_LONG).show();   //비밀번호를 다시 입력하는 알림

                }


            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = editTextPassword.getText().toString();
                ok1 = 0;
                check_validation1(input);
                Integer inputcount = input.length();
                if(inputcount < 8){
                    password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
                }
                else{
                    if(ok1 == 0){
                        password1.setText("영어,숫자,특수문자 포함 8자 이상을 적어주세요.");
                    }
                    else{
                        password1.setText("사용할 수 있습니다.");
                        if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                            password2.setText("일치합니다.");
                            //password2.setTextColor(Integer.parseInt("#3CB354"));
                        }
                        else{
                            password2.setText("비밀번호를 확인해주세요.");

                        }

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //비밀번호 일치 확인
        editTextPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    //password2.setTextColor(Integer.parseInt("#3CB354"));
                }
                else{
                    password2.setText("비밀번호를 확인해주세요.");
                    //password2.setTextColor(Integer.parseInt("#DB4455"));
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    //password2.setTextColor(Integer.parseInt("#3CB354"));
                }
                else{
                    password2.setText("일치하지 않습니다.");
                    //password2.setTextColor(Integer.parseInt("#DB4455"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())){
                    password2.setText("일치합니다.");
                    //password2.setTextColor(Integer.parseInt("#3CB354"));
                }
                else{
                    password2.setText("일치하지 않습니다.");
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
}



