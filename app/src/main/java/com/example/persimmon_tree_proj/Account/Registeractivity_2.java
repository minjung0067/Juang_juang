package com.example.persimmon_tree_proj.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Juang_juang.R;

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
                checktext.setText("이메일 확인 버튼을 눌러서 발송된 메일을 확인해주세요.");

            }
        });

        Emailcheck = (Button)findViewById(R.id.btn_emailcheck); //이메일 발송 버튼
        Emailcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                1) 계정이 중복되는 경우
                checktext.setText("중복되는 계정이 있습니다.");
                2) 이메일 형식이 아닌 경우
                checktext.setText("유효한 이메일 형식으로 적어주세요");
                3) 메일이 발송되었을 경우
                checktext.setText("이메일이 발송되었습니다. 메일에서 확인해주세요");
                 */

                //이메일 인증이 된 경우, 클릭할 수 없도록 막아놓기

            }
         });

    }
}