package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity {
    private TextView my_id;
    private TextView my_introduce;
    private TextView my_fcode;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String id;
    private String introduce;
    private String fcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        my_id = (TextView) findViewById(R.id.my_id);
        my_introduce = (TextView) findViewById(R.id.my_introduce);
        my_fcode = (TextView) findViewById(R.id.my_fcode);

        //database에서 값 가져와서 보여주기
        mReference = mDatabase.getReference("user");
        SharedPreferences comefile = getSharedPreferences("saveprofile", MODE_PRIVATE); // 저장된 값을 불러오기 위해 네임파일 saveprofile을 찾음
        final String name = comefile.getString("name", ""); //key에 저장된 값이 있는지 확인 없으면 ""반환
//        mReference.child(name).child("introduce").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                a_adapter.clear(); //ListView에 넣을 값을 넣기전 초기화하기
//                //child 내에 있는 answer데이터를 저장하는 작업
//                for(DataSnapshot answerData : snapshot.getChildren()){
//                    String answer = answerData.getValue().toString();
//                    a_Array.add(answer);
//                    a_adapter.add(answer);
//                }
//                //ListView를 갱신하고 마지막 위치를 카운트
//                a_adapter.notifyDataSetChanged();
//                a_View.setSelection(a_adapter.getCount()-1);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        }

        Button revise = (Button)findViewById(R.id.go_back);
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, MakeProfile.class);
                startActivity(intent);
            }
        });
    }
}
