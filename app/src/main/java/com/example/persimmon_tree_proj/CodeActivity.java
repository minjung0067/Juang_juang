package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;


public class CodeActivity extends AppCompatActivity {
    private Button btn_profileok;
    private TextView tv_code;
    private Button ok;
    private EditText et_introduce;
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    //private FirebaseDatabase database;
    //private DatabaseReference mDatabase; //데이터베이스에서 데이터 읽고 쓰기위해 인스턴스 필요
    private String str_code = "";
    private int tf = 0;
    //code 공유하기 누를때 복사하기 위한 string형 code
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code); //code xml 보여주기
        ok = (Button)findViewById(R.id.ok_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeActivity.this, Make_FamilyProfile.class);
                startActivity(intent);
            }
        });

        //----코드 생성-----
        // str_code에 6자리 숫자를 기록 할당하고 makecode안에서 checkDatabase를 돌리기 때문에 똑같은 코드가 아니면 업로드 까지
        do {
            str_code = makeCode();
            checkDatabase(str_code);
        }while(tf == 1);

        writeGroupFamily(str_code);//새로운 key, value 추가하는 방식으로 writeGroupFamily함수를 불러서 group에 추가함

        tv_code.setText(str_code);//화면에 code출력하기

        //----공유하기----
        //tv_code(text view)에 적혀있는 코드를 가져오는 방식임
        //오류가 나거나 잘안된다면 str_code(string형)을 바로 사용하는 방식으로 바꾸어도 좋을듯
        btn_profileok = (Button)findViewById(R.id.btn_ok);//btn과 java연결
        TextView textView= (TextView) findViewById(R.id.tv_code); //텍스트뷰
        final String code = textView.getText().toString();//텍스트 뷰 글자 가져옴
        btn_profileok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == ""){ //가족 코드 생성 전에 누른다면
                    Toast.makeText(CodeActivity.this, "가족코드가 생성이 되지 않았습니다. 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                }
                else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("family code", code); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                    clipboardManager.setPrimaryClip(clipData);

                    //복사가 되었다면 토스트메시지 노출
                    Toast.makeText(CodeActivity.this, "가족 코드가 복사되었습니다. 가족들에게 공유해주세요 !", Toast.LENGTH_SHORT).show();

                    //토스트 띄우고 화면 전환하기
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String myfcode = snapshot.child("fcode").getValue(String.class);
                            String user_name = snapshot.child("name").getValue().toString();
                            String introduce = snapshot.child("introduce").getValue().toString();
                            HashMap user_info = new HashMap<>();  //database 올릴 때 사용 , username이 key값이며, introduce, gam profil, color를 hashmap으로 가짐.
                            user_info.put("introduce", introduce);
                            user_info.put("user_gam", "1");
                            user_info.put("user_color", "#ffffff");
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).setValue(user_info);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(CodeActivity.this, Make_FamilyProfile.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public String makeCode(){ //코드 만드는 함수
        tv_code = (TextView) findViewById(R.id.tv_code); //초기화
        Log.i("Check function","make code");
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

        //현재 들어온 사람의 fcode 쪽에 fcode들어가게
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
        mDatabase.getReference("users").child(user.getUid()).child("fcode").setValue(str_code);

    }


    public void checkDatabase(final String str_code) {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("groups");

        FirebaseDatabase.getInstance().getReference("groups").addValueEventListener(new ValueEventListener() {

            ;     @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("value event function","data upload start");
                tf = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if ((snapshot.getValue()).equals(str_code)){//str_code랑 원래 기존에 있던 코드랑 같다면
                        tf = 1; //있는지 없는지 true false 알려줌 있으면 1 없으면 0(기존 설정 값)
                        Log.i("break","----here");
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("코드 엑티비티", "groups 안에 하위 노드를 읽지 못하였음");
            }
        });

    }

    public void share(){//공유하기
        btn_profileok = (Button)findViewById(R.id.btn_ok);//btn과 java연결
        TextView textView= (TextView) findViewById(R.id.tv_code); //텍스트뷰
        final String code = textView.getText().toString();//텍스트 뷰 글자 가져옴

        //tv_code(text view)에 적혀있는 코드를 가져오는 방식임
        //오류가 나거나 잘안된다면 str_code(string형)을 바로 사용하는 방식으로 바꾸어도 좋을듯

        if (code == ""){
            Toast.makeText(CodeActivity.this, "가족코드가 생성이 되지 않았습니다. 다시 시도해주세요.",Toast.LENGTH_LONG).show();
        }

        else {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //클립보드 사용 코드
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("family code", code); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                    clipboardManager.setPrimaryClip(clipData);

                    //복사가 되었다면 토스트메시지 노출
                    Toast.makeText(CodeActivity.this, "가족 코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();

                }
            };
        }
    }


}
