package com.example.persimmon_tree_proj.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.LocusId;
import android.os.Bundle;
import android.util.Log;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.Make_FamilyProfile;
import com.example.persimmon_tree_proj.Family.Waitactivity;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Profile.MakeProfile;
import com.example.persimmon_tree_proj.Profile.MakeProfilemain;
import com.example.persimmon_tree_proj.Profile.profile_color;
import com.example.persimmon_tree_proj.Profile.profile_gam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LodingPage_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String introduce;
    private String user_name;
    private String user_color;
    private String user_gam;
    private String user_fcode;

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
         *                          case 6-1 : 가족을 기다리는 중임 => waitactivity로 이동
         *                          case 6-2 : mainactivity에 이미 들어와있음 => mainacitivy로 이동
         *
         *
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
                Log.i("user_name", String.valueOf(user_name));
                Log.i("userid", String.valueOf(user.getUid()));
                Log.i("usergam", String.valueOf(user_gam));
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
                                    //if()
                                    Intent intent = new Intent(LodingPage_Activity.this, Waitactivity.class);  // 대기 화면을 설정하러 가라
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    //else()
                                    //Intent intent = new Intent(LodingPage_Activity.this, MainActivity.class);  // 감 캐릭터를 설정하러 가라
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //startActivity(intent);
                                    //finish();
                                }

                            }
                        }
                    }

                }




            }

            @Override public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}