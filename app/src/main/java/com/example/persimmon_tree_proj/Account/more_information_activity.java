package com.example.persimmon_tree_proj.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Mypage.profile_gam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
more_information_activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //회원가입 로직에 사용!
    private DatabaseReference mDatabase;   //database 사용 시 필요함
    private Button buttonok;
    private EditText editTextBirth; //생일 칸
    private EditText editTextName; //이름 칸

    private Integer ok1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information_activity);

        //이름 ,생년월일 받는 페이지
        //다음 activity는 make_gam

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인한 사람이 user
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        buttonok = (Button) findViewById(R.id.btn_ok);  //회원가입 버튼
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextBirth = (EditText) findViewById(R.id.edit_birth);

        //회원가입 버튼을 누르면
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                String birth = editTextBirth.getText().toString();
                check_validation2(birth);
                if(!name.equals("")) { //이름 o
                    if(!birth.equals("")){ //생일 o
                        if(ok1 == 1){
                            //생년월일 조건에 맞는 경우(8자리)
                            //db에 올리기
                            reference.child(user.getUid()).child("user_name").setValue(name); //이름 넣기
                            reference.child(user.getUid()).child("birth").setValue(birth); //이름 넣기

                            //다음 페이지인 감 페이지로 이동
                            Intent intent = new Intent(more_information_activity.this, profile_gam.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            //생년월일 조건에 맞지 않는 경우, ok2 = 0 인 경우
                            Toast.makeText(more_information_activity.this, "생년월일은 8자로 작성해주세요 (ex)20200912", Toast.LENGTH_LONG).show();
                        }


                    }
                    else{ //이름 o 생일 x
                        Toast.makeText(more_information_activity.this, "생년월일 8자리로 작성해주세요.20200925", Toast.LENGTH_LONG).show();
                    }
                }
                else{ //이름 x
                    Toast.makeText(more_information_activity.this, "이름을 알려주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    void check_validation2(String birth) {
        // 비밀번호 유효성 검사식1 : 숫자, 특수문자가 포함되어야 한다.
        if(birth.length() == 8){
            ok1 = 1;
        }
        else{
            ok1 = 0;
        }

    }
}