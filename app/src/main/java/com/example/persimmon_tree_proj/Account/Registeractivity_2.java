package com.example.persimmon_tree_proj.Account;

import androidx.annotation.NonNull;
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

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registeractivity_2 extends AppCompatActivity {

    private Button Emailcheck;
    private EditText Email;
    private TextView checktext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        checktext = (TextView)findViewById(R.id.txt_check); //이메일 발송 안내 텍스트
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        auth.setLanguageCode("fr"); //인증 인스턴스의 언어 코드를 업데이트 하여 확인 메일을 현지화


        Emailcheck = (Button)findViewById(R.id.btn_emailcheck); //이메일 발송 버튼
        Emailcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.i("check","Email sent.");
                                }
                            }
                        });

                /*
                1) 계정이 중복되는 경우
                checktext.setText("중복되는 계정이 있습니다.");
                2) 이메일 형식이 아닌 경우
                checktext.setText("유효한 이메일 형식으로 적어주세요");
                3) 메일이 발송되었을 경우
                checktext.setText("이메일이 발송되었습니다. 메일에서 확인해주세요");
                 */

                //이메일 인증이 된 경우, 클릭할 수 없도록 막아놓기

                Intent intent = new Intent(Registeractivity_2.this,Registeractivity_3.class);
                startActivity(intent);


            }
         });

    }



}