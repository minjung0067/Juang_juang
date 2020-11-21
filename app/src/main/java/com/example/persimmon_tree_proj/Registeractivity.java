package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.Juang_juang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registeractivity extends AppCompatActivity {
    private static final String TAG = "Registeractivity";
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonJoin; //회원가입 버튼

    //체크박스 체크 여부 확인하는 변수들들 // No Check = 0, Check = 1
    public int TERMS_AGREE_1 = 0;   //첫번째 약관 동의 버튼
    public int TERMS_AGREE_2 = 0;   //두번째 약관 동의 버튼
    public int TERMS_AGREE_3 = 0;   //세번째 약관 동의 버튼
    public int TERMS_AGREE_4 = 0;   //전체 약관 동의

    //체크박스
    CheckBox check1; // 첫번째 체크박스 동의
    CheckBox check2; // 두번째 체크박스 동의
    CheckBox check3; // 두번째 체크박스 동의
    CheckBox checkall; // 전체 체크박스 동의

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);

       //체크박스
        check1 = (CheckBox) findViewById(R.id.check_1);
        check2 = (CheckBox) findViewById(R.id.check_2);
        check3 = (CheckBox) findViewById(R.id.check_3);
        checkall = (CheckBox) findViewById(R.id.check_all);

        buttonJoin = (Button) findViewById(R.id.btn_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(Registeractivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });


    // 첫번째 항 동의
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {   //체크 안되어있으면
                TERMS_AGREE_1 = 1;    //0을 1로
            } else {   //이미 체크 되어있으면
                TERMS_AGREE_1 = 0;    //1을 0으로
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
            }
        }
    }
        );
    // 세번째 항 동의
        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                TERMS_AGREE_3 = 1;
            } else {
                TERMS_AGREE_3 = 0;
            }
        }
    }
        );

    // 전체동의
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                check1.setChecked(true);   //나머지 싹 다 true로 바꾸기
                check2.setChecked(true);
                check3.setChecked(true);
                TERMS_AGREE_4 = 1;
            } else {
                check1.setChecked(false);    //나머지 싹 다 체크 해제
                check2.setChecked(false);
                check3.setChecked(false);
                TERMS_AGREE_4 = 0;
            }
        }
    }
        );

        buttonJoin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 전체 약관 체크여부
            if (TERMS_AGREE_4 != 1) {
                // 첫번째 약관 체크여부
                if (TERMS_AGREE_3 == 1) {
                    // 두번째 약관 체크 여부
                    if (TERMS_AGREE_2 == 1) {
                        //세번재 약관 체크 여부
                        if (TERMS_AGREE_1 == 1) {

                            // 계약 동의 화면을 true로 설정해서, 다음에는 true일떄때는 진행 안 하도록

                            // 페이저 login 액티비티로 고고
                            startActivity(new Intent(Registeractivity.this, log_inactivity.class));
                        } else {

                            Toast.makeText(Registeractivity.this, "약관을 체크해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(Registeractivity.this, "약관을 체크해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(Registeractivity.this, "약관을 체크해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // 전체 약관 체크된경우
            else {
                Intent intent = new Intent(Registeractivity.this, log_inactivity.class);
                startActivity(intent);
            }
        }

    });
    }


    // 약관 동의 내용 끝


    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(Registeractivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(Registeractivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}