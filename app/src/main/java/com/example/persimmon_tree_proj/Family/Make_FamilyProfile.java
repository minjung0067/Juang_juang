package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Make_FamilyProfile extends AppCompatActivity {


    Button ok; //확인버튼
    private EditText about_familys; //가족 이름
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String myfcode;

    private TextView tv_code;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String str_code = "";
    private int tf = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__familyprofile);


        about_familys = (EditText) findViewById(R.id.about_family);

        //----코드 생성-----
        // str_code에 6자리 숫자를 기록 할당하고 makecode안에서 checkDatabase를 돌리기 때문에 똑같은 코드가 아니면 업로드 까지
        do {
            str_code = makeCode();
            checkDatabase(str_code);
        }while(tf == 1);





        //확인 버튼 누르면 Waitactivity으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                reference.child(user.getUid()).child("fcode").setValue(str_code);
                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int move = 0;

                        String user_name = snapshot.child("user_name").getValue().toString();
                        FirebaseDatabase.getInstance().getReference("groups").child(str_code).child("members").child(user.getUid()).setValue(user_name);
                        String about_myfamily = about_familys.getText().toString();
                        FirebaseDatabase.getInstance().getReference("groups").child(str_code).child("f_name").setValue(about_myfamily);
                        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("captain").setValue("true");

                        move = 1;
                        if(move ==1){
                            Intent intent = new Intent(getApplicationContext(), Waitactivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public String makeCode(){ //코드 만드는 함수
        Random ran = new Random();
        str_code = "";
        for(int i=0;i<6;i++){ //총6자리 수 코드 만들기
            int num1 = (int) 48 + (int) (ran.nextDouble() * 74);
            if ((num1>=48 && num1<=57)||(num1>=65 && num1<=90)||(num1>=97 && num1<=122))    // 특수문자 제외시킴
            {
                str_code = str_code + (char) num1;
            }

            //int randomNum =(int)(Math.random()*10); //일의 자리 수 int 값 난수 생성
            //char random = ((char)((int)(Math.random()*26)+65)); // 랜덤 한 대문자
            //str_code += Integer.toString(randomNum);
        }

        return str_code;
    }

    public class GenerateCertCharacter{
        private int certCharLength = 6;
        private final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

        public String excuteGenerate() {
            Random random = new Random(System.currentTimeMillis());
            int tablelength = characterTable.length;
            StringBuffer buf = new StringBuffer();

            for(int i = 0; i < certCharLength; i++) {
                buf.append(characterTable[random.nextInt(tablelength)]);
            }

            return buf.toString();
        }

        public int getCertCharLength() {
            return certCharLength;
        }

        public void setCertCharLength(int certCharLength) {
            this.certCharLength = certCharLength;
        }
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




