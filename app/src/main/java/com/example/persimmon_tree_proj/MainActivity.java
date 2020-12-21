package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button btn_logout;     //로그아웃 버튼
    private Button btn_goanswer; //답변하러가기 버튼
    private Button btn_mypage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference answerReference;
    private ChildEventListener mChild;
    private TextView textView; //질문 나오는 textView
    private TextView answerView; //질문에 대한 답변이 나오는 textView
    private String question;
    private String question_position;
    private Spinner spinner; //질문 목록이 있는 spinner
    //array배열을 생성하고 spinner와 연결
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        spinner =(Spinner)findViewById(R.id.spinner_question); //spinner_question
        answerView = (TextView)findViewById(R.id.txt_answer); //answer을 나타내는 textView

        initDatabase();

        answerReference = mDatabase.getReference("")
        mReference = mDatabase.getReference("question");
        //ValueEventListener : 경로의 전체 내용에 대한 변경을 읽고 수신 대기
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                adapter.clear(); //spinner에 넣을 값을 넣기 전 초기화하기

                //child 내에 있는 question 데이터를 저장하는 작업
                for(DataSnapshot questionData : datasnapshot.getChildren()){
                    String question = questionData.getValue().toString();
                    Array.add(question);
                    adapter.add(question);
                }
                //spinner를 갱신하고 마지막 위치를 카운트
                adapter.notifyDataSetChanged();
                spinner.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //spinner 선택했을 때
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(" "+parent.getItemAtPosition(position)); //mainactivity에서 textview에 question을 띄어줌.
                question = textView.getText().toString();                 //quesition이라는 변수에 문자열로 저장
                question_position = String.valueOf(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //spinner를 연결하고, Arrayadapter와 spinner 연결
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        spinner.setAdapter(adapter);

        Button logout = (Button) findViewById(R.id.btn_logout); //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPregerences값을 불러온다.
                Intent intent = new Intent(MainActivity.this, log_inactivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //edit.clear()는 파일 auto에 들어있는 모든 정보를 기기에서 지운다.
                editor.clear();
                editor.commit(); //저장
                Toast.makeText(MainActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button goanswer = (Button) findViewById(R.id.btn_goanswer); //답변하러가기 버튼
        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Answeactivity로 이동
                Intent intent = new Intent(MainActivity.this, Answeractivity.class);
                intent.putExtra("question",question); //선택한 question을 갖고 감.
                intent.putExtra("position",question_position); //선택한 position값을 갖고 감.
                startActivity(intent);
            }
        });

        Button mypage = (Button) findViewById(R.id.btn_mypage); //마이페이지 버튼
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 마이페이지로 이동
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDatabase(){
        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("question");

        mChild = new ChildEventListener(){


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    protected void onDestroy(){
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}


