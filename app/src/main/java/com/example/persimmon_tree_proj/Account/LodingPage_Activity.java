package com.example.persimmon_tree_proj.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.LocusId;
import android.os.Bundle;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.Make_FamilyProfile;
import com.example.persimmon_tree_proj.Family.Waitactivity;
import com.example.persimmon_tree_proj.Family.familyactivity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Profile.MakeProfile;
import com.example.persimmon_tree_proj.Profile.MakeProfilemain;
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
    private String check_code;
    private String introduce;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding_page_);

        //로그인을 거친 사용자가 들어오게 되는 로딩페이지 겸 사용자 분류 용 페이지

        /*
         * 이 페이지에서 확인할 것
         * - 가족코드가 있는 사람인가?
         *
         * case 1 . db에 올라간, 나와 연결된 가족 코드 없음 => familyactivity로 가서
         *                              우리 가족 코드 입력 or 가족코드 만들기
         *                       => 두 가지 경우에서 일을 다 수행하고 db에 올라가면
         *                          이 로딩 페이지에 다시 들어오고, 그때는 case 2로 감
         *
         * case 2 . 내 db에 올라간 가족 코드 있음
         *       case 2-1. 가족 코드를 입력했는데 내 프로필을 아직 안 만든 사람 => makeprofile
         *       case 2-1-2.  소셜 로그인 -> 가족코드 입력-> 이름이나 생년월일 같은 기본 정보 아직 db에 없음 => more information
         *
         *       case 2-2. 메인으로 들어갈 준비 완료 (내 프로필 만듦, 이미 메인 넘어간 사람)
         *                   => main
         *  */


        // - 가족 코드가 있는 사람인가?

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
        check_code = reference.child(user.getUid()).getKey(); //가족 코드를 생성했다면 그 사람의 하위 가지에 fcode라는 키가 있을 것!

        //case 1 : 자신과 연결된 가족 코드가 없음 => familyactivity로 이동
        if (check_code == null) {
            Intent intent = new Intent(LodingPage_Activity.this, familyactivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        //case 2 : 자신과 연결된 가족 코드 있음
        else {
            reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    introduce = snapshot.child("introduce").getValue(String.class); //프로필 만들기 마지막 단계가 되었는지 확인
                    user_name = snapshot.child("user_name").getValue(String.class); //프로필 만들기 마지막 단계가 되었는지 확인

                    // 내 프로필 끝까지 다 만들었는지 확인

                    // case 2-1-2. 이름이나 생년월일 같은 기본 정보 아직 db에 없음 (소셜만 해당) => more information
                    if (user_name == null) {
                        Intent intent = new Intent(LodingPage_Activity.this, more_information_activity.class);  //프로필 만들러 가라
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        //case 2-1 : 가족 코드는 있지만 내 프로필 아직 안 만든 사람 (이메일, 소셜 둘 다 해당) => makeprofilemain
                        if (introduce == null) {
                            Intent intent = new Intent(LodingPage_Activity.this, MakeProfilemain.class);  //프로필 만들러 가라
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }


                        // case 2-2 . 프로필까지 다 만들었다 ! => wait
                        else{
                            Intent intent = new Intent(LodingPage_Activity.this, Waitactivity.class);  //대기화면으로 가라 !
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }
    }
}