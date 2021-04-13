package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.more_information_activity;
import com.example.persimmon_tree_proj.Family.Waitactivity;
import com.example.persimmon_tree_proj.Family.Waitactivity2;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Mypage.MakeProfile;
import com.example.persimmon_tree_proj.Mypage.profile_color;
import com.example.persimmon_tree_proj.Mypage.profile_gam;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LodingPage_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String introduce;
    private String user_name;
    private String user_color;
    private String user_gam;
    private String user_fcode;
    private String fcount;
    private String family_name;
    private String captain;
    private Random rnd;
    private String[] loding_message={"자신감 있는 \n표정을 지으면 \n자신감이 생긴다 ",
            "세상에는 \n세 가지의 \n감춰질 수 없는 것이 있다. \n해와 달, \n그리고 \n진실이다." ,
            "우정은 \n기쁨을 두 배로 하고\n\n슬픔을 반감시킨다." ,
            "유감없이 \n보낸 하루는 \n즐거운 잠을 가져온다." ,
            "승리는 \n자신감을 가진 사람의 편이다. " ,
            "기억을 증진하는 \n가장 좋은 약은 \n감탄하는 것이다." ,
            "운명은 \n용감한 자를 사랑한다." ,
            "용감한 사람은 \n자기 운명을 창조해 간다."};
    private String[] person={"- 찰스 다윈",
            "- 석가모니" ,
            "- 프리드리히 실러" ,
            "- 레오나르도 다 빈치" ,
            "- 가토 마사오" ,
            "- 탈무드" ,
            "- 베르길리우스" ,
            "- 미겔 데 세르반테스"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding_page_);

        //로그인을 거친 사용자가 들어오게 되는 로딩페이지 겸 사용자 분류 용 페이지



        /*
         * 이 페이지에서 확인할 것
         * 개인 프로필 설정(이름, 생년월일) =) 감 색깔 설정 => 감 캐릭터 설정 => 별명 => 가족 코드 (가족 프로필까지 작성해야 코드가 올라간다.) => waitactivity
         * 개인 프로필 : 이름 , 생년월일
         * case 1-1: 개인 프로필을 설정하지 않았음.
         *          => more_information_activity로 이동
         * case 1-2: 개인 프로필을 설정하였음.
         *      case 2-1 : 감 캐릭터를 설정하지 않았음. => profile_gam activity로 이동
         *
         *      case 2-2 : 감 캐릭터를 설정하였음.
         *          case 3-1 : 감 색깔을 설정하지 않았음 => profile_color activity로 이동
         *          case 3-2 : 감 색깔을 설정하였음
         *                  case 4-1 : 별명을 설정하지 않았음 => makeprofile로 이동
         *                  case 4-2 : 별명을 설정하였음.
         *                      case 5-1 : 나와 연결된 가족 코드가 없음 => familyactivity로 이동
         *                      case 5-2 : 나와 연결된 가족 코드가 있음
         *                          case 6-2: 가족을 기다리는 중임
         *                              case 6-2-1 : captain == true 일 경우에 waitactivity로 이동
         *                              case 6-2-2 : captain == false 일 경우에 waitactivity2로 이동
         *                          case 6-1 : mainactivity에 이미 들어와있음 => mainacitivy로 이동
         *
         *
         * // 로딩 메세지 출력하는 부분
         *
         *  */


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name = String.valueOf(snapshot.child(user.getUid()).child("user_name").getValue()); //개인 정보를 작성했는지 확인
                user_color = String.valueOf(snapshot.child(user.getUid()).child("user_color").getValue()); //감 색깔을 설정했는지 확인
                user_gam = String.valueOf(snapshot.child(user.getUid()).child("user_gam").getValue()); // 감 캐릭터를 설정했는지 확인
                introduce = String.valueOf(snapshot.child(user.getUid()).child("introduce").getValue()); //프로필 만들기 마지막 단계가 되었는지 확인
                user_fcode = String.valueOf(snapshot.child(user.getUid()).child("fcode").getValue());
                captain = String.valueOf(snapshot.child(user.getUid()).child("captain").getValue()); //방장 확인
                firebaseAuth = FirebaseAuth.getInstance();

                //case 1-1: 이름이나 생년월일 같은 기본 정보 아직 db에 없음 => more_information_activity로 이동
                if(user_name == "null"){
                    Intent intent = new Intent(LodingPage_Activity.this, more_information_activity.class);  //프로필 만들러 가라
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                //case 1-2 : 이름이나 생년월일 같은 기본 정보 db에 있음
                else{
                    //case 2-1 감 캐릭터를 설정하지 않았을 경우 => profile_gam로 이동
                    if(user_gam == "null"){
                        Intent intent = new Intent(LodingPage_Activity.this, profile_gam.class);  // 감 캐릭터를 설정하러 가라
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    // case 2-2 :감 캐릭터를 설정한 경우
                    else{
                        // case 3-1 : 감 색깔을 설정하지 않은 경우 => profile_color로 이동
                        if(user_color == "null"){
                            Intent intent = new Intent(LodingPage_Activity.this, profile_color.class);  // 감 색깔을 설정하러 가라
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        //case3-2 : 감 색깔을 설정한 경우
                        else{
                            //case 4-1 : 별명을 설정하지 않은 경우 => MakeProfile로 이동
                            if(introduce == "null"){
                                Intent intent = new Intent(LodingPage_Activity.this, MakeProfile.class);  // 프로필(별명)을 설정하러 가라
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            //case 4-2 : 별명을 설정한 경우
                            else{
                                //case 5-1 : 지정된 가족이 없는 경우 => familyactivity로 이동
                                if(user_fcode == "null"){
                                    Intent intent = new Intent(LodingPage_Activity.this,familyactivity.class);  // 가족코드를 설정하러 가라
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                //case 5-2
                                else{
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groups");  //users에서 현 uid 가진 사람 찾기
                                    reference.child(user_fcode).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            fcount = String.valueOf(snapshot.child("count").getValue());
                                            if(fcount == "null") {
                                                if(captain.equals("true")){
                                                    Intent intent = new Intent(LodingPage_Activity.this, Waitactivity.class);  // 대기 화면을 설정하러 가라
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else if(captain.equals("false")){
                                                    Intent intent = new Intent(LodingPage_Activity.this, Waitactivity2.class);  // 대기 화면을 설정하러 가라
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                            else{
                                                family_name = String.valueOf(snapshot.child("f_name").getValue());
                                                new Timer().schedule(new TimerTask() {
                                                    public void run() {
                                                        Intent intent = new Intent(LodingPage_Activity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("user_fcode",user_fcode);
                                                        intent.putExtra("user_color",user_color);
                                                        intent.putExtra("user_gam",user_gam);
                                                        intent.putExtra("user_name",user_name);
                                                        intent.putExtra("family_name",family_name);
                                                        intent.putExtra("introduce",introduce);
                                                        intent.putExtra("count",fcount);
                                                        startActivity(intent);
                                                        overridePendingTransition(0, 0); //intent시 효과 없애기
                                                        finish();
                                                    }
                                                }, 1500);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                }

                            }
                        }
                    }

                }




            }

            @Override public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //로딩 메세지 출력하는 부분

        TextView sentence = (TextView) findViewById(R.id.sentence);
        TextView name = (TextView) findViewById(R.id.name);
        rnd = new Random(); //랜덤클래스로부터 랜덤 값 받아오는 변수 작성.
        int num = rnd.nextInt(loding_message.length); //랜덤 숫자 생성
        sentence.setText(loding_message[num]); //위에서 담아놓은 문구 중 랜덤하게 가져옴
        name.setText(person[num]);


    }
}