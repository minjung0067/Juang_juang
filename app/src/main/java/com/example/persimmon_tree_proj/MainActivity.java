package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity {

    private Button btn_goanswer; //답변하러가기 버튼
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private ChildEventListener c_Child;
    private TextView textView; //질문 나오는 textView
    private TextView textViewcode;
    private String question;
    private String question_position;
    //array배열을 생성하고 spinner와 연결

    //answer 관련
    private FirebaseDatabase a_Database;
    private FirebaseDatabase c_Database;
    private DatabaseReference a_Reference;
    private DatabaseReference c_Reference;
    private DatabaseReference color_Reference;
    private ChildEventListener a_Child;
    private ListView a_View;
    private LinearLayout container;
    private ArrayAdapter<String> a_adapter;
    private Spinner spinner; //질문 목록이 있는 spinner
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter2;
    private ArrayList<String> all_q_arr; //만들어 놓은 모든 질문 담기는 배열
    private ArrayList<String> our_q_arr; //우리 가족의 질문이 담기는 배열
    String pst ="";
    private ArrayList<String> member_arr = new ArrayList<String>();
    private ArrayList<String> member_ans_arr =  new ArrayList<String>();
    ArrayList<String> member_color_arr =  new ArrayList<String>();
    ArrayList<String> member_gam_arr =  new ArrayList<String>();

    //family code 관련
    private String f_code;
    private String users_color;
    static int count;
    static int member_count;
    static int answer_position;
    static int user_count;
    private boolean first_time;
    static int index;
    private String user_name;
    static int qq_cnt;
    private String key;
    String this_color="";
    String this_gam ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        qq_cnt = intent.getIntExtra("qq_cnt", 0);

        textViewcode =(TextView)findViewById(R.id.textViewcode); //fcode확인
        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        spinner =(Spinner)findViewById(R.id.spinner_question); //spinner_question
        container = (LinearLayout)findViewById(R.id.answer_view); //answer을 나타내는 textView

        initDatabase();

        //전체 질문 목록 가져와서 all_q_arr에 넣기
        DatabaseReference reference_q = FirebaseDatabase.getInstance().getReference("question");
        Log.i("this question","여긴 옴");
        reference_q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("this question","여기도 들어옴");
                all_q_arr = new ArrayList<>();
                all_q_arr.clear();
                for(int i=1;i<7;i++) {  //질문 추가되면 수정필요
                    String this_question = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                    Log.i("this question",this_question);
                    all_q_arr.add(this_question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        //전체 질문 목록 가져오기 끝
        final Button goanswer = (Button) findViewById(R.id.btn_goanswer);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = snapshot.child("fcode").getValue().toString();
                user_name = snapshot.child("userName").getValue().toString();
                Log.i("f_code",f_code);
                member_count = 0;
                //지정한 member 수 가져오기
                a_Reference = a_Database.getReference("family");
                a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //count 수 가져오기
                        String str = (String) snapshot.child("count").getValue();
                        count = Integer.valueOf(str);
                        //member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                        while (members.hasNext()){
                            String member_num = members.next().getKey();
                            member_count++;
                        }


                        //가족 수 확인하여서 가족 만들어졌는지 확인

                        //가족 감나무가 만들어졌을 경우
                        if(member_count == count){
                            a_Reference = a_Database.getReference("family");
                            a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    answer_position = 1;
                                    first_time = snapshot.child("answer").hasChild("1");  //첫번째 질문이 있으면 true이고 없으면 false
                                    Log.i("first time인가용",String.valueOf(first_time));
                                    if(first_time){
                                        long question_cnt = snapshot.child("answer").getChildrenCount();  //현재 db에 올라간 질문 개수 처음엔 1개부터
                                        int q_cnt = Long.valueOf(question_cnt).intValue();
                                        Log.i("all_arr00",String.valueOf(q_cnt));
                                        our_q_arr = new ArrayList<>();
                                        our_q_arr.clear();
                                        for (int i=0; i<q_cnt;i++){
                                            Log.i("all_arr11",all_q_arr.get(i));
                                            String this_question = String.valueOf(all_q_arr.get(i));
                                            our_q_arr.add(this_question);
                                            index = i; //db에 올라간 최신질문이 전체 질문의 몇 번째 index인지
                                            Log.i("all_arr0022",String.valueOf(index));
                                        }

                                        Iterator<DataSnapshot> user_num = snapshot.child("answer").child(String.valueOf(q_cnt)).getChildren().iterator(); //제일 최근 질문
                                        user_count = 0;
                                        while(user_num.hasNext()){//대답한 인원 구하기  //모든 사람이 답변해야한다
                                            String answer_user = user_num.next().getKey();
                                            user_count++;  //대답한 사람의 수
                                            Log.i("usrctn",String.valueOf(user_count));
                                            Log.i("userctn2",String.valueOf(count));
                                        }


                                        if(user_count == count){
                                            //새로운 질문 하나 더 추가
                                            our_q_arr.add(all_q_arr.get(index+1));
                                            index++;
                                            if(our_q_arr.size() == (q_cnt +1)){
                                                goanswer.setClickable(true);
                                            }
                                            if (snapshot.child("answer").child(String.valueOf(q_cnt+1)).hasChild(user_name)) { //사용자가 대답했으면
                                                goanswer.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) { //누르면 마이페이지로 이동
                                                        Toast.makeText(MainActivity.this, "다른 가족들이 안 왔다감1~", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                goanswer.setClickable(false); //버튼 클릭 못함
                                            }
                                            else{
                                                goanswer.setClickable(true); //버튼 클릭 못함
                                            }

                                        }
                                        else if(user_count < count) {  //아직 가족 모두가 대답 안 한 거
                                            while(user_count == count){
                                                if (snapshot.child("answer").child(String.valueOf(our_q_arr.size())).hasChild(user_name)) { //사용자가 대답했으면
                                                    goanswer.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) { //누르면 마이페이지로 이동
                                                            Toast.makeText(MainActivity.this, "다른 가족들이 안 왔다감2~", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    goanswer.setClickable(false); //버튼 클릭 못함
                                                }

                                            }
                                            our_q_arr.add(all_q_arr.get(index+1));

                                        }
                                        //우리 가족 질문 배열에 질문 수 넣기
                                        Log.i("sizesize",String.valueOf(our_q_arr.size()));
                                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, our_q_arr);
                                        spinner = (Spinner)findViewById(R.id.spinner_question);
                                        spinner.setAdapter(arrayAdapter);
                                        spinner.setSelection(index);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //선택->답변 띄우기
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                textView.setText("질문이 뭔감 ! : " + our_q_arr.get(i));
                                                Toast.makeText(getApplicationContext(),our_q_arr.get(i)+"가 선택되었습니다.",
                                                        Toast.LENGTH_SHORT).show();
                                                answer_position = i++; //answer_position : 0~
                                                Log.i("answerposition",String.valueOf(answer_position));
                                                setanswer();

                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    }
                                    else {  // 첫번째 질문 들어가 있을 때 first_time==false
                                        our_q_arr = new ArrayList<>();
                                        our_q_arr.add(String.valueOf(all_q_arr.get(0))); //첫번째 질문 array에 추가
                                        Log.i("sizesize",String.valueOf(our_q_arr.size()));
                                        arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, our_q_arr);
                                        spinner = (Spinner)findViewById(R.id.spinner_question);
                                        spinner.setAdapter(arrayAdapter2);
                                        spinner.setSelection(index);
                                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, our_q_arr);
                                        spinner = (Spinner)findViewById(R.id.spinner_question);
                                        spinner.setAdapter(arrayAdapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //선택->답변 띄우기
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                textView.setText("질문이 뭔감 ! : " + our_q_arr.get(i));
                                                answer_position = i++; //answer_position : 0~
                                                Log.i("answerposition",String.valueOf(answer_position));
                                                setanswer();

                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                        //감나무가 생성되지 않은 경우
                        else if(member_count < count){
                            Toast.makeText(MainActivity.this, "아직 감나무가 생성되지 않았습니다..", Toast.LENGTH_SHORT).show();

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


        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Answeactivity로 이동
                Intent intent = new Intent(MainActivity.this, Answeractivity.class);
                intent.putExtra("question",all_q_arr.get(index)); //선택한 question을 갖고 감.
                intent.putExtra("position",String.valueOf(index+1)); //선택한 position값을 갖고 감.
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

        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn); //왔다감 버튼
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 왔다감으로 이동
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton go_calendar = (ImageButton) findViewById(R.id.calender_btn); //공유캘린더 버튼
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivity(intent);
            }
        });
    }
    private void setanswer(){   //spinner에서 선택한 질문에 대한 사용쟈의 답 동적으로 생성
        a_Reference = a_Database.getReference("family");
        a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                member_arr.clear();
                member_ans_arr.clear();
                member_color_arr.clear();
                member_gam_arr.clear();
                for(DataSnapshot data : dataSnapshot.child("answer").child(String.valueOf(answer_position+1)).getChildren()){
                    key = data.getKey();
                    String value = data.getValue().toString();
                    this_color = dataSnapshot.child("members").child(key).child("user_color").getValue(String.class);
                    this_gam = dataSnapshot.child("members").child(key).child("user_gam").getValue(String.class);
                    member_color_arr.add(this_color);
                    member_gam_arr.add(this_gam);
                    member_arr.add(key);
                    member_ans_arr.add(value);
                }
                int now_size = member_arr.size();

                if (now_size < count ) { //대답 덜한 사람 있는 최신 질문에 대해서는
                    for(int i=0; i<(count-now_size);i++){
                        //부족한 답변 갯수만큼 추가해줘야함
                        member_arr.add("아직"); //member 랑 임의로 답변 추가해줘야함..
                        member_ans_arr.add("아직 답변하지 않았감 !");
                        member_color_arr.add("#92C44B");
                        member_gam_arr.add("4");
                    }
                }
                //저장해 준 것들 하나씩 꺼내서 대답 표시
                //현재 묶여있는 구성원 수만큼 동적으로 layout 생성
                container.removeAllViewsInLayout();
                for(int i=0; i<count; i++){
                    sub_answer n_layout1 = new sub_answer(getApplicationContext());  //동적 layout 생성
                    ImageView iv = n_layout1.findViewById(R.id.profile_image);
                    TextView family_answers = n_layout1.findViewById(R.id.family_answer);  //각각 ID 찾아서
                    iv.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                    GradientDrawable gd1 = (GradientDrawable) iv.getBackground(); //동적으로 테두리 색 바꿈
                    gd1.setStroke(23,Color.parseColor(member_color_arr.get(i))); //배열에 담긴 색깔로 테두리 설정
                    Log.i("member_gam",member_gam_arr.get(i));
                    if (member_gam_arr.get(i).equals("1")){
                        iv.setImageResource(R.drawable.gam1);}
                    else if(member_gam_arr.get(i).equals("2")){
                        iv.setImageResource(R.drawable.gam2);}
                    else if(member_gam_arr.get(i).equals("3")){
                        iv.setImageResource(R.drawable.gam3);}
                    else if(member_gam_arr.get(i).equals("4")){
                        iv.setImageResource(R.drawable.gam4);}
                    else if(member_gam_arr.get(i).equals("5")){
                        iv.setImageResource(R.drawable.gam5);}
                    else if(member_gam_arr.get(i).equals("6")){
                        iv.setImageResource(R.drawable.gam6);}
                    else if(member_gam_arr.get(i).equals("7")){
                        iv.setImageResource(R.drawable.gam7);}
                    else if(member_gam_arr.get(i).equals("8")){
                        iv.setImageResource(R.drawable.gam8);}
                    else{
                        iv.setImageResource(R.drawable.gam1);}
                    if(member_ans_arr.get(i) == "아직 답변하지 않았감 !"){ //아직 대답 안된 부분 처리
                        family_answers.setTextColor(Color.parseColor("#808080"));
                    }
                    family_answers.setText(member_ans_arr.get(i));   //소개 띄우는 부분
                    container.addView(n_layout1); // 기존 layout에 방금 동적으로 생성한 n_layout추가
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
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