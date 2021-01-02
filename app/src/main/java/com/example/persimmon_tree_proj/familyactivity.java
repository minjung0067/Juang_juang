package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode; //가족코드생성 버튼
    private Button btn_ok; //코드 확인 버튼
    private EditText et_code; //코드 입력 text
    private String str; //입력한 코드 str로 바꿀 string 변수
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private int tf = 0; //가족 코드 맞는지 표시해줄 int형 변수
    private FirebaseDatabase mDatabase;
    private String count;


    //바로 가족코드 만들기 하면 넘어감
    // 근데 자꾸 intent 생성하고 넘어가는 화면전환으로 인해서 여러개가 생성되는 거 같음 ! 확인 필요함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyactivity);

        et_code = findViewById(R.id.et_code);
        btn_makecode = findViewById(R.id.btn_makecode);
        btn_ok = findViewById(R.id.btn_ok);
        //findViewById : activity_familyactivity.xml에서 위에 선언한 친구들을 찾아라
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.btn_ok: {
                        //btn_ok 눌렀을 때의 처리
                        str = et_code.getText().toString(); //str에다가 code넣어줌
                        mDatabase = FirebaseDatabase.getInstance();
                        mDatabase.getReference("groups").addValueEventListener(new ValueEventListener() { //groups에서 실제로 그 코드가 있는지 확인함
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //tf = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Log.i("familycheck", String.valueOf(snapshot.getValue()));
                                    if ((snapshot.getValue()).equals(str)) {//str_code랑 원래 기존에 있던 코드랑 같다면
                                        Log.i("familycheck", "str is = " + str);
                                        tf = 1;
                                        Log.i("break", "----here");
                                        break;

                                    }
                                }

                                if (tf == 1) { //가족 코드 모음집(groups)에 있는 코드와 동일함 그래서 자기database에 fcode추가하고 화면전환
                                    Log.i("family acitivity", "tf=1");
                                    firebaseAuth = FirebaseAuth.getInstance();
                                    FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인한 사람이 user
                                    mDatabase.getReference("users").child(user.getUid()).child("fcode").setValue(str); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기
                                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("family");
                                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            count = dataSnapshot.child(str).child("count").getValue(String.class);
                                            if(count == null)
                                                FirebaseDatabase.getInstance().getReference("family").child(str).child("count").setValue("");
                                                FirebaseDatabase.getInstance().getReference("family").child(str).child("family_name").setValue("");

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            throw databaseError.toException();
                                        }
                                    });

                                    Intent intent = new Intent(getApplicationContext(), MakeProfile.class); //바로 프로필 만들러 ㄱㄱ
                                    startActivity(intent);
                                    finish();
                                    //초대 코드 중복 체크 + 존재하는 것만 담을 수 있게 하고

                                } else if(tf==0){//잘못된 코드 입력 시 토스트 띄우기 !
                                    Log.i("family acitivity", "tf=0");
                                    Toast.makeText(familyactivity.this, "가족 코드가 틀렸습니다. 다시 시도해주세요 !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("family activity", "groups 안에 하위 노드를 읽지 못하였음");
                            }
                        });
                    }
                        break;

                    case R.id.btn_makecode:
                        {
                        //btn_makecode 눌렀을 때의 처리
                        Intent intent = new Intent(familyactivity.this, CodeActivity.class); //코드 생성 xml로 이동
                        startActivity(intent);
                        finish();
                        }
                        break;


                }
            }
        };
        btn_makecode.setOnClickListener(onClickListener);
        btn_ok.setOnClickListener(onClickListener);
    }
}
