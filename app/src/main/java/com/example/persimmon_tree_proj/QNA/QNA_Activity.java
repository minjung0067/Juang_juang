package com.example.persimmon_tree_proj.QNA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ImageButton nextquestion; //다음 질문으로 넘어가는 메세지모양 버튼
    private ImageButton right; //오른쪽 누르면 이 다음 보여주는 버튼
    private ImageButton left; //왼쪽 누르면 이 전 질문 보여주는 버튼


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
    String this_introduce="";
    private String showindex;
    private int ii; //질문 와리가리하기위한 숫자 변수
    private boolean meanswer; //내가 답 했는지 안했는지 true false
    private int didanswer;
    private int pastDate;

    String user_gam = "";
    String user_color = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);



        Intent intent = getIntent();
        f_code = intent.getStringExtra("f_code"); //main 에서 받아온 f_code
        String question = intent.getStringExtra("question");//qnaactivity에서 받아온 question
        showindex = intent.getStringExtra("showindex");//answer에서 받아온 showindex 이걸로 어느 질문까지 보여줄 지 확인함
        textView =(TextView)findViewById(R.id.txt_question); //question 을 나타내는 textView
        right = (ImageButton)findViewById(R.id.btn_right);
        left = (ImageButton)findViewById(R.id.btn_left);
        spinner =(Spinner)findViewById(R.id.spinner_question); //question을 선택하는 spinner
        container = (LinearLayout) findViewById(R.id.answer_view); //answer담는 레이아웃
        nextquestion = (ImageButton) findViewById(R.id.image_next_question);
        initDatabase();

        SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
        formatH = new SimpleDateFormat("yyyyMMdd"); //formatH에 현재 시간 넣어줌 대소문자 중요함
        Date today = new Date(); //today 변수에 Date 부르기
        String strDate = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326

        final Button goanswer = (Button) findViewById(R.id.btn_goanswer);  //답변 하러 가기
        //화면 윗 상단에 마이페이지 감 캐릭터 및 색상 보여주기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //현재 사용자 확보
        DatabaseReference reference_user = FirebaseDatabase.getInstance().getReference("users");
        reference_user.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_code = snapshot.child("fcode").getValue().toString();
                Log.i("myfcode", f_code);
                user_name = snapshot.child("user_name").getValue().toString();
                member_count = 0;
                //지정한 member 수 가져오기
                a_Reference = a_Database.getReference("family");
                a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //count 수 가져오기
                        String str = String.valueOf(snapshot.child("count"));
                        count = Integer.valueOf(str);
                        //본인의 감프로필과 컬러 오른쪽 상단 프로필 맵에 띄우기

                        //가져온 f_code에 해당하는 member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                        while (members.hasNext()) { //boolean hasNext() 메소드는 읽어 올 요소가 남아있는지 확인하는 메소드. 있으면 true, 없으면 false를 반환
                            String member_num = members.next().getKey();
                            member_count++;
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
        //질문 업데이트 함수(모든 가족이 답 했다면 새 질문 추가함) position과 strDate여기서 업데이트
        DatabaseReference reference_ans = FirebaseDatabase.getInstance().getReference("answer").child(f_code);

        //answer이 아직 만들어지지 않았을 경우 157 번째 줄에서 자꾸 오류

        if(reference_ans.equals(null)){
            FirebaseDatabase.getInstance().getReference("answer").child(f_code).child("0").child(user_name).setValue("test");
        }
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

        reference_ans.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> questions = snapshot.getChildren().iterator();
                if(!(questions.hasNext())){ //fcoud까지만 올라간 상태라면, 즉 첫 질문 조차 없는 상태라면
                    FirebaseDatabase.getInstance().getReference("answer").child(f_code).child("1").setValue(strDate); //question번호와 날짜 올리기
                }
                else{ //첫 질문이라도 시작된 상태라면
                    didanswer = (int) snapshot.child(String.valueOf(our_q_arr.size())).getChildrenCount(); //didanswer 변수에 답한 멤버 수 담기
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //조건에 맞게 질문 보여주기
        //String showquestion = our_q_arr.get(Integer.parseInt(showindex));
        int qsize = our_q_arr.size();
        DatabaseReference familyreference = mDatabase.getReference("answer");
        familyreference.child(f_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meanswer = snapshot.child(String.valueOf(our_q_arr.size())).hasChild(user_name);
                if(meanswer){ //내가 답 O 지금 답 한 main화면을 보여주기
                    showQuestion(String.valueOf(our_q_arr.size()));
                }

                else{ //내가 답 X 경우 이 전의 화면까지만 볼 수 있음
                    textView.setText(our_q_arr.get(qsize-1));
                    setanswer(Integer.parseInt(String.valueOf(qsize-1)));
                }

                if (member_count > didanswer){ //다 답을 안했다면
                    nextquestion.setEnabled(false); //새로운 질문으로 넘어가는 버튼 비활성화
                    pastDate = (int) snapshot.child(String.valueOf(our_q_arr.size())).getValue(); //pastDate 시간 저장
                }

                else if((member_count == didanswer) && (pastDate < Integer.parseInt(strDate))){ //모두가 답했고 저번 질문보다 24시간이 지난 후라면 새로운 질문을 our_q_arr추가하고, answer로 넘어갈 수 있는 버튼 활성화
                    our_q_arr.add(all_q_arr.get(qsize+1));
                    nextquestion.setEnabled(true); //새로운 질문으로 넘어가는 버튼 활성화
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                textView.setText(our_q_arr.get(Integer.parseInt(showindex)-1));
//                setanswer(Integer.parseInt(showindex)-1);
                if (ii>1){
                    ii -=1;
                }
                else{
                    Toast.makeText(QNA_Activity.this,"이 질문이 첫 추억이담!",Toast.LENGTH_LONG).show();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                textView.setText(our_q_arr.get(Integer.parseInt(showindex)+1));
//                setanswer(Integer.parseInt(showindex)+1);
                if ((!meanswer) && ii>=qsize){ //내가 답을 안했는데 최신 질문 보려고 누른다면,
                    Toast.makeText(QNA_Activity.this,"지금 질문에 답을 하면 다음 질문으로 넘어갈 수 있어요!",Toast.LENGTH_LONG).show();
                    right.setEnabled(false); //비활성화
                    ii = qsize-1;
                }
                else if ((!meanswer) && ii < qsize){ //내가 답은 안했지만 예전 질문들의 그 다음 질문을 볼 수 있어야함
                    ii+=1;
                }
                else if(meanswer && ii < qsize) {
                    ii+=1;
                }
                else if(meanswer && ii == qsize) { //내가 답 한게 제일 최근 질문인데 오른쪽 버튼을 누른다면
                    Toast.makeText(QNA_Activity.this,"모든 가족들의 답변을 확인하고 다음 메세지로 넘어가잠!",Toast.LENGTH_LONG).show();
                }
            }
        });


        //Answeractivity로 이동 이번에 추가 됨 아래 항목 복사 하는 일 똑같음 -수빈
        nextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QNA_Activity.this, Answeractivity.class);
                intent.putExtra("question",all_q_arr.get(index)); //선택한 question을 갖고 감.
                intent.putExtra("position",String.valueOf(index+1)); //선택한 position값을 갖고 감.
                intent.putExtra("f_code",f_code);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //Answeractivity로 이동
        goanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QNA_Activity.this, Answeractivity.class);
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
                Intent intentt = new Intent(getApplicationContext(), MypageActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code",f_code);
                startActivity(intentt);
            }
        });


        //왔다감 버튼
        ImageButton go_qna = (ImageButton) findViewById(R.id.qna_btn);
        go_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
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
                Intent intent = new Intent(getApplicationContext(), ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //main 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                finish();
            }
        });

        //뭐할감
        ImageButton go_todo = (ImageButton) findViewById(R.id.to_do_btn);
        go_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Todolist_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //게임
        ImageButton go_game = (ImageButton) findViewById(R.id.game_btn);
        go_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

    }

    //질문 textview에 띄우는 함수 왼쪽 오른쪽 선택에 따라서 계속 달라져야해서 이렇게 함수로 따로 만듬
    private void showQuestion(String valueOf) {
            textView.setText(our_q_arr.get(ii));
            setanswer(ii);
    }

    private void setanswer(int iindex){   //선택한 질문에 대한 사용쟈의 답 동적으로 생성
        int i = iindex;
        a_Reference = a_Database.getReference("answer");
        a_Reference.child(f_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                member_arr.clear();
                member_ans_arr.clear();
                member_color_arr.clear();
                member_gam_arr.clear();
                for(DataSnapshot data : dataSnapshot.child(String.valueOf(i)).getChildren()){
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

                if (now_size < count ) { //대답 덜한 사람 있는 최신 질문에 대해서는
                    for(int i=0; i<(count-now_size);i++){
                        //부족한 답변 갯수만큼 추가해줘야함
                        member_arr.add("아직"); //member 랑 임의로 답변 추가해줘야함..
                        member_ans_arr.add("감감 무소식 ..");
                        member_color_arr.add("#808080");
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

//    Intent intent = new Intent(this, Answeractivity.class);
//    intent.putExtra("our_q_arr",our_q_arr);
//    startActivity(intent);

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