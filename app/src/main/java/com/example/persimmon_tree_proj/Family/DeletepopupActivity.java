package com.example.persimmon_tree_proj.Family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeletepopupActivity extends AppCompatActivity {

    private String f_code;
    private String f_count;
    private ArrayList<String> user_list;
    private int i; //user_list index
    private int count; //user_list size

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseDatabase aDatabase;
    private DatabaseReference aReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_deletepopup);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code");
        f_count = intent.getStringExtra("f_count");
        count = Integer.valueOf(f_count);

        DatabaseReference reference2  = FirebaseDatabase.getInstance().getReference("groups");
        reference2.child(f_code).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list = new ArrayList<>();
                user_list.clear();

                for(DataSnapshot membersData : dataSnapshot.getChildren()){
                    String user = membersData.getKey();
                    user_list.add(user);

                }


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //확인 누르면 파이어베이스에 가족 코드가 삭제됨.
        Button btn_ok = (Button)findViewById(R.id.btn_okay);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //가족 코드 삭제하기를 누르면, 가족 int move = 0; //파이어베이스에 저장되면 이동하도록 함.
                int move = 0;

                delete();

                move = 1;

                if (move == 1){
                    Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
            }
        });

        Button btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //감나무 시작하기를 누르면, 가족 int move = 0; //파이어베이스에 저장되면 이동하도록 함.
                Intent intent = new Intent(getApplicationContext(), Waitactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


    }

    private void delete(){

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");

        for (i = 0 ; i < count ; i++){
            mReference.child(user_list.get(i)).child("fcode").removeValue();
            mReference.child(user_list.get(i)).child("captain").removeValue();
        }

        aDatabase = FirebaseDatabase.getInstance();
        aReference = aDatabase.getReference("groups");
        for(i = 0 ; i < count ; i++){
            aReference.child(f_code).child("members").child(user_list.get(i)).removeValue();
        }
        aReference.child(f_code).child("f_name").removeValue();






    }
}