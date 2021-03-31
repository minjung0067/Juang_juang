package com.example.persimmon_tree_proj.Family;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode; //가족코드생성 버튼
    private Button btn_ok; //코드 확인 버튼
    private EditText et_code; //코드 입력 text
    private String str; //입력한 코드 str로 바꿀 string 변수
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private int exist = 0;
    private FirebaseDatabase mDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyactivity);

        et_code = findViewById(R.id.et_code);
        btn_makecode = findViewById(R.id.btn_makecode);
        btn_ok = findViewById(R.id.btn_ok);
        //findViewById : activity_familyactivity.xml에서 위에 선언한 친구들을 찾아라

        btn_makecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btn_makecode 눌렀을 때의 처리
                Intent intent = new Intent(getApplicationContext(), CodeActivity.class); //코드 생성 activity로 이동
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = et_code.getText().toString(); //str에다가 code넣어줌
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.getReference("groups").addListenerForSingleValueEvent(new ValueEventListener() { //groups에서 실제로 그 코드가 있는지 확인함
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //tf = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if ((snapshot.getKey()).equals(str)) {
                                exist = 1;//str_code랑 원래 기존에 있던 코드에 있다면 exist = 1 , 같지 않다면, exist = 0
                                break;

                            }
                        }
                        if (exist == 0) { //코드가 없는 경우
                            Toast.makeText(familyactivity.this, "올바르지 않은 코드입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (exist == 1) { //코드가 있는 경우
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");
                            reference1.child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //count 수 가져오기
                                    int move = 0;

                                    firebaseAuth = FirebaseAuth.getInstance();
                                    FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인한 사람이 user\
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });





                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    mDatabase.getReference("users").child(user.getUid()).child("fcode").setValue(str); //database user의 정보 부분에 fcode넣기
                                    FirebaseDatabase.getInstance().getReference("answer").child("fcode").child("members").child(user.getUid()).setValue();


                                    //exist = 1이고, 가입할 수 있는 경우 자기database에 fcode추가하고 화면전환
                                    move = 1;

                                    if (move == 1) {
                                        Intent intent = new Intent(getApplicationContext(), Waitactivity.class); //코드 생성 activity로 이동
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        //초대 코드 중복 체크 + 존재하는 것만 담을 수 있게 하고
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
    }
}