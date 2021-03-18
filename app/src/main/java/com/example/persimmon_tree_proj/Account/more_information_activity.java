package com.example.persimmon_tree_proj.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class
more_information_activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //회원가입 로직에 사용!
    private DatabaseReference mDatabase;   //database 사용 시 필요함
    private Button buttonok;
    private EditText editTextBirth; //생일 칸
    private EditText editTextName; //이름 칸

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information_activity);

        //소셜로그인 시 이름이나 생년월일 처럼 이메일로 가입할 때 받아야하는 정보
        //따로 받을 수 있도록 만든 페이지
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
                if(!name.equals("")) { //이름 o
                    if(!birth.equals("")){ //생일 o
                        //db에 올리기
                        reference.child(user.getUid()).child("user_name").setValue(name); //이름 넣기
                        reference.child(user.getUid()).child("birth").setValue(birth); //이름 넣기

                        //다시 로딩 페이지로 이동
                        Intent intent = new Intent(more_information_activity.this, LodingPage_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    else{ //이름 o 생일 x
                        Toast.makeText(more_information_activity.this, "생일을 알려주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                else{ //이름 x
                    Toast.makeText(more_information_activity.this, "이름을 알려주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
 }
}