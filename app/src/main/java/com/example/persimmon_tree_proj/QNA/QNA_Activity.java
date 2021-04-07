<<<<<<< HEAD
=======
package com.example.persimmon_tree_proj.QNA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.To_do_list.Todolist_Activity;
import com.example.persimmon_tree_proj.customer_sound;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import java.text.SimpleDateFormat; //시간 날짜 체크를 위함
import java.util.Date;


public class QNA_Activity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private TextView textView; //질문 나오는 textView
    //array배열을 생성하고 spinner와 연결

    //answer 관련
    private FirebaseDatabase a_Database;
    private DatabaseReference a_Reference;
    private ChildEventListener a_Child;
    private LinearLayout container;
    private Spinner spinner; //질문 목록이 있는 spinner
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter2;
    private ArrayList<String> all_q_arr; //만들어 놓은 모든 질문 담기는 배열
    private ArrayList<String> our_q_arr; //우리 가족의 질문이 담기는 배열
    private ArrayList<String> member_arr = new ArrayList<String>();
    private ArrayList<String> member_ans_arr =  new ArrayList<String>();
    ArrayList<String> member_color_arr =  new ArrayList<String>();
    ArrayList<String> member_gam_arr =  new ArrayList<String>();


    //family code 관련
    private String f_code;
    static int member_count;
    static int answer_position;
    static int user_count;
    private boolean first_time;
    static int index;
    private String user_name;
    private String key;
    String this_color="";
    String this_gam ="";
    String this_introduce="";

    String user_gam = "";
    String user_color = "";

    //추가
    private int didanswer; //가족 중 몇 명이 대답했는지 +1(Date때문)
    private int count ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        f_code = intent.getStringExtra("user_fcode");
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");
        int count = Integer.parseInt(intent.getStringExtra("count"));


        Log.i("bin_check",count + "f_code is " + f_code);
        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        //spinner =(Spinner)findViewById(R.id.spinner_question); //question을 선택하는 spinner
        container = (LinearLayout) findViewById(R.id.answer_view); //answer담는 레이아웃

        SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
        formatH = new SimpleDateFormat("yyyyMMdd"); //formatH에 현재 시간 넣어줌 대소문자 중요함
        Date today = new Date(); //today 변수에 Date 부르기
        String everyToday = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326

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

        a_Reference = a_Database.getReference("answer");
        a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //본인의 감프로필과 컬러 오른쪽 상단 프로필 맵에 띄우기
                if (snapshot.child("1").getChildrenCount()==1){ //첫 질문 배열에 넣음
                    long question_cnt = snapshot.child("answer").getChildrenCount();  //현재 데이터베이스에 우리가족이 대답한 question의 갯수
                    int q_cnt = Long.valueOf(question_cnt).intValue();
                    our_q_arr = new ArrayList<>();                                   //현재 우리가족이 대답한 question을 갖는 배열
                    our_q_arr.clear();
                    for (int i=0; i<q_cnt;i++){
                        String this_question = String.valueOf(all_q_arr.get(i));
                        our_q_arr.add(this_question);                                 //현재 우리가족이 대답한 question을 배열에 추가
                        index = i;                                                   //db에 올라간 최신질문이 전체 질문의 몇 번째 index인지
                    }
                    Log.i("binerror","plz come here");
                    Log.i("binerror", "childrencount : "+String.valueOf(snapshot.child("1").getChildrenCount()));
                    Log.i("binerror","line 229 our_q_arr size would be one -> "+String.valueOf(our_q_arr.size()));
                    Toast.makeText(QNA_Activity.this,"첫 질문이 도착했대요 ! 대답하러 가볼까요? ",Toast.LENGTH_LONG).show();
                    //처음이라면 메인 안보여주고 바로 answer로 넘김
                    index = our_q_arr.size();
                    Intent intent = new Intent(QNA_Activity.this, Answeractivity.class);
                    intent.putExtra("question",all_q_arr.get(index-1)); //선택한 question을 갖고 감.
                    intent.putExtra("position",String.valueOf(index)); //선택한 position값을 갖고 감.
                    intent.putExtra("f_code",f_code);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0); //intent시 효과 없애기
                }
                else{ //처음이 아니라면
                    didanswer = (int) snapshot.child(String.valueOf(our_q_arr.size())).getChildrenCount(); //didanswer 변수에 답한 멤버 수 담기
                    int questionday = (int) snapshot.child(String.valueOf(our_q_arr.size())).child("Date").getValue();
                    Log.i("binerror", "line 244 : "+String.valueOf(our_q_arr.size()));
                    if((didanswer-1) == count && (Integer.valueOf(everyToday)>questionday)){ //모두가 답 했다면 (Date 까지 childrenCount에서 셀테니까 -1해야함) 그리고 하루가 지났다면
                        String stDate = formatH.format(today); //오늘 날짜가 stDate 변수에 저장. 20210326
                        index = our_q_arr.size(); //2번 질문까지 답했으면 사이즈 = 2
                        FirebaseDatabase.getInstance().getReference("answer").child(f_code).child(String.valueOf(index+1)).child("Date").setValue(stDate); //question번호와 날짜 올리기
                        String qq = all_q_arr.get(index);//사이즈로는 3 index상으로 2번 질문이 추가되어야함
                        our_q_arr.add(qq); //젤 첫 질문 q_arr에 추가
                    }
                    else if((didanswer-1) == count && (Integer.valueOf(everyToday)<=questionday)){ //모두가 답했지만, 하루 이상이 안지났다면
                        index = our_q_arr.size();
                        Toast.makeText(QNA_Activity.this,"질문은 하루에 하나씩만 제공한담! 내일의 새 질문을 기대해달라감!",Toast.LENGTH_LONG).show();

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        a_Reference = a_Database.getReference("answer");
        a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Answeractivity로 이동
        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QNA_Activity.this,Answeractivity.class);
                intent.putExtra("question",all_q_arr.get(index)); //선택한 question을 갖고 감.
                intent.putExtra("position",String.valueOf(index+1)); //선택한 position값을 갖고 감.
                intent.putExtra("f_code",f_code);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(getApplicationContext(),MypageActivity.class);
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
                Intent intent = new Intent(QNA_Activity.this, customer_sound.class);
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
                    this_introduce = dataSnapshot.child("members").child(key).child("introduce").getValue(String.class);
                    member_color_arr.add(this_color);
                    member_gam_arr.add(this_gam);
                    member_arr.add(this_introduce);
                    member_ans_arr.add(value);
                }
                int now_size = member_arr.size();

                if (didanswer-1 < count ){ //대답 덜한 사람 있는 최신 질문에 대해서는
                    //블러 추가
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
                    if(member_ans_arr.get(i) == null ){ //아직 대답 안된 부분 처리
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

>>>>>>> 63ff42646503e7b7db059042b0f369bc8d3ffc69
