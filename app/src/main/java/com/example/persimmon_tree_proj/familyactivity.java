package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode;
    private Button btn_ok;
    private EditText et_code;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyactivity);

        et_code = findViewById(R.id.et_code);
        btn_makecode = findViewById(R.id.btn_makecode);
        btn_ok = findViewById(R.id.btn_ok);
        //activity_familyactivity.xml에서 위에 선언한 친구들을 찾아라
        str = et_code.getText().toString(); //str에다가 code넣어줌

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(familyactivity.this, FamilyCheckActivity.class); //여기 수정해야함 FamilyCheckActivity가 아니라 메인페이
                startActivity(intent1); //acitivity 코드 확인 후 메인페이지 이동
                intent1.putExtra("str_code",str);
            }
        });


        btn_makecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(familyactivity.this, CodeActivity.class);
                startActivity(intent); //activity 초대장 코드 생성으로 이동

            }
        });

        //버튼 누르면 이걸로 실행

}