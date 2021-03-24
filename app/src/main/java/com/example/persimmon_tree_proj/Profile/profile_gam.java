package com.example.persimmon_tree_proj.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.more_information_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class profile_gam extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private ImageView gam_1;
    private ImageView gam_2;
    private ImageView gam_3;
    private ImageView gam_4;
    private ImageView gam_5;
    private ImageView gam_6;
    private ImageView gam_7;
    private ImageView gam_8;
    private int[] clicked_arr = {0, 0, 0, 0, 0, 0, 0, 0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gam);

        gam_1 = (ImageView) findViewById(R.id.gam1);
        gam_2 = (ImageView) findViewById(R.id.gam2);
        gam_3 = (ImageView) findViewById(R.id.gam3);
        gam_4 = (ImageView) findViewById(R.id.gam4);
        gam_5 = (ImageView) findViewById(R.id.gam5);
        gam_6 = (ImageView) findViewById(R.id.gam6);
        gam_7 = (ImageView) findViewById(R.id.gam7);
        gam_8 = (ImageView) findViewById(R.id.gam8);


        ImageButton go_back = (ImageButton) findViewById(R.id.go_back);  //뒤로가기 버튼
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile_color.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }



    public void check_process(int clicked_what, ImageView clicked_btn){
        if (clicked_what == 1) { //1일 떄는 선택하는 것.
            clicked_btn.setBackgroundResource(R.drawable.btn_clicked);
        }
        else { //0일 때는 선택하지 않은 것
            clicked_btn.setBackgroundResource(R.drawable.btn_not_clicked);
        }
    }

    public void make_clicked() {   //선택한 걔빼고는 다 선택해제 되게, 이미 다른 가족이 선택한 애들은 선택 안되게 하는 함수!
        check_process(clicked_arr[0], gam_1);
        check_process(clicked_arr[1], gam_2);
        check_process(clicked_arr[2], gam_3);
        check_process(clicked_arr[3], gam_4);
        check_process(clicked_arr[4], gam_5);
        check_process(clicked_arr[5], gam_6);
        check_process(clicked_arr[6], gam_7);
        check_process(clicked_arr[7], gam_8);
    }



    //사용자가 어떤 색깔 선택하면 그 전에 선택했던 거 취소 시키는 함수
    public void another_unselected ( int clicked_index){
        for (int i = 0; i < 8; i++) {
            if (i != clicked_index) {   //지금 선택한 거 말고
                if (clicked_arr[i] == 1) {  //사용자가 한번 선택했다가 다른 거 또 선택해서 1로 남아있는 거
                    clicked_arr[i] = 0;
                }
            }
        }
    }

    public void Click ( final View view){  //버튼 클릭시마다 switch문으로 다른 감 프로필 선택
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //현재 user가 선택한 감 사진 실시간 확인
                switch (view.getId()) {
                    case R.id.gam1:
                        //다른 가족이 선택 안했으면서 선택하면 0->1,선택했다가 취소하면 1->0,다른 가족 선택했으면 선택 못하게 하는 함수
                        if (clicked_arr[0] == 1) {
                            clicked_arr[0] = 0;
                        } else if (clicked_arr[0] == 0) {
                            clicked_arr[0] = 1;
                            another_unselected(0);   //그 전에 선택했던 거 취소 시키는 함수
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("1");
                        }
                        make_clicked();
                        break;
                    case R.id.gam2:
                        if (clicked_arr[1] == 1) {
                            clicked_arr[1] = 0;
                        } else if (clicked_arr[1] == 0) {
                            clicked_arr[1] = 1;
                            another_unselected(1);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("2");
                        }
                        make_clicked();
                        break;
                    case R.id.gam3:
                        if (clicked_arr[2] == 1) {
                            clicked_arr[2] = 0;
                        } else if (clicked_arr[2] == 0) {
                            clicked_arr[2] = 1;
                            another_unselected(2);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("3");
                        }
                        make_clicked();
                        break;
                    case R.id.gam4:
                        if(clicked_arr[3] == 1){ clicked_arr[3] = 0; } else if(clicked_arr[3] ==0){
                            clicked_arr[3]=1;
                            another_unselected(3);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("4");
                        }
                        make_clicked();
                        break;
                    case R.id.gam5:
                        if(clicked_arr[4] == 1){ clicked_arr[4] = 0; } else if(clicked_arr[4] ==0){
                            clicked_arr[4]=1;
                            another_unselected(4);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("5");
                        }
                        make_clicked();
                        break;
                    case R.id.gam6:
                        if(clicked_arr[5] == 1){ clicked_arr[5] = 0; } else if(clicked_arr[5] ==0){
                            clicked_arr[5]=1;
                            another_unselected(5);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("6");
                        }
                        make_clicked();
                        break;
                    case R.id.gam7:
                        if(clicked_arr[6] == 1){ clicked_arr[6] = 0; } else if(clicked_arr[6] ==0){
                            clicked_arr[6]=1;
                            another_unselected(6);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("7");                        }
                        make_clicked();
                        break;
                    case R.id.gam8:
                        if(clicked_arr[7] == 1){ clicked_arr[7] = 0; } else if(clicked_arr[7] ==0){
                            clicked_arr[7]=1;
                            another_unselected(7);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("user_gam").setValue("8");
                        }
                        make_clicked();
                        break;

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), more_information_activity.class); //코드 생성 activity로 이동
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}