package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.util.List;

import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity {

    private Button btn_logout;     //로그아웃 버튼
    private Button btn_goanswer; //답변하러가기 버튼
    private Button btn_mypage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private TextView textView; //질문 나오는 textView
    private TextView textViewcode;
    private String question;
    private String question_position;
    private Spinner spinner; //질문 목록이 있는 spinner
    //array배열을 생성하고 spinner와 연결
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    //answer 관련
    private FirebaseDatabase a_Database;
    private DatabaseReference a_Reference;
    private ChildEventListener a_Child;
    private ListView a_View;
    private ArrayAdapter<String> a_adapter;
    List<Object> a_Array = new ArrayList<Object>();
    String pst ="";

    //family code 관련
    private String f_code;
    private int count;
    private int member_count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewcode =(TextView)findViewById(R.id.textViewcode); //fcode확인
        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        spinner =(Spinner)findViewById(R.id.spinner_question); //spinner_question
        a_View = (ListView)findViewById(R.id.answer_view); //answer을 나타내는 textView

        initDatabase();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = snapshot.child("fcode").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        //family-fcode-members가 family-fcode-count 보다 작으면, 블러처리하면서, 아직 가족이 생성되지 않았습니다. 표시
        //family-fcode-members가 family-fcode-count가 같다면, 가족 생성되어서 첫번째 질문을 보여줌
        //family-fcode-count랑 family-fcode-members가 같은데 또, 가족코드가 입력되었다면 알맞지 않은 것으로 표시함.
        //가족 인원수 확인함.

        //family-fcode-answer-(num)이 family-fcode-count보다 작다면, 다음 질문을 보여주지 않는다.
        //family-fcode-answer-(num)이 family-fcode-count와 같아진다면, 다음 질문을 보여준다.
        //질문 확인함.
        a_Reference = a_Database.getReference("family");
        /*a_Reference.child(f_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int)snapshot.child("count").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        member_count = 0;
        a_Reference = a_Database.getReference("family");
        a_Reference.child(f_code).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot countdata : snapshot.getChildren()){
                    countdata.getKey();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */



        //spinner 선택했을 때
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textView.setText(" "+parent.getItemAtPosition(position)+f_code); //mainactivity에서 textview에 question을 띄어줌.
                textView.setText(" "+parent.getItemAtPosition(position));
                question = textView.getText().toString();                 //quesition이라는 변수에 문자열로 저장
                question_position = String.valueOf(position+1);
                Log.i("a 의 값 : ", question_position);


                //listView에 answer 올리기
                a_Reference.child(f_code).child("answer").child(question_position).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        a_adapter.clear(); //ListView에 넣을 값을 넣기전 초기화하기

                        //child 내에 있는 answer데이터를 저장하는 작업
                        for(DataSnapshot answerData : snapshot.getChildren()){
                            String answer = (answerData.getValue()).toString();
                            a_Array.add(answer);
                            a_adapter.add(answer);
                        }
                        //ListView를 갱신하고 마지막 위치를 카운트
                        a_adapter.notifyDataSetChanged();
                        a_View.setSelection(a_adapter.getCount()-1);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        mReference = mDatabase.getReference("question");
        //ValueEventListener : 경로의 전체 내용에 대한 변경을 읽고 수신 대기
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear(); //spinner에 넣을 값을 넣기 전 초기화하기

                //child 내에 있는 question 데이터를 저장하는 작업
                for(DataSnapshot questionData : snapshot.getChildren()){
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

        //spinner를 연결하고, Arrayadapter와 spinner 연결
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        spinner.setAdapter(adapter);

        //listview를 연결하고, Arrayadapter_a와 listview  연결
        a_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        a_View.setAdapter(a_adapter);




        Button goanswer = (Button) findViewById(R.id.btn_goanswer); //답변하러가기 버튼
        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Answeactivity로 이동
                Intent intent = new Intent(MainActivity.this, Answeractivity.class);
                intent.putExtra("question",question); //선택한 question을 갖고 감.
                intent.putExtra("position",question_position); //선택한 position값을 갖고 감.
                intent.putExtra("f_code",f_code);
                startActivity(intent);
            }
        });

        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage); //마이페이지 버튼
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
        a_Database = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("question");
        a_Reference = a_Database.getReference("family");

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

        a_Child = new ChildEventListener() {
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
        a_Reference.addChildEventListener(a_Child);
    }

    protected void onDestroy(){
        super.onDestroy();
        mReference.removeEventListener(mChild);
        a_Reference.removeEventListener(a_Child);
    }
}

