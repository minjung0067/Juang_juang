package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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

public class profile_color_cal extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private ImageView c_1;
    private ImageView c_2;
    private ImageView c_3;
    private ImageView c_4;
    private ImageView c_5;
    private ImageView c_6;
    private ImageView c_7;
    private ImageView c_8;
    //private ImageView c_9;
    private int[] clicked_arr = {0,0,0,0,0,0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_color);

        c_1 = (ImageView) findViewById(R.id.c1);
        c_2 = (ImageView) findViewById(R.id.c2);
        c_3 = (ImageView) findViewById(R.id.c3);
        c_4 = (ImageView) findViewById(R.id.c4);
        c_5 = (ImageView) findViewById(R.id.c5);
        c_6 = (ImageView) findViewById(R.id.c6);
        c_7 = (ImageView) findViewById(R.id.c7);
        c_8 = (ImageView) findViewById(R.id.c8);
        //c_9 = (ImageView) findViewById(R.id.c9);


        Button gam = (Button) findViewById(R.id.gam);     //감 프로필 사진 고르기 버튼

        gam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile_gam_cal.class); //코드 생성 activity로 이동
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        ImageButton go_back = (ImageButton) findViewById(R.id.go_back);    //뒤로가기
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeProfilemain.class); //makeprofil로 이동
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                final String user_name = dataSnapshot.child("name").getValue(String.class);
                //이미 다른 가족 구성원이 선택한 색깔이 뭔지 검사하는 부분 시작
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");
                reference1.child(myfcode).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> members = dataSnapshot.getChildren().iterator();
                        //원래 사용자가 선택했던 건 다시 선택지 안에 들어가야 하니까
                        String previous_choice = dataSnapshot.child(user_name).child("user_color").getValue(String.class);
                        if(previous_choice != null){
                            make_cannot_select(previous_choice,1);
                        }
                        make_clicked();
                        while(members.hasNext()) {
                            String family_color_num = members.next().child("user_color").getValue(String.class);
                            if (family_color_num != null && family_color_num!= previous_choice) {
                                //family_color_num이 있긴 하면서 이전의 선택과 같지 않은 거 == 나를 제외, 가족들이 선택한 거
                                make_cannot_select(family_color_num,2);
                                //각각 체크해서 다른 가족에 의해 선택된 건 2로 바꾸는 함수
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MakeProfil_cal.class); //코드 생성 activity로 이동
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        //안드로이드 백버튼 막기
        return;
    }

    public void check_process(int clicked_what, ImageView clicked_btn){
        //0과 1은 현재 user가 선택x or 선택 나타내고 2는 다른 사람이 해서 아예 선택 못하는 거
        if (clicked_what==1){
            clicked_btn.setBackgroundResource(R.drawable.btn_clicked_color);
        }
        else if(clicked_what==2){
            clicked_btn.setBackgroundResource(R.drawable.btn_clicked_color);
        }
        else{
            clicked_btn.setBackgroundResource(R.drawable.btn_not_clicked);
        }
    }

    public void make_clicked() {   //선택한 걔빼고는 다 선택해제 되게, 이미 다른 가족이 선택한 애들은 선택 안되게 하는 함수!
        check_process(clicked_arr[0], c_1);
        check_process(clicked_arr[1], c_2);
        check_process(clicked_arr[2], c_3);
        check_process(clicked_arr[3], c_4);
        check_process(clicked_arr[4], c_5);
        check_process(clicked_arr[5], c_6);
        check_process(clicked_arr[6], c_7);
        check_process(clicked_arr[7], c_8);
        //check_process(clicked_arr[8], c_9);
    }
    //사용자가 전에 선택했던 건 1로 가족들이 이미 선택한 색깔 체크해서 이미 선택된 건 2로 바꾸는 함수
    public void make_cannot_select(String family_color_num,int num) {
        if (family_color_num.equals("#FE8189")) {
            clicked_arr[0] = num;
        } else if (family_color_num.equals("#FE8E69")) {
            clicked_arr[1] = num;
        } else if (family_color_num.equals("#FEC56C")) {
            clicked_arr[2] = num;
        } else if (family_color_num.equals("#B7DB79")) {
            clicked_arr[3] = num;
        } else if (family_color_num.equals("#87dade")) {
            clicked_arr[4] = num;
        } else if (family_color_num.equals("#A1AEE5")) {
            clicked_arr[5] = num;
        } else if (family_color_num.equals("#99CAEB")) {
            clicked_arr[6] = num;
        } else if (family_color_num.equals("#E89CDA")) {
            clicked_arr[7] = num; }
//        else if (family_color_num.equals("#527B03F4")) {
//            clicked_arr[8] = num; }
        else{
        }
        make_clicked();
        //이미 선택된 건 색깔 어둡게 바꿔주세용~ 하는 함수
    }
    //이미 다른 가족 구성원이 선택한 색깔이 뭔지 검사하는 부분 끝

    //사용자가 어떤 색깔 선택하면 그 전에 선택했던 거 취소 시키는 함수
    public void another_unselected(int clicked_index){
        for(int i=0; i<8;i++){
            if(i!=clicked_index){   //지금 선택한 거 말고
                if(clicked_arr[i]==1){  //사용자가 한번 선택했다가 다른 거 또 선택해서 1로 남아있는 거
                    clicked_arr[i] =0;
                }
            }
        }
    }

    public void Click(final View view) {  //버튼 클릭시마다 switch문으로 다른 색깔 선택
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                String user_name = dataSnapshot.child("name").getValue(String.class);
                //현재 user가 선택한 색깔 확인
                switch (view.getId()){
                    case R.id.c1:
                        //다른 가족이 선택 안했으면서 선택하면 0->1,선택했다가 취소하면 1->0,다른 가족 선택했으면 선택 못하게 하는 함수
                        if(clicked_arr[0] == 1){ clicked_arr[0] = 0; } else if(clicked_arr[0] ==0){
                            clicked_arr[0]=1;
                            another_unselected(0);   //그 전에 선택했던 거 취소 시키는 함수
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#FE8189");}
                        make_clicked();
                        break;
                    case R.id.c2:
                        if(clicked_arr[1] == 1){ clicked_arr[1] = 0; } else if(clicked_arr[1] ==0){
                            clicked_arr[1]=1;
                            another_unselected(1);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#FE8E69");
                        }
                        make_clicked();
                        break;
                    case R.id.c3:
                        if(clicked_arr[2] == 1){ clicked_arr[2] = 0; } else if(clicked_arr[2] ==0){
                            clicked_arr[2]=1;
                            another_unselected(2);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#FEC56C");
                        }
                        make_clicked();
                        break;
                    case R.id.c4:
                        if(clicked_arr[3] == 1){ clicked_arr[3] = 0; } else if(clicked_arr[3] ==0){
                            clicked_arr[3]=1;
                            another_unselected(3);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#B7DB79");
                        }
                        make_clicked();
                        break;
                    case R.id.c5:
                        if(clicked_arr[4] == 1){ clicked_arr[4] = 0; } else if(clicked_arr[4] ==0){
                            clicked_arr[4]=1;
                            another_unselected(4);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#87dade");
                        }
                        make_clicked();
                        break;
                    case R.id.c6:
                        if(clicked_arr[5] == 1){ clicked_arr[5] = 0; } else if(clicked_arr[5] ==0){
                            clicked_arr[5]=1;
                            another_unselected(5);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#A1AEE5");
                        }
                        make_clicked();
                        break;
                    case R.id.c7:
                        if(clicked_arr[6] == 1){ clicked_arr[6] = 0; } else if(clicked_arr[6] ==0){
                            clicked_arr[6]=1;
                            another_unselected(6);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#99CAEB");
                        }
                        make_clicked();
                        break;
                    case R.id.c8:
                        if(clicked_arr[7] == 1){ clicked_arr[7] = 0; } else if(clicked_arr[7] ==0){
                            clicked_arr[7]=1;
                            another_unselected(7);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#E89CDA");
                        }
                        make_clicked();
                        break;
//                    case R.id.c9:
//                        if(clicked_arr[8] == 1){ clicked_arr[8] = 0; } else if(clicked_arr[8] ==0){
//                            clicked_arr[8]=1;
//                            another_unselected(8);
//                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#527B03F4");
//                        }
//                        make_clicked();
//                        break;

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }
}
