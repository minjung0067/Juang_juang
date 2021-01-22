package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class CodeActivity extends AppCompatActivity {
    private TextView tv_code;
    private Button ok;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String str_code = "";
    private int tf = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기

        tv_code.setText(str_code);//화면에 code출력하기

        ok = (Button)findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Make_FamilyProfile.class);
                intent.putExtra("f_code",str_code);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //----코드 생성-----
        // str_code에 6자리 숫자를 기록 할당하고 makecode안에서 checkDatabase를 돌리기 때문에 똑같은 코드가 아니면 업로드 까지
        do {
            str_code = makeCode();
            checkDatabase(str_code);
        }while(tf == 1);

        writeGroupFamily(str_code);//새로운 key, value 추가하는 방식으로 writeGroupFamily함수를 불러서 group에 추가함

    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.tv_code); //초기화
        str_code = "";
        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            str_code += Integer.toString(randomNum);
        }

        return str_code;
    }

    //groups라는 루트 노드 아래에 str_code 6자리를 키 값과 value로 가지는 노드 생성
    private void writeGroupFamily(String str_code){
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("groups"); //파이어 베이스의 경로 선택함
        GroupFamily groupFamily = new GroupFamily(str_code);
        mReference.child(str_code).setValue(str_code);

    }


    public void checkDatabase(final String str_code) {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("groups");

        FirebaseDatabase.getInstance().getReference("groups").addValueEventListener(new ValueEventListener() {

            ;     @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tf = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if ((snapshot.getValue()).equals(str_code)){//str_code랑 원래 기존에 있던 코드랑 같다면
                        tf = 1; //있는지 없는지 true false 알려줌 있으면 1 없으면 0(기존 설정 값)
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}