package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Profile.MakeProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import android.widget.Toast;

public class Make_FamilyProfile extends AppCompatActivity {


    Button ok; //확인버튼

    private EditText counts; //가족 몇명
    private EditText about_familys; //가족 이름
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String myfcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__familyprofile);

        counts = (EditText) findViewById(R.id.count);
        about_familys = (EditText) findViewById(R.id.about_family);


        Intent intent = getIntent();//mainactivity에서 받아온 intent 선언
        myfcode = intent.getStringExtra("f_code");//mainactivity에서 받아온 question


        //확인 버튼 누르면 makeprofile으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer fcount2 = Integer.valueOf(counts.getText().toString());
                if((fcount2< 1) || (fcount2 > 8)){
                    Toast.makeText(Make_FamilyProfile.this, "가족 인원 수는 최소 1명, 최대 8명까지 가능합니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                    reference.child(user.getUid()).child("fcode").setValue(myfcode);
                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String user_name = snapshot.child("name").getValue().toString();
                            HashMap user_info = new HashMap<>();  //database 올릴 때 사용 , username이 key값이며, introduce, gam profil, color를 hashmap으로 가짐.
                            user_info.put("introduce", "");
                            user_info.put("user_gam", "1");
                            user_info.put("user_color", "#ffffff");
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user.getUid()).setValue(user_info);
                            String fcount = counts.getText().toString();
                            String about_myfamily = about_familys.getText().toString();
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("count").setValue(fcount);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("family_name").setValue(about_myfamily);
                            Intent intent = new Intent(getApplicationContext(), MakeProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }
        });
    }


}



