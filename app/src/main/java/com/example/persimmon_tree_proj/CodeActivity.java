package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;


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

        int familycodee = 0;
        Intent intent = getIntent();
        String str_code = intent.getStringExtra("str_code"); //family activity
        Integer familycode = new Integer(familycodee);
        mDatabase.child("users").child(String.valueOf(familycode)).child("fcode").setValue(codelist); //전체 객체를 다 쓰지 않고 하위 항목 업데이트 하는 방식으로 family code 할당하기



    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.textView); //초기화
    /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스의 인스턴스를 가져온다 즉, root를 가져온다고 생각
        DatabaseReference myRef =database.getReference("groups");//Root밑에있는 groups라는 위치를 참조한다
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { }//onDataChange
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }//onCancelled
        });//addValueEventListener
        //참조 위치에 이벤트 리스너 연결참조한 위치에 데이터가 변화가 일어날때 마다 매번 읽어온다고 생각하면된다.
        //예를 들어 설명하면 현재 참조한 위치(myRef)가 users를 가리키고 있는데 users에 속한 데이터 중 하나라도 변경이 되면 매번 users 전체를 읽어온다.
    */

        //addchildEventListner로 변경하거나 참조할때 users childe fcode를 참조해서 가져옴 그렇게해서 if문 변경하고 else 동일한 fcode가 있을시 다시 fcode재생성으로 수정하기
        //가족 코드만 있는 groups에서 모든 데이터 참조 그넫 모든 데이터 가져오면 너무 시간 오래걸리지 않나? 이거 나중에 효율성을 위해서 제일 윗대가리들만 가져오는거 찾아보자
        //그렇게하고 공유하기 누르면 복사 되는거 만들기
        //홈으로 가기 만들기
        //코드 생성 후 intent로 profile만드는 xml로 이동하기

        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            str_code += Integer.toString(randomNum);
        }
        return str_code;
    }

    public TextView checkDatabase(final String str_code) {

        //현재 코드가 생성하는 사용자 이름을 디바이스에 저장된 파일에서 불러오기 위함
        SharedPreferences comefile = getSharedPreferences("saveprofile", MODE_PRIVATE); // 저장된 값을 불러오기 위해 네임파일 saveprofile을 찾음
        String name = comefile.getString("name", ""); //key에 저장된 값이 있는지 확인 없으면 ""반환

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
                    editor.putString("fcode", String.valueOf(tv_code));//코드 저장
                    editor.commit(); //최종 커밋 커밋 안하면 저장 안됨

                    mDatabase.child("users").child(name).child("fcode").setValue(tv_code); //user 이름 개인정보 있는 데이터 베이스에 올리기
                    mDatabase.child("groups").setValue(tv_code); //가족 코드 별 group 으로 묶어주는 데이터 베이스에 올리기
                    tv_code.setText(str_code);//화면에 code출력하기
                }
                //tf ==1 같은 코드가 있음 로그에 동일한 가족 코드 발생한 정보를 띄우고 makecode다시 실행함
                else {
                    Log.i("가족 코드", "동일한 가족코드가 발생하였음");
                    makeCode(); //기존 가족 코드랑 같은 값이 나왔다면 코드 생성 함수를 다시 실행하라
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("코드 엑티비티", "groups 안에 하위 노드를 읽지 못하였음");
            }
        }
    }

    public void share(){//공유하기

    }

}