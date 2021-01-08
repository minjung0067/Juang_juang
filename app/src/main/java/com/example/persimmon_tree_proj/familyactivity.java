package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.icu.text.Edits;
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

import java.util.HashMap;
import java.util.Iterator;

public class familyactivity extends AppCompatActivity {
    private Button btn_makecode; //가족코드생성 버튼
    private Button btn_ok; //코드 확인 버튼
    private EditText et_code; //코드 입력 text
    private String str; //입력한 코드 str로 바꿀 string 변수
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private int tf = 2; //가족 코드 맞는지 표시해줄 int형 변수
    private int exist = 0;
    private FirebaseDatabase mDatabase;
    private String count;
    private int member_count;
    private int family_count2;


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
                        if (str!=null && str.length()==6){//가족 코드가 6자리를 착실하게 기입했는데 그게 실제로 있는 코드인지 확인
                        mDatabase = FirebaseDatabase.getInstance();
                        mDatabase.getReference("groups").addValueEventListener(new ValueEventListener() { //groups에서 실제로 그 코드가 있는지 확인함
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //tf = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Log.i("familycheck", String.valueOf(snapshot.getValue()));
                                    if ((snapshot.getValue()).equals(str)) {
                                        exist = 1;//str_code랑 원래 기존에 있던 코드에 있다면 exist = 1 , 같지 않다면, exist = 0
                                        Log.i("familycheck1",str);
                                        Log.i("familycheck1", String.valueOf(exist));
                                        break;

                                    }
                                }
                                if(exist == 0){ //코드가 없는 경우
                                    Log.i("family acitivity", "tf=0");
                                    Toast.makeText(familyactivity.this, "올바르지 않은 코드입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                else if(exist == 1){ //코드가 있는 경우
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");
                                    reference1.child(str).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //count 수 가져오기
                                            int tf = 0; //가족 코드 맞는지 표시해줄 int형 변수
                                            Log.i("test1t", String.valueOf(exist));
                                            Log.i("testt2", String.valueOf(tf));
                                            Log.i("familycheck2","들어왔니?");
                                            String family_count1 = (String)snapshot.child("count").getValue();
                                            Integer family_count2 = Integer.valueOf(family_count1);
                                            //f_code에 해당하는 member수 세기
                                            Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator();
                                            int member_count = 0;
                                            int move = 0;
                                            while(members.hasNext()){
                                                String member_num = members.next().getKey();
                                                member_count++;
                                            }
                                            Log.i("member_count", String.valueOf(member_count));
                                            Log.i("count", String.valueOf(family_count2));
                                            //가입할 수 있는가 없는가를 따짐.
                                            if(member_count >= family_count2){ //이미 가족이 모두 찼을 경우
                                                tf = 0; //가입 할 수 없음
                                                Log.i("family_check", String.valueOf(tf));
                                            }
                                            else if(member_count < family_count2){//member_count < family_count
                                                tf = 1; //가입 할 수 있음.
                                                Log.i("family_check", String.valueOf(tf));

                                            }
                                            if (tf == 1) { //exist = 1이고, 가입할 수 있는 경우 자기database에 fcode추가하고 화면전환
                                                Log.i("family acitivity", "tf=1");
                                                firebaseAuth = FirebaseAuth.getInstance();
                                                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 로그인한 사람이 user\

                                                mDatabase.getReference("users").child(user.getUid()).child("fcode").setValue(str); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                                                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String myfcode = snapshot.child("fcode").getValue(String.class);
                                                        String user_name = snapshot.child("name").getValue().toString();
                                                        //String introduce = snapshot.child("introduce").getValue().toString();
                                                        Log.i("어디있니?","어디가3");
                                                        HashMap user_info = new HashMap<>();  //database 올릴 때 사용 , username이 key값이며, introduce, gam profil, color를 hashmap으로 가짐.
                                                        user_info.put("introduce", "");
                                                        user_info.put("user_gam", "1");
                                                        user_info.put("user_color", "#ffffff");
                                                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).setValue(user_info);
                                                        /*Intent intent = new Intent(familyactivity.this, MakeProfile.class); //개인프로필 만드는 창으로 이동
                                                        startActivity(intent);
                                                        finish();

                                                         */
                                                        //초대 코드 중복 체크 + 존재하는 것만 담을 수 있게 하고

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                move = 1;

                                            }
<<<<<<< HEAD
                                        });
                                        break;
                                    }
                                    else{
                                        tf=0;
                                    }
                                }
=======
                                            else if(tf == 0){ //존재는 하지만, 가족이 다 찼을 경우
                                                Log.i("어디있니?","어디가2");
                                                Log.i("family acitivity", "tf=0");
                                                Toast.makeText(familyactivity.this, "가족인원이 다 찼습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            if(move == 1){
                                                Log.i("어디있니?","어디가1");
                                                Intent intent = new Intent(familyactivity.this, MakeProfile.class); //개인프로필 만드는 창으로 이동
                                                startActivity(intent);
                                                finish();
                                                //초대 코드 중복 체크 + 존재하는 것만 담을 수 있게 하고

>>>>>>> 4e521ad8700bf09010f4660f8cac6652ee2173ec

                                            }



<<<<<<< HEAD
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String myfcode = snapshot.child("fcode").getValue(String.class);
                                            String user_name = snapshot.child("name").getValue().toString();
                                            //String introduce = snapshot.child("introduce").getValue().toString();
                                            HashMap user_info = new HashMap<>();  //database 올릴 때 사용 , username이 key값이며, introduce, gam profil, color를 hashmap으로 가짐.
                                            //user_info.put("introduce", introduce);
                                            user_info.put("user_gam", "1");
                                            user_info.put("user_color", "#ffffff");
                                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).setValue(user_info);
=======
>>>>>>> 4e521ad8700bf09010f4660f8cac6652ee2173ec
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("family activity", "groups 안에 하위 노드를 읽지 못하였음");
                            }
                        });
<<<<<<< HEAD
                        }
                        else{ //가족코드 6자리 or 공백일 때 오류 토스트 띄우기
                            Log.i("family acitivity", "str is null");
                            Toast.makeText(familyactivity.this, "가족 코드 6자리를 입력해주세요 !", Toast.LENGTH_SHORT).show();
                        }
=======




>>>>>>> 4e521ad8700bf09010f4660f8cac6652ee2173ec
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