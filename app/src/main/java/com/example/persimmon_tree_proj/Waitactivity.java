package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;


public class Waitactivity extends AppCompatActivity {


    private String f_code;
    private String user_name;
    private int member_count;
    private int count;



    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private FirebaseDatabase a_Database;
    private DatabaseReference a_Reference;
    private ChildEventListener a_Child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitactivity);

        final TextView textchange = (TextView)findViewById(R.id.textView6);
        //복사가 되었다면 토스트메시지 노출

        Button send = (Button) findViewById(R.id.btn_copy);
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("family code",f_code); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                //복사가 되었다면 토스트메시지 노출
                Toast.makeText(Waitactivity.this, "가족 코드가 복사되었습니다. 가족들에게 공유해주세요 !", Toast.LENGTH_SHORT).show();

                //텍스트 변환 효과
                textchange.setText("모두 가입해야\n감나무가 열려요!");

            }
        });


        Button change = (Button) findViewById(R.id.btn_change);//가족 구성원 수 바꾸기
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(Waitactivity.this, Make_FamilyProfilewait.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentt);

            }
        });

        Button logout = (Button) findViewById(R.id.btn_logout2); //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPregerences값을 불러온다.
                Intent intent = new Intent(getApplicationContext(), log_inactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Intent intent = new Intent(MypageActivity.this, log_inactivity.class);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장
                Toast.makeText(Waitactivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                //startActivity(intent);
                finish();
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = snapshot.child("fcode").getValue().toString();
                TextView txt_fcode = (TextView) findViewById(R.id.txt_fcode);
                txt_fcode.setText(f_code);
                user_name = snapshot.child("userName").getValue().toString();
                member_count = 0;
                //지정한 member 수 가져오기
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");
                reference1.child(f_code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //count 수 가져오기
                        String str = (String) snapshot.child("count").getValue();
                        count = Integer.valueOf(str);
                        //가져온 f_code에 해당하는 member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                        while (members.hasNext()){
                            String member_num = members.next().getKey();
                            member_count++;
                        }


                        //가족 수 확인하여서 가족 만들어졌는지 확인 member_count 와 count 비교


                        //가족 감나무가 만들어졌을 경우
                        if(member_count == count){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        }
                        //감나무가 생성되지 않은 경우
                        else if(member_count < count){


                        }

                        else{//member_count > count

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}