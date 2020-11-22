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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registeractivity extends AppCompatActivity {
    private static final String TAG = "Registeractivity";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextBirth;
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
        setContentView(R.layout.activity_registeractivity); //registeractivity 와 연결

        firebaseAuth = FirebaseAuth.getInstance();  //auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database 초기화

        //xml 속 id값과 연결 & 변수할당
        editTextEmail = (EditText) findViewById(R.id.editText_id);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextPhone = (EditText) findViewById(R.id.editText_Phone);
        editTextBirth = (EditText) findViewById(R.id.edit_birth);

        //체크박스
        check1 = (CheckBox) findViewById(R.id.check_1);
        check2 = (CheckBox) findViewById(R.id.check_2);
        check3 = (CheckBox) findViewById(R.id.check_3);
        checkall = (CheckBox) findViewById(R.id.check_all);  //모두 체크하는 거

        buttonJoin = (Button) findViewById(R.id.btn_join);  //회원가입 버튼


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
        });
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
        });

        // 전체동의
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check1.setChecked(true);   //싹 다 true로 바꾸기
                    check2.setChecked(true);
                    check3.setChecked(true);
                    TERMS_AGREE_4 = 1;
                } else {
                    check1.setChecked(false);    //싹 다 체크 해제
                    check2.setChecked(false);
                    check3.setChecked(false);
                    TERMS_AGREE_4 = 0;
                }
            }
        });


        //회원가입 버튼을 누르면
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //모든 정보 입력했는지 확인
                String id = editTextEmail.getText().toString();
                String pwd = editTextPassword.getText().toString();
                String phone = editTextPhone.getText().toString();
                String birth = editTextBirth.getText().toString();
                String name = editTextName.getText().toString();
                if (!id.equals("") && !pwd.equals("") && !phone.equals("") && !name.equals("") && !birth.equals("")) {
                    // 모든 칸이 공백이 아닐때

                    // 전체 약관 체크여부
                    if (TERMS_AGREE_4 != 1) {
                        // 첫번째 약관 체크여부
                        if (TERMS_AGREE_3 == 1) {
                            // 두번째 약관 체크 여부
                            if (TERMS_AGREE_2 == 1) {
                                //세번재 약관 체크 여부
                                if (TERMS_AGREE_1 == 1) {
                                    // 페이저 login 액티비티로 고고
                                    startActivity(new Intent(Registeractivity.this, log_inactivity.class));
                                } else {

                                    Toast.makeText(Registeractivity.this, "모든 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(Registeractivity.this, "모든 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            Toast.makeText(Registeractivity.this, "모든 약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // 전체 약관 체크된경우
                    else {                //hashmap 만들기
                        HashMap result = new HashMap<>();
                        result.put("name", name);
                        result.put("phone", phone);
                        result.put("birth", birth);

                        createUser(id, pwd, phone, birth, name);   //새로운 유저 만들기 함수로 넘어감!
                    }

                    // 약관 동의 내용 끝

                } else {
                    // 하나라도 공백이 있는 경우
                    Toast.makeText(Registeractivity.this, "회원정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();   //알림 메세지 띄움
                }
            }
        });
    }

    private void createUser(String id, String pwd, final String phone, final String birth, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(id, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            final String uid = task.getResult().getUser().getUid(); //생성된 사람의 id를 uid라는 변수에 저장
                            UserModel usermodel = new UserModel(name, uid, phone, birth);
                            mDatabase.child("users").child(uid).setValue(usermodel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            Toast.makeText(Registeractivity.this, "회원가입 및 db저장 완료했습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Registeractivity.this, log_inactivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            Toast.makeText(Registeractivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Registeractivity.this, Registeractivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            // database에 저장
                            finish();

                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(Registeractivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registeractivity.this, Registeractivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}