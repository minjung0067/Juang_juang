package com.example.persimmon_tree_proj.QNA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;

import java.text.SimpleDateFormat; //시간 날짜 체크를 위함
import java.util.Date;
import java.util.Iterator;


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
    //private ArrayList<String> all_q_arr; //만들어 놓은 모든 질문 담기는 배열
    private ArrayList<String> our_q_arr; //우리 가족의 질문이 담기는 배열
    private ArrayList<String> member_arr = new ArrayList<String>();
    private ArrayList<String> member_ans_arr = new ArrayList<String>();
    ArrayList<String> member_color_arr = new ArrayList<String>();
    ArrayList<String> member_gam_arr = new ArrayList<String>();
    ArrayList<String> uid_list = new ArrayList<String>();


    //family code 관련
    private String f_code;
    static int member_count;
    static int answer_position;
    static int user_count;
    private boolean first_time;
    static int index;
    private String user_name;
    private String key;
    String this_color = "";
    String this_gam = "";
    String this_introduce = "";

    String user_gam = "";
    String user_color = "";

    //추가
    private int didanswer; //가족 중 몇 명이 대답했는지 +1(Date때문)
    private int count;
    private Object Firebase;
    private FirebaseAuth firebaseAuth;
    private int question_cnt;

    private View blurView;
    private View linearView;
    private View answer_view;

    private ImageButton newmessage;
    private int questionday;
    private TextView Numq;

    private int qsize;
    private int que_cnt; //삭제 될 예쩡
    private String qu;
    private DatabaseReference referencesetanswer;
    private DatabaseReference referencescanfamily;

    String all_q_arr[] = {"가족의 도플갱어가 나타난다면 어떻게 구분 할 것인감?", "내일이 지구종말이라면?",
            "가족들의 이름을 바꿀 수 있는 능력이 생긴다면?", "나이를 실감할 때는 언제인감?", "미처 전하지 못했던 고마운 일이 있다면 살짝 털어놓아볼감?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 user 확인
        String uid = user.getUid();

        textView = (TextView) findViewById(R.id.txt_question); //question 을 나타내는 textView
        //spinner =(Spinner)findViewById(R.id.spinner_question); //question을 선택하는 spinner
        container = (LinearLayout) findViewById(R.id.answer_view); //answer담는 레이아웃
        View blurView = (View) findViewById(R.id.blurView);
        View linearView = (View) findViewById(R.id.linear_view);
        View answer_vew = (View) findViewById(R.id.answer_view);
        TextView showblur = (TextView) findViewById(R.id.txt_blur);
        ImageButton blurgotoans = (ImageButton) findViewById(R.id.blur_gotoans);
        ImageButton newmessage = (ImageButton) findViewById(R.id.newmessagecome);
        TextView Numq = (TextView) findViewById(R.id.tv_questionnum);

        SimpleDateFormat formatH; // formatH = 0-23으로 표현하는 시각 포맷 변수 선언
        formatH = new SimpleDateFormat("yyyyMMdd"); //formatH에 현재 시간 넣어줌 대소문자 중요함
        Date today = new Date(); //today 변수에 Date 부르기
        String everyToday = formatH.format(today); //오늘 날짜가 strDate 변수에 저장. 20210326

        initDatabase();

        Log.i("bin_cycle Qna oncreate", all_q_arr[0]);

//        all_q_arr.add(0,"가족의 도플갱어가 나타난다면 어떻게 구분 할 것인감?");
//        all_q_arr.add(1,"내일이 지구종말이라면?");
//        all_q_arr.add(2,"가족들의 이름을 바꿀 수 있는 능력이 생긴다면?");
//        all_q_arr.add(3,"나이를 실감할 때는 언제인감?");
//        all_q_arr.add(4,"미처 전하지 못했던 고마운 일이 있다면 털어놓아보라감");

        //DatabaseReference referencescanfamily = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference referencesetanswer = FirebaseDatabase.getInstance().getReference();

//        all_q_arr = new ArrayList<>();
        our_q_arr = new ArrayList<>();
        //uid_list = new ArrayList<>();
        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");
        final String count2 = intent.getStringExtra("count");
        ArrayList<String> uid_list = (ArrayList<String>) intent.getSerializableExtra("uid_list");
        ArrayList<String> member_gam_arr = (ArrayList<String>) intent.getSerializableExtra("member_gam_arr");
        ArrayList<String> member_arr = (ArrayList<String>) intent.getSerializableExtra("member_arr");
        ArrayList<String> member_color_arr = (ArrayList<String>) intent.getSerializableExtra("member_color_arr");


        a_Reference = a_Database.getReference();
        a_Reference.child("answer").child(f_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //본인의 감프로필과 컬러 오른쪽 상단 프로필 맵에 띄우기
                Log.i("bin_check", "여긴 들어옴 젤 처음");
                que_cnt = 0;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    qu = String.valueOf(ds.getValue());
                    Log.d("bin_TAG", qu);
                    que_cnt ++;
                    Log.i("bin_error", String.valueOf(que_cnt));

                }
                question_cnt = que_cnt;


                //question_cnt = (int) snapshot.getChildrenCount();  //현재 데이터베이스에 우리가족이 대답한 question의 갯수
                Log.i("bin_arr question_cnt", String.valueOf(question_cnt));
                for (int i = 0; i < question_cnt; i++) {
                    String this_question = String.valueOf(all_q_arr[i]);
                    Log.i("bin_arr 1", "" + i + this_question);
                    our_q_arr.add(this_question);                                 //현재 우리가족이 대답한 question을 배열에 추가
                    index = i;                                                   //db에 올라간 최신질문이 전체 질문의 몇 번째 index인지
                }
                qsize = our_q_arr.size();
                if (snapshot.child("1").getChildrenCount() == 1 || snapshot.child("1").child(uid).getValue() == null) { //첫 질문 배열에 넣음
                    Log.i("bin_check", "첫 질문에 아무도 답 안하거나 내가 답 안함");
                    /*
                    String this_question = all_q_arr.get(0);
                    our_q_arr.add(0, this_question);  //현재 우리가족이 대답한 question을 배열에 추가
                    index = our_q_arr.size();
                    Log.i("bin_error", "첫 질문 배열에 넣음 index : "+String.valueOf(index));
                    if(our_q_arr.size()!=0){
                        textView.setText(our_q_arr.get(index-1)); //main화면에서 글씨 창 보이기
                    }
                    Numq.setText(String.valueOf(index)+"번째 감");*/
                    textView.setText("가족의 도플갱어가 나타난다면 어떻게 구분 할 것인감?");
                    Numq.setText("1 번째 감");
                    readData(a_Reference, new OnGetDataListiner() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(QNA_Activity.this, "첫 질문이 도착했대요 ! 대답하러 가볼까요? ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Answeractivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("question", "가족의 도플갱어가 나타난다면 어떻게 구분 할 것인감?"); //선택한 question을 갖고 감.
                            intent.putExtra("position", String.valueOf(qsize)); //선택한 position값을 갖고 감.
                            intent.putExtra("f_code", f_code);
                            intent.putExtra("introduce", introduce);
                            intent.putExtra("user_name", user_name);
                            intent.putExtra("user_color", user_color);
                            intent.putExtra("user_gam", user_gam);
                            intent.putExtra("count", count2);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0); //intent시 효과 없애기
                        }

                        @Override
                        public void onStart() {
                            //when starting
                            Log.d("ONSTART", "Started");
                        }

                        @Override
                        public void onFailure() {
                            Log.d("onFailure", "Failed");
                        }

                    });
                } else { //처음이 아니라면
                    //String c = count2;
                    ///count = Integer.parseInt(c);                  //가족 수
                    count = 2;

                    didanswer = (int) snapshot.child(String.valueOf(our_q_arr.size())).getChildrenCount();             //didanswer 변수에 답한 멤버 수 담기
                    if (String.valueOf(snapshot.child(String.valueOf(our_q_arr.size())).child("Date").getValue())==null) {
                        Log.i("bin_error", "null questionday is" + questionday);
                    }
                    else{
                        String j = (String) snapshot.child(String.valueOf(our_q_arr.size())).child("Date").getValue();
                        Log.i("bin_error questionday", "our_q_size is" + our_q_arr.size());
                        questionday = Integer.parseInt(j); //제일 최근 질문에 올라간 날짜 담기
                        Log.i("bin_error questionday", "questionday is" + questionday);
                    }
                    Log.i("bin_error", "line 244 : " + didanswer + "that is did answer. our q arr is" + String.valueOf(our_q_arr.size()));

                    if ((didanswer - 1) == count && Integer.valueOf(everyToday) > questionday) { //모두가 답하고 24시간이 지남
                        Log.i("bin_check", "1번 if -> set answer, none blur");
                        index = our_q_arr.size();
                        Log.i("bin_index", String.valueOf(index));
                        setanswer(index);
                        if (our_q_arr.size() != 0) {
                            index = our_q_arr.size();
                            index -=1;
                            textView.setText(our_q_arr.get(index)); //main화면에서 글씨 창 보이기
                        }
                        blurView.setVisibility(View.INVISIBLE);
                        showblur.setVisibility(View.INVISIBLE);           //모든 가족이 답해야만 ~ 주황 글씨 숨김
                        String stDate = formatH.format(today);          //오늘 날짜가 stDate 변수에 저장. 20210326
                        String qq = all_q_arr[index];//사이즈로는 3 index상으로 2번 질문이 추가되어야함
                        our_q_arr.add(qq); //젤 첫 질문 q_arr에 추가
                        qsize = our_q_arr.size();
                        Log.i("bin_check", qq);
                        FirebaseDatabase.getInstance().getReference("answer").child(f_code).child(String.valueOf(qsize)).child("Date").setValue(stDate); //question번호와 날짜 올리기

                        //새로운 질문 보여주는 버튼 보여주기 + 활성화 버튼 누를 시

                        Toast.makeText(QNA_Activity.this, "새 질문이 왔대요 ! 답하러 가볼까요 ? ", Toast.LENGTH_LONG).show();

                        newmessage.setVisibility(View.VISIBLE);             //새 질문이 왔다는  메세지 팝업 뜸
                        newmessage.setOnClickListener(new View.OnClickListener() { //클릭시 answer로 이동
                            @Override
                            public void onClick(View v) {
                                qsize = our_q_arr.size();
                                String this_question = our_q_arr.get(qsize - 1);
                                Intent intent = new Intent(getApplicationContext(), Answeractivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("question", this_question); //선택한 question을 갖고 감.
                                intent.putExtra("position", String.valueOf(qsize)); //선택한 position값을 갖고 감.
                                intent.putExtra("f_code", f_code);
                                intent.putExtra("introduce", introduce);
                                intent.putExtra("user_name", user_name);
                                intent.putExtra("user_color", user_color);
                                intent.putExtra("user_gam", user_gam);
                                intent.putExtra("count", count2);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0); //intent시 효과 없애기
                            }
                        });

                    } else if ((didanswer - 1) == count && Integer.valueOf(everyToday) <= questionday) {
                        Log.i("bin_check", "2번 else if 24시간 안지남 setanswer + nothing. index = " + index);
                        setanswer(index + 1);
                        if (our_q_arr.size() != 0) {
                            textView.setText(our_q_arr.get(index)); //main화면에서 글씨 창 보이기
                        }
                        blurView.setVisibility(View.INVISIBLE);
                        showblur.setVisibility(View.INVISIBLE);
//                      index = our_q_arr.size();
                        Toast.makeText(QNA_Activity.this, "질문은 하루에 하나씩만 제공한담! 내일의 새 질문을 기대해달라감!", Toast.LENGTH_LONG).show();
                    } else { //모두가 답을 안했음 블러 보여주기
                        Log.i("bin_check", "3번 else 다 답안함 몇 번째감 보여주기 blur visible");
                        blurView.setVisibility(View.VISIBLE);       //블러 처리 시킴
                        showblur.setVisibility(View.VISIBLE);       //우리 가족이 웅앵 글씨 보이게
                        if (our_q_arr.size() != 0) {
                            textView.setText(our_q_arr.get(index)); //main화면에서 글씨 창 보이기
                        }
                        Numq.setText(String.valueOf(index + 1) + "번째 감");
                        if (snapshot.child(String.valueOf(our_q_arr.size())).hasChild(uid)) {
                            Log.i("bin_check", "3번-1 if 다 답안함 근데 난함 -> floating btn 안보이게");
                            //답하러 가는 플로트 버튼 안보이게 + 비활성화 하기
                            blurgotoans.setVisibility(View.INVISIBLE);
                            blurView.setVisibility(View.VISIBLE);
                            showblur.setVisibility(View.VISIBLE);       //우리 가족이 웅앵 글씨 보이게
                            Toast.makeText(QNA_Activity.this, "나 빼고 아직 답 안함!", Toast.LENGTH_LONG).show();

                        } else {
                            Log.i("bin_check", "3번-2 else 다 답안함 근데 나도 안함 -> floating btn 띄우기");
                            //답하러 가는 플로트 버튼 띄우기 + 활성화
                            Toast.makeText(QNA_Activity.this, "모두가 답 안했고 나도 답 안함", Toast.LENGTH_LONG).show();
                            Numq.setText(String.valueOf(index+1) + "번째 감");
                            blurgotoans.setVisibility(View.VISIBLE);
                            blurgotoans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String this_question = our_q_arr.get(index);
                                    Intent intent = new Intent(getApplicationContext(), Answeractivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("question", this_question); //선택한 question을 갖고 감.
                                    intent.putExtra("position", String.valueOf(index+1)); //선택한 position값을 갖고 감.
                                    intent.putExtra("f_code", f_code);
                                    intent.putExtra("introduce", introduce);
                                    intent.putExtra("user_name", user_name);
                                    intent.putExtra("user_color", user_color);
                                    intent.putExtra("user_gam", user_gam);
                                    intent.putExtra("count", count2);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0, 0); //intent시 효과 없애기
                                }
                            });

                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });





        //질문 리스트로 넘어가는 창
        TextView questionList = (TextView) findViewById(R.id.txt_question);
        questionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(getApplicationContext(), qlistpopup_activity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("Qnum", String.valueOf(index));
                startActivity(intentt);
            }
        });

        //마이페이지 버튼
        ImageButton mypage = (ImageButton) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(getApplicationContext(), MypageActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code", f_code);
                startActivity(intentt);
            }
        });


        //왔다감 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });


    }


    private void readData(DatabaseReference question, OnGetDataListiner onGetDataListiner) {
        onGetDataListiner.onStart();
        onGetDataListiner.onSuccess();
//        question.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshotq) {
//                onGetDataListiner.onSuccess(snapshotq);
//                Log.i("binerror 1st onSuccess", "ㅏㅏ");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                onGetDataListiner.onFailure();
//            }
//        });
    }

    private void setanswer(int a) {   //spinner에서 선택한 질문에 대한 사용쟈의 답 동적으로 생성
        Log.i("bin_error", "setanswer 들어옴");
        index = a;
        Log.i("bin_index in setanswer",""+a);
//        Numq.setText(String.valueOf(index)+"번째 감");


        referencesetanswer = FirebaseDatabase.getInstance().getReference();
        referencesetanswer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//                for(DataSnapshot membersData : dataSnapshot.child("groups").child(f_code).child("members").getChildren()) {
//                    Log.i("bin_membersData", "plzhere");
//                    Log.i("bin_user membersData", String.valueOf(membersData));
//                    String user = membersData.getValue().toString();
//                    uid_list.add(user);
//                    Log.i("bin_user", user);
//                }


                    member_ans_arr.clear();
                    while (count > member_ans_arr.size()) {
                        for (int i = 0; i < count; i++) {
                            Iterator<DataSnapshot> whoans = dataSnapshot.child("answer").child(f_code).child(String.valueOf(a)).getChildren().iterator();
                            if (uid_list.get(i) == whoans.next().getKey()) {
                                member_ans_arr.add(i, String.valueOf(dataSnapshot.child("answer").child(f_code).child(String.valueOf(a)).child(uid_list.get(i)).getValue()));
                            }
                        }
                        Log.i("bin_check", "memeber ans arr size 확인" + member_ans_arr.size());
                        if (count == member_ans_arr.size()) {
                            Log.i("bin_check", "while문 에러 memeber ans arr size 확인" + member_ans_arr.size());
                            break;
                        }
                    }

                    int now_size = member_arr.size();

                    //저장해 준 것들 하나씩 꺼내서 대답 표시
                    //현재 묶여있는 구성원 수만큼 동적으로 layout 생성
                    container.removeAllViewsInLayout();
                    for (int i = 0; i < count; i++) {
                        sub_answer n_layout1 = new sub_answer(getApplicationContext());
                        TextView name = n_layout1.findViewById(R.id.tv_name); //각자의 이름
                        name.setText(member_arr.get(i).toString());//동적 layout 생성
                        ImageView iv = n_layout1.findViewById(R.id.profile_image);
                        TextView family_answers = n_layout1.findViewById(R.id.family_answer);  //각각 ID 찾아서
                        iv.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                        GradientDrawable gd1 = (GradientDrawable) iv.getBackground(); //동적으로 테두리 색 바꿈
                        gd1.setStroke(50, Color.parseColor(member_color_arr.get(i))); //배열에 담긴 색깔로 테두리 설정
                        if (member_gam_arr.get(i).equals("1")) {
                            iv.setImageResource(R.drawable.gam1);
                        } else if (member_gam_arr.get(i).equals("2")) {
                            iv.setImageResource(R.drawable.gam2);
                        } else if (member_gam_arr.get(i).equals("3")) {
                            iv.setImageResource(R.drawable.gam3);
                        } else if (member_gam_arr.get(i).equals("4")) {
                            iv.setImageResource(R.drawable.gam4);
                        } else if (member_gam_arr.get(i).equals("5")) {
                            iv.setImageResource(R.drawable.gam5);
                        } else if (member_gam_arr.get(i).equals("6")) {
                            iv.setImageResource(R.drawable.gam6);
                        } else if (member_gam_arr.get(i).equals("7")) {
                            iv.setImageResource(R.drawable.gam7);
                        } else if (member_gam_arr.get(i).equals("8")) {
                            iv.setImageResource(R.drawable.gam8);
                        } else {
                            iv.setImageResource(R.drawable.gam1);
                        }
                        if (member_ans_arr.get(i) == null) { //아직 대답 안된 부분 처리
                            family_answers.setTextColor(Color.parseColor("#808080"));
                        }
                        family_answers.setText(member_ans_arr.get(i));   //소개 띄우는 부분
                        container.addView(n_layout1); // 기존 layout에 방금 동적으로 생성한 n_layout추가
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //전체 user 가져오기
    }

    public void scanfamiliy(){
        Log.i("bin_scan","scanfamiliy 들어옴");
//        referencescanfamily = FirebaseDatabase.getInstance().getReference();
//        referencescanfamily.child("groups").child(f_code).child("members").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                uid_list.clear();
//                for(DataSnapshot membersData : snapshot.getChildren()) {
//                    Log.i("bin_membersData", "plzhere");
//                    Log.i("bin_user membersData", String.valueOf(membersData));
//                    String user = membersData.getValue().toString();
//                    String user1 = membersData.getKey().toString();
//                    uid_list.add(user);
//                    Log.i("bin_user", user);
//                    Log.i("bin_user 1 ", user1);
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        a_Database = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("question");
        a_Reference = a_Database.getReference("answer");

        mChild = new ChildEventListener() {


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

    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
        a_Reference.removeEventListener(a_Child);
    }
}


