package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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


public class CodeActivity extends AppCompatActivity {
    private Button bt_share;
    private TextView tv_code;
    private EditText et_introduce;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요
    private String str_code;
    private int tf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기

        str_code = makeCode();
        checkDatabase(str_code);

        Toast.makeText(CodeActivity.this, "가족 코드가 생성이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
        //mDatabase.removeEventListener();
        Intent intent = new Intent(getApplicationContext(),MakeProfile.class);
        startActivity(intent);

    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.textView); //초기화
        //그렇게하고 공유하기 누르면 복사 되는거 만들기
        //홈으로 가기 만들기
        //코드 생성 후 intent로 profile만드는 xml로 이동하기


        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            str_code += Integer.toString(randomNum);
        }
    return str_code;
    }

    //groups라는 루트 노드 아래에 str_code 6자리를 키 값과 value로 가지는 노드 생성
    private void writeGroupFamily(String str_code){
        GroupFamily groupFamily = new GroupFamily(str_code);
        mDatabase.child("groups").child(str_code).setValue(str_code);
    }

    public void checkDatabase(final String str_code) {

        //현재 코드가 생성하는 사용자 이름을 디바이스에 저장된 파일에서 불러오기 위함
        SharedPreferences comefile = getSharedPreferences("saveprofile", MODE_PRIVATE); // 저장된 값을 불러오기 위해 네임파일 saveprofile을 찾음
        final String name = comefile.getString("name", ""); //key에 저장된 값이 있는지 확인 없으면 ""반환

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //같은 코드가 groups 안에 있는지 확인하는 for문
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue().equals(str_code)) { //str_code랑 원래 기존에 있던 코드랑 같다면
                        tf = 1; //있는지 없는지 true false 알려줌 있으면 1 없으면 0(기존 설정 값)
                    }
                }

                //tf ==0 같은 코드가 없다면 새로운 코드 디바이스, 코드에 저장
                if (tf == 0) {
                    SharedPreferences saveprofile = getSharedPreferences("saveprofile", MODE_PRIVATE); //sharedpreferences를 saveprofile이름, 기본모드로 설정함
                    SharedPreferences.Editor editor = saveprofile.edit();//저장하기 위해 editor를 이용하여 값 저장
                    editor.putString("fcode", String.valueOf(str_code));//코드 저장
                    editor.commit(); //최종 커밋 커밋 안하면 저장 안됨

                    mDatabase.child("users").child(name).child("fcode").setValue(str_code); //user 이름 개인정보 있는 데이터 베이스에 올리기
                    writeGroupFamily(str_code);//새로운 key, value 추가하는 방식으로 writeGroupFamily함수를 불러서 group에 추가함
                    tv_code.setText(str_code);//화면에 code출력하기
                }
                //tf ==1 같은 코드가 있음 로그에 동일한 가족 코드 발생한 정보를 띄우고 makecode다시 실행함
                else  {
                    Log.i("가족 코드", "동일한 가족코드가 발생하였음");
                    makeCode(); //기존 가족 코드랑 같은 값이 나왔다면 코드 생성 함수를 다시 실행하라
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("코드 엑티비티", "groups 안에 하위 노드를 읽지 못하였음");
            }
        };
    }

    public void share(){//공유하기

    }

}
