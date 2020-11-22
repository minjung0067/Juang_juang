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

import java.util.HashMap;  //database에 올릴 때 HashMap이란 걸 사용한다구 함!

public class Registeractivity extends AppCompatActivity {
    private static final String TAG = "Registeractivity";
    private FirebaseAuth firebaseAuth; //회원가입 로직에 사용!
    private DatabaseReference mDatabase;   //database 사용 시 필요함
    private EditText editTextEmail;   //id 칸
    private EditText editTextPassword; //비번 칸
    private EditText editTextPhone; //폰번호 칸
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity); //registeractivity 와 연결

        firebaseAuth = FirebaseAuth.getInstance();  //auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database 초기화

        //xml 속 id값과 연결 & 변수할당
        editTextEmail = (EditText) findViewById(R.id.editText_id);    //id
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);    //pwd
        editTextName = (EditText) findViewById(R.id.editText_name);   //name
        editTextPhone = (EditText) findViewById(R.id.editText_Phone);      //phone number
        editTextBirth = (EditText) findViewById(R.id.edit_birth);    //birth

        //xml 속 체크박스 id값과 연결 & 변수할당
        check1 = (CheckBox) findViewById(R.id.check_1);
        check2 = (CheckBox) findViewById(R.id.check_2);
        check3 = (CheckBox) findViewById(R.id.check_3);
        checkall = (CheckBox) findViewById(R.id.check_all);  //모두 체크하는 체크 박스!

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
                if (isChecked) {    //모두 동의 체크 안되어 있으면
                    check1.setChecked(true);   //싹 다 true로 바꾸기
                    check2.setChecked(true);
                    check3.setChecked(true);
                    TERMS_AGREE_all = 1;   //모두 동의 체크 변수를 0에서 1로
                }else if (TERMS_AGREE_1==1 && TERMS_AGREE_2==1 && TERMS_AGREE_3==1){    //필수 약관 모두 체크 되면
                    checkall.setChecked(true);   //모두 동의 체크란도 자동으로 체크로 바뀌게

                } else {
                    check1.setChecked(false);    //한번에 싹 다 체크 해제
                    check2.setChecked(false);
                    check3.setChecked(false);
                    TERMS_AGREE_all = 0;  //모두 동의 체크 변수를 1에서 0으로
                }
            }
        });


        //회원가입 버튼을 누르면
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //모든 정보 입력했는지 확인
                //email, pwd, phone, birth, name 변수에 입력된 내용을 string으로 바꿔서 각각의 변수에 넣기
                String id = editTextEmail.getText().toString();
                String pwd = editTextPassword.getText().toString();
                String phone = editTextPhone.getText().toString();
                String birth = editTextBirth.getText().toString();
                String name = editTextName.getText().toString();
                // 모든 칸이 공백이 아닐때 = 모든 칸이 입력되어있을 때
                if (!id.equals("") && !pwd.equals("") && !phone.equals("") && !name.equals("") && !birth.equals("")) {
                    // 전체 약관 체크여부
                    if (TERMS_AGREE_all != 1) {
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
                        HashMap result = new HashMap<>();  //database 올릴 때 사용
                        result.put("name", name);
                        result.put("phone", phone);
                        result.put("birth", birth);

                        createUser(id, pwd, phone, birth, name);   //새로운 유저 만들기 함수로 넘어감!
                    }

                    // 약관 동의 내용 끝

                } else {
                    // 하나라도 공백이 있는 경우 = 사용자가 입력 안 한 칸이 하나라도 있으면
                    Toast.makeText(Registeractivity.this, "회원정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();   //알림 메세지 띄움
                }
            }
        });
    }
    //회원가입 로직 : id/pwd는 firebase auth에, 나머지 회원정보는 firebase database에 올리는 함수
    private void createUser(String id, String pwd, final String phone, final String birth, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(id, pwd)  //id와 pwd는 firebase auth에 업로드
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  //auth에 올리는 task
                        if (task.isSuccessful()) {   //auth에 업로드에 성공했다면
                            // 회원가입 성공
                            final String uid = task.getResult().getUser().getUid(); //생성된 사람의 id를 uid라는 변수에 저장
                            UserModel usermodel = new UserModel(name, uid, phone, birth);  //usermodel.java에서 새로운 UserModel 만듦
                            mDatabase.child("users").child(uid).setValue(usermodel) //database에 users 안에 usermodel의 내용으로 업로드
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) { //database에 올리기 성공했으면
                                            Toast.makeText(Registeractivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Registeractivity.this, log_inactivity.class);   //회원가입 성공했으니 로그인 페이지로 이동
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {  //database에 올리기 실패했으면
                                            Toast.makeText(Registeractivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Registeractivity.this, Registeractivity.class); //회원가입 실패했으니 페이지 다시 로드
                                            startActivity(intent);
                                        }
                                    });
                            // database에 저장
                            finish();

                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(Registeractivity.this, "아이디(이메일 형식), 비밀번호(6자리 이상)를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registeractivity.this, Registeractivity.class);  //회원가입 실패했으니 페이지 다시 로드
                            startActivity(intent);
                        }
                    }
                });
    }
}