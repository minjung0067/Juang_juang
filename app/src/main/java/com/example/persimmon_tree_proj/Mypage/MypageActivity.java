package com.example.persimmon_tree_proj.Mypage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.log_inactivity;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.To_do_list.Todolist_Activity;
import com.example.persimmon_tree_proj.customer_sound;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView my_id;
    private TextView my_introduce;
    private TextView my_fcode;
    private ImageView my_image;
    private String gam_number;
    private String color_number;
    private String myfcode;
    private String f_code;
    private String user_name;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        my_introduce = (TextView) findViewById(R.id.my_introduce);
        my_fcode = (TextView) findViewById(R.id.my_fcode);
        my_image = (ImageView) findViewById(R.id.profile_image);

        //현재 로그인한 user uid로 접근해서 현재 유저의 id,fcode,한 줄 소개 가져오기
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code"); //main 에서 받아온 f_code

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myfcode = String.valueOf(dataSnapshot.child("fcode").getValue());
                user_name = String.valueOf(dataSnapshot.child("name").getValue());
                DatabaseReference reference_family = FirebaseDatabase.getInstance().getReference("family");
                reference_family.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        color_number = String.valueOf(dataSnapshot.child(myfcode).child("members").child(user.getUid()).child("user_color").getValue());
                        gam_number = String.valueOf(dataSnapshot.child(myfcode).child("members").child(user.getUid()).child("user_gam").getValue());

                        //나중에 주석 처리해서 지울 부분
                        if (gam_number == null){
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("1");
                        }
                        if (color_number == null){
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#9FFFBB33");
                        }
                        // 나중엔 여기까지 지우기 !

                        //#fff -> int 형으로 바꿔줘야함 Color.parseColor(color_number)
                        // int형을 backgroundtint colorstatelist형으로 전환
                        //ColorStateList tint = ColorStateList.valueOf(Color.parseColor(color_number));

                        switch (gam_number) {
                            case "1":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd1 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd1.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam1);
                                break;
                            case "2":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd2 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd2.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam2);
                                break;
                            case "3":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd3 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd3.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam3);
                                break;
                            case "4":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd4 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd4.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam4);
                                break;
                            case "5":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd5 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd5.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam5);
                                break;
                            case "6":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd6 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd6.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam6);
                                break;
                            case "7":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd7 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd7.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam7);
                                break;
                            case "8":
                                my_image.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd8 = (GradientDrawable) my_image.getBackground(); //동적으로 테두리 색 바꿈
                                gd8.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                my_image.setImageResource(R.drawable.gam8);
                                break;
                            default:
                                my_image.setBackgroundColor(Color.parseColor("#ffffff"));
                                my_image.setImageResource(R.drawable.gam1);
                                break;
                        }

                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }


                });}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });




        final SharedPreferences autologin = getSharedPreferences("auto",AppCompatActivity.MODE_PRIVATE);//users에서 현 uid 가진 사람 찾기
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //fcode 랑 소개랑 id 데이터베이스에서 찾아와서 보여주기
                String myfcode = String.valueOf(dataSnapshot.child("fcode").getValue());
                String myintroduce = String.valueOf(dataSnapshot.child("introduce").getValue());
                my_introduce.setText(myintroduce);
                my_fcode.setText(myfcode);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });




        ImageButton revise = (ImageButton)findViewById(R.id.edit_btn);
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeProfilemain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",myfcode);
                startActivity(intent);
                finish();
//                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        ImageButton goback = (ImageButton)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //왔다감 버튼
        ImageButton go_qna = (ImageButton) findViewById(R.id.qna_btn);
        go_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //공유캘린더 버튼
        ImageButton go_calendar = (ImageButton) findViewById(R.id.calendar_btn);
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(), ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //왔다감 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //뭐할감
        ImageButton go_todo = (ImageButton) findViewById(R.id.to_do_btn);
        go_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Todolist_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //게임
        ImageButton go_game = (ImageButton) findViewById(R.id.game_btn);
        go_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });


        Button logout = (Button) findViewById(R.id.btn_logout); //로그아웃 버튼


        logout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPregerences값을 불러온다.

                Intent intent = new Intent(getApplicationContext(), log_inactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장
                Toast.makeText(MypageActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();

                //구글 로그인 시 로그아웃
                FirebaseAuth.getInstance().signOut();

                //네이버 아이디로 로그인했을 때 로그아웃 용
//                mOAuthLoginModule.logout(mContext);
//                Toast.makeText(MypageActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //뒤로가기 버튼 누르면, 원래 있었던 곳으로 가기
    @Override
    public void onBackPressed() {
        finish();
    }
}
