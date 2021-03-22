package com.example.persimmon_tree_proj.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Answeractivity extends AppCompatActivity {

    private EditText edit_answer;
    private TextView textView;
    InputMethodManager imm;
    public String msg; //edit_answer의 텍스트 값을 받아 msg에 저장
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseDatabase mDatabase;
    private FirebaseAuth firebaseAuth;
    private String user_name;
    private String f_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answeractivity);

        Intent intent = getIntent();//mainactivity에서 받아온 intent 선언
        String question = intent.getStringExtra("question");//mainactivity에서 받아온 question
        //final String f_code = {intent.getStringExtra("f_code")}; //mainacitivity에서 받아온 f_code 순서가 바뀌어야 하니까 intent꼬일까봐 users에서 받아오는 걸로 변경
        final String position = intent.getStringExtra("position");
        ArrayList<Object> our_q_arr = (ArrayList<Object>) intent.getSerializableExtra("our_q_arr");
        textView =(TextView)findViewById(R.id.txt_question2);
        textView.setText(question); //textView에 question 띄우기
        edit_answer = (EditText)findViewById(R.id.edit_answer);

        //화면 터치하면 키보드 내려가도록 만들었써용~
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 user 확인
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
        mDatabase = FirebaseDatabase.getInstance();
        Button answer = (Button)findViewById(R.id.btn_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = edit_answer.getText().toString();//edit_answer에 작성한 text msg에 저장
                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        f_code = String.valueOf(snapshot.child("fcode"));
                        String user_name = snapshot.child("userName").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("family").child(f_code).child("answer").child(position).child(user_name).setValue(msg);
                        Intent intent = new Intent(Answeractivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        //내가 답 X
        DatabaseReference familyreference = mDatabase.getReference("family");
        familyreference.child(f_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("answer").child(String.valueOf(our_q_arr.size())).hasChild(user_name)){

                }
                //내가 답 O
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    //화면 터치 시 키보드 내려가게
    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(edit_answer.getWindowToken(), 0);
    }

    //뒤로가기 버튼 누르면 Mainactivity로 이동
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}