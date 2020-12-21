package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class CodeActivity extends AppCompatActivity {
    private Button bt_share;
    private TextView tv_code;
    private EditText et_introduce;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    //private FirebaseDatabase database;
    //private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요
    private String str_code = "";
    private int tf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기

        // str_code에 6자리 숫자를 기록 할당하고 makecode안에서 checkDatabase를 돌리기 때문에 똑같은 코드가 아니면 업로드 까지
        str_code = makeCode();
        tv_code.setText(str_code);//화면에 code출력하기

        //지금 바로 만들어주고 넘어가니까 공유하기 누르면 넘어가는 걸로 바꾸기
        //Toast.makeText(CodeActivity.this, "가족 코드가 생성이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
        //mDatabase.removeEventListener();
        //Intent intent = new Intent(getApplicationContext(),MakeProfile.class);
        //startActivity(intent);

    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.textView); //초기화
        Log.i("Check function","make code");
        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            str_code += Integer.toString(randomNum);
        }
        
        Toast.makeText(CodeActivity.this, "가족코드 "+ str_code +"생성 완료 ", Toast.LENGTH_SHORT).show();
        checkDatabase(str_code);
    return str_code;
    }

    //groups라는 루트 노드 아래에 str_code 6자리를 키 값과 value로 가지는 노드 생성
    private void writeGroupFamily(String str_code){
        GroupFamily groupFamily = new GroupFamily(str_code);
        mReference.child(str_code).setValue(str_code);
    }


    public void checkDatabase(final String str_code) {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("groups");

        //현재 코드가 생성하는 사용자 이름을 디바이스에 저장된 파일에서 불러오기 위함
        SharedPreferences comefile = getSharedPreferences("saveprofile", MODE_PRIVATE); // 저장된 값을 불러오기 위해 네임파일 saveprofile을 찾음
        final String name = comefile.getString("name", ""); //key에 저장된 값이 있는지 확인 없으면 ""반환
        Log.i("checkDatabase function","they run this fuction");
        FirebaseDatabase.getInstance().getReference("groups").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("value event function","data upload start");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.i("datasnapshot soob","here");
                    if (snapshot.getValue().equals(str_code)){//str_code랑 원래 기존에 있던 코드랑 같다면
                        tf = 1; //있는지 없는지 true false 알려줌 있으면 1 없으면 0(기존 설정 값)
                    }
                }

                if ((tf==0)){
                    SharedPreferences saveprofile = getSharedPreferences("saveprofile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveprofile.edit();//저장하기 위해 editor를 이용하여 값 저장
                    editor.putString("fcode", String.valueOf(str_code));//코드 저장
                    editor.commit(); //최종 커밋 커밋 안하면 저장 안됨

                    Log.i("if function","tf == 0 start upload data");
                    mReference.child("users").child(name).child("fcode").setValue(str_code); //user 이름 개인정보 있는 데이터 베이스에 올리기
                    writeGroupFamily(str_code);//새로운 key, value 추가하는 방식으로 writeGroupFamily함수를 불러서 group에 추가함
                }

                else{
                    Log.i("Check function", "same fcode");
                    //makeCode(); //기존 가족 코드랑 같은 값이 나왔다면 코드 생성 함수를 다시 실행하라
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("코드 엑티비티", "groups 안에 하위 노드를 읽지 못하였음");
            }
        });

    }

    public void share(){//공유하기
        bt_share = (Button)findViewById(R.id.btn_profileok);
    }

}
