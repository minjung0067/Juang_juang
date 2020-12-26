package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_gam extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gam);
        Button color = (Button) findViewById(R.id.color);   //색상 버튼
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_gam.this, profile_color.class);
                startActivity(intent);
            }
        });
        ImageButton go_back = (ImageButton) findViewById(R.id.go_back);  //뒤로가기 버튼
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_gam.this, MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void Click(final View view) {  //버튼 클릭시마다 switch문으로 다른 감 프로필 선택
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                String user_name = dataSnapshot.child("name").getValue(String.class);
                switch (view.getId()) {
                    case R.id.gam1:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("1");
                        break;
                    case R.id.gam2:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("2");
                        break;
                    case R.id.gam3:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("3");
                        break;
                    case R.id.gam4:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("4");
                        break;
                    case R.id.gam5:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("5");
                        break;
                    case R.id.gam6:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("6");
                        break;
                    case R.id.gam7:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("7");
                        break;
                    case R.id.gam8:
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("8");
                        break;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}
