package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.log_inactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.Juang_juang.R.drawable.btn_brightgray_rounded;
import static com.example.Juang_juang.R.drawable.line_dungle;

public class Waitactivity2 extends AppCompatActivity {


    private String f_code;
    private String user_name;
    private int member_count; //현재 들어와있는 가족 구성원 수 count

    private ListView userList;
    private ListViewAdapter userListadapter;
    List<Object> Array = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitactivity2);


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
                Toast.makeText(Waitactivity2.this, "가족 코드가 복사되었습니다. 가족들에게 공유해주세요 !", Toast.LENGTH_SHORT).show();

                //텍스트 변환 효과
                textchange.setText("모두 가입해야\n감나무가 열려요!");

            }
        });

        Button revisecode = (Button)findViewById(R.id.btn_revisecode);
        revisecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fcount = String.valueOf(member_count);
                Intent intent = new Intent(Waitactivity2.this, RevisepopupActivity.class);

                intent.putExtra("f_count",fcount);
                intent.putExtra("f_code",f_code);
                startActivityForResult(intent, 1);

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
                Toast.makeText(Waitactivity2.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        userList = (ListView)findViewById(R.id.list_user);
        userListadapter = new ListViewAdapter(); //Adapter 생성
        userList.setAdapter(userListadapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = String.valueOf(snapshot.child("fcode").getValue());
                TextView txt_fcode = (TextView) findViewById(R.id.txt_fcode);
                txt_fcode.setText(f_code);


                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("groups");
                reference2.child(f_code).child("members").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot membersData : dataSnapshot.getChildren()) {
                            String user = membersData.getValue().toString();
                            Log.i("user",user);
                            Array.add(user);
                            userListadapter.addItem(ContextCompat.getDrawable(getApplicationContext(), btn_brightgray_rounded),ContextCompat.getDrawable(getApplicationContext(),line_dungle),user);
                        }
                        userListadapter.notifyDataSetChanged(); //리스트뷰 갱신
                        userList.setSelection(userListadapter.getCount() - 1); //마지막 위치를 카운트해서 보내줌.
                        member_count = userListadapter.getCount();

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