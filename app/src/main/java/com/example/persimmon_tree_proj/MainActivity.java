package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

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
    private ArrayList<String> member_name_arr = new ArrayList<String>();


    //family code 관련
    private String f_code;
    static int count;
    static int member_count;
    static int answer_position;
    static int user_count;
    private boolean first_time;
    static int index;
    private String user_name;
    private String key;
    String this_color="";
    String this_gam ="";

    String user_gam = "";
    String user_color = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();

        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        spinner =(Spinner)findViewById(R.id.spinner_question); //question을 선택하는 spinner
        container = (LinearLayout) findViewById(R.id.answer_view); //answer담는 레이아웃

        initDatabase();

        //전체 질문 목록 가져와서 all_q_arr에 넣기
        DatabaseReference reference_q = FirebaseDatabase.getInstance().getReference("question");
        reference_q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                all_q_arr = new ArrayList<>();
                all_q_arr.clear();
                int i=1;
                while(true) {  //질문 추가되면 수정필요 (7은 질문 갯수)
                    String this_question = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                    if(this_question ==null){
                        break;
                    }
                    else{
                        all_q_arr.add(this_question);
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        //전체 질문 가져오기 끝

        final Button goanswer = (Button) findViewById(R.id.btn_goanswer);  //답변 하러 가기

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = snapshot.child("fcode").getValue().toString();
                user_name = snapshot.child("userName").getValue().toString();
                member_count = 0;
                //지정한 member 수 가져오기
                a_Reference = a_Database.getReference("family");
                a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //count 수 가져오기
                        String str = (String) snapshot.child("count").getValue();
                        count = Integer.valueOf(str);
                        //본인의 감프로필과 컬러 오른쪽 상단 프로필 맵에 띄우기

                        //가져온 f_code에 해당하는 member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                        while (members.hasNext()){
                            String member_num = members.next().getKey();
                            member_count++;
                            if(user_name.equals(member_num)) { //현재 로그인된 userid의 이름 == 우리가족 fcode > member > 이름 과 같다면
                                Log.i("profile",member_num);
                                user_gam = snapshot.child("members").child(user_name).child("user_gam").getValue(String.class); //자신의 gam과 컬러를
                                user_color = snapshot.child("members").child(user_name).child("user_color").getValue(String.class);
                                ImageView profile = (ImageView) findViewById(R.id.btn_mypage2);

                                if (user_gam.equals("1")) {
                                    profile.setImageResource(R.drawable.gam1);
                                } else if (user_gam.equals("2")) {
                                    profile.setImageResource(R.drawable.gam2);
                                } else if (user_gam.equals("3")) {
                                    profile.setImageResource(R.drawable.gam3);
                                } else if (user_gam.equals("4")) {
                                    profile.setImageResource(R.drawable.gam4);
                                } else if (user_gam.equals("5")) {
                                    profile.setImageResource(R.drawable.gam5);
                                } else if (user_gam.equals("6")) {
                                    profile.setImageResource(R.drawable.gam6);
                                } else if (user_gam.equals("7")) {
                                    profile.setImageResource(R.drawable.gam7);
                                } else if (user_gam.equals("8")) {
                                    profile.setImageResource(R.drawable.gam8);
                                } else {
                                    profile.setImageResource(R.drawable.gam1);
                                }

                                profile.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd1 = (GradientDrawable) profile.getBackground(); //동적으로 테두리 색 바꿈
                                gd1.setStroke(50, Color.parseColor(user_color)); //배열에 담긴 색깔로 테두리 설정

                            }

                        }


                        //가족 수 확인하여서 가족 만들어졌는지 확인 member_count 와 count 비교


                        //가족 감나무가 만들어졌을 경우
                        if(member_count == count){
                            a_Reference = a_Database.getReference("family");
                            a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    answer_position = 1;
                                    first_time = snapshot.child("answer").hasChild("1");  //첫번째 질문이 있으면 true이고 없으면 false
                                    if(first_time){
                                        //첫번쨰 질문이 있는 경우, 답변해야 하는 질문 가져오기
                                        long question_cnt = snapshot.child("answer").getChildrenCount();  //현재 데이터베이스에 우리가족이 대답한 question의 갯수
                                        int q_cnt = Long.valueOf(question_cnt).intValue();
                                        our_q_arr = new ArrayList<>();                                   //현재 우리가족이 대답한 question을 갖는 배열
                                        our_q_arr.clear();
                                        for (int i=0; i<q_cnt;i++){
                                            String this_question = String.valueOf(all_q_arr.get(i));
                                            our_q_arr.add(this_question);                                 //현재 우리가족이 대답한 question을 배열에 추가
                                            index = i;                                                   //db에 올라간 최신질문이 전체 질문의 몇 번째 index인지
                                        }

                                        //제일 최근 질문에 답변한 인원 수 구하기
                                        Iterator<DataSnapshot> user_num = snapshot.child("answer").child(String.valueOf(q_cnt)).getChildren().iterator(); //제일 최근 질문
                                        user_count = 0; //질문에 답변한 인원 수
                                        while(user_num.hasNext()){
                                            String answer_user = user_num.next().getKey();
                                            user_count++;  //대답한 사람의 수
                                        }

                                        //모두 답변한 경우, 새로운 질문 하나 더 추가
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
                                                        Toast.makeText(MainActivity.this, "다른 가족들이 안 왔다감~", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                goanswer.setClickable(false); //버튼 클릭 못함
                                            }
                                            else{
                                                goanswer.setClickable(true); //버튼 클릭 못함
                                            }


                                        }

                                        //모두 답변하지 못한 경우
                                        else if(user_count < count) {
                                            //모두가 답변하기 전까지 답변하러가기 버튼 누를 수 없으며, 모두 답변한 경우 다음 질문으로 넘어감.

                                            while(user_count == count){
                                                if (snapshot.child("answer").child(String.valueOf(our_q_arr.size())).hasChild(user_name)) { //사용자가 대답했으면
                                                    goanswer.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) { //누르면 마이페이지로 이동
                                                            Toast.makeText(MainActivity.this, "다른 가족들이 안 왔다감", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    goanswer.setClickable(false); //버튼 클릭 못함
                                                }

                                            }

                                        }

                                        //우리 가족 질문 배열에 질문 수 넣기
                                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, our_q_arr);
                                        spinner = (Spinner)findViewById(R.id.spinner_question);
                                        spinner.setAdapter(arrayAdapter);
                                        spinner.setSelection(index); //가장 최근 질문 spinner에 자동 선택
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //spinner를 선택하면 질문과 함꼐 답변 가져오기
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                textView.setText(our_q_arr.get(i));
                                                answer_position = i++; //answer_position : 0~
                                                setanswer();

                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    }


                                    //첫번쨰 질문이 들어가지 않았을 경우
                                    else {  // 첫번째 질문 들어가 있을 때 first_time==false
                                        our_q_arr = new ArrayList<>();
                                        our_q_arr.add(String.valueOf(all_q_arr.get(0))); //첫번째 질문 array에 추가
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
                                                textView.setText( our_q_arr.get(i));
                                                answer_position = i++; //answer_position : 0~
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
                            Intent intent = new Intent(getApplicationContext(),Waitactivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
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


        //Answeractivity로 이동
        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Answeractivity.class);
                intent.putExtra("question",all_q_arr.get(index)); //선택한 question을 갖고 감.
                intent.putExtra("position",String.valueOf(index+1)); //선택한 position값을 갖고 감.
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(MainActivity.this,MypageActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code",f_code);
                startActivity(intentt);
            }
        });



        //왔다감 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //공유캘린더 버튼
        ImageButton go_calendar = (ImageButton) findViewById(R.id.calendar_btn);
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더로 이동
                Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //고객의 소리함
        ImageButton go_setting = (ImageButton) findViewById(R.id.setting_btn);
        go_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,customer_sound.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
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
                        member_ans_arr.add("감감 무소식 ..");
                        member_color_arr.add("#92C44B");
                        member_gam_arr.add("4");
                    }
                }
                //저장해 준 것들 하나씩 꺼내서 대답 표시
                //현재 묶여있는 구성원 수만큼 동적으로 layout 생성
                container.removeAllViewsInLayout();
                for(int i=0; i<count; i++){
                    sub_answer n_layout1 = new sub_answer(getApplicationContext());
                    TextView name = n_layout1.findViewById(R.id.tv_name); //각자의 이름
                    name.setText(member_arr.get(i).toString());//동적 layout 생성
                    ImageView iv = n_layout1.findViewById(R.id.profile_image);
                    TextView family_answers = n_layout1.findViewById(R.id.family_answer);  //각각 ID 찾아서
                    iv.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                    GradientDrawable gd1 = (GradientDrawable) iv.getBackground(); //동적으로 테두리 색 바꿈
                    gd1.setStroke(50,Color.parseColor(member_color_arr.get(i))); //배열에 담긴 색깔로 테두리 설정
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
                    if(member_ans_arr.get(i) == "감감 무소식.."){ //아직 대답 안된 부분 처리
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