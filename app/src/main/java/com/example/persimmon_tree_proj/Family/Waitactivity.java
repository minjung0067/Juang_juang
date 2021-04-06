package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.log_inactivity;
import com.example.persimmon_tree_proj.Calendar.PopupCalendar;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class Waitactivity extends AppCompatActivity {


    private String f_code;
    private int member_count; //현재 들어와있는 가족 구성원 수 count

    private ArrayList<String> all_user_arr; //user를 담는 배열
    private ListView userList;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitactivity);

        final TextView textchange = (TextView)findViewById(R.id.txt_notice);
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


        Button change = (Button) findViewById(R.id.btn_change);//가족 코드 없애버리기
        //경고창 구현하면 어떨까
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(Waitactivity.this, familyactivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentt);

            }
        });
        


        Button logout = (Button) findViewById(R.id.btn_logout2); //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해 SharedPregerences값을 불러온다.
                Intent intent = new Intent(getApplicationContext(), log_inactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장
                Toast.makeText(Waitactivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button start = (Button) findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //경고 메세지 주었으면 좋겠음.
                String fcount = String.valueOf(member_count);

                Intent intent = new Intent(Waitactivity.this, StartpopupActivity.class);
                intent.putExtra("fcount",fcount);
                intent.putExtra("f_code",f_code);
                startActivityForResult(intent, 1);
                SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
                formatH = new SimpleDateFormat("yyyyMMdd"); //formatH에 현재 시간 넣어줌 대소문자 중요함
                Date today = new Date(); //today 변수에 Date 부르기
                String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326
                FirebaseDatabase.getInstance().getReference("answer").child(f_code).child("1").child("Date").setValue(strDate); //question번호와 날짜 올리기

            }
        });


        userList = (ListView)findViewById(R.id.list_user);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        userList.setAdapter(adapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = String.valueOf(snapshot.child("fcode").getValue());
                TextView txt_fcode = (TextView) findViewById(R.id.txt_fcode);
                txt_fcode.setText(f_code);


                DatabaseReference reference2  = FirebaseDatabase.getInstance().getReference("groups");
                reference2.child(f_code).child("members").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adapter.clear();

                        for(DataSnapshot membersData : dataSnapshot.getChildren()){
                            String user = membersData.getValue().toString();
                            Array.add(user);
                            adapter.add(user);
                        }
                        adapter.notifyDataSetChanged(); //리스트뷰 갱신
                        userList.setSelection(adapter.getCount() -1); //마지막 위치를 카운트해서 보내줌.
                        member_count = adapter.getCount();

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //전체 user 가져오기



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







}