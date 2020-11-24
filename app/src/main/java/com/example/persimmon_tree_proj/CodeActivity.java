package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity {
    private Button bt_share;
    private TextView tv_code;
    private EditText et_introduce;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요

    ArrayList<Integer> codelist = new ArrayList<Integer>(); //code번호 4자리를 넣을 배열 선언

    public ArrayList<Integer> makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.textView); //초기화
        for(int i=0;i<4;i++){ //총4자리 수 코드 만들기
            int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            codelist.add(randomNum); //코드 배열에 난수 넣기
            //tv_code += randomNum.toString(); //code 부분에 랜덤으로 한 자리씩 넣기 수정이 필요함 ㅠ
            //str = et_code.getText().toString(); // textview에선 int 값 못들어가니까 string으로 전환 수정이 필요함
        }
        return codelist;
    }

    public void share(){//공유하기

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기


//        Intent intent = getIntent();
//        String str_code = intent.getStringExtra("str_code"); //family activity
        Integer familycode;
        mDatabase.child("users").child(familycode).child("fcode").setValue(codelist); //전체 객체를 다 쓰지 않고 하위 항목 업데이트 하는 방식으로 family code 할당하기



    }
}