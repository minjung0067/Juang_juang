package com.example.persimmon_tree_proj.To_do_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Calendar.ShareCalendarActivity;
import com.example.persimmon_tree_proj.Game_activity;
import com.example.persimmon_tree_proj.LodingPage_Activity;
import com.example.persimmon_tree_proj.Main.MainActivity;
import com.example.persimmon_tree_proj.Mypage.MypageActivity;
import com.example.persimmon_tree_proj.QNA.QNA_Activity;
import com.example.persimmon_tree_proj.To_do_list.Todolist_Activity;
import com.example.persimmon_tree_proj.To_do_list.list_adapter;
import com.example.persimmon_tree_proj.customer_sound;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Iterator;

public class Todolist_edit extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    //자기 프로필 표시를 위함
    String user_name = "";
    String user_gam = "";
    String user_color = "";
    RecyclerView recyclerView;
    InputMethodManager inputmethodmanager;


    // 배열리스트
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> contents = new ArrayList<String>();
    private ArrayList<String> writer = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> style = new ArrayList<String>();
    private ArrayList<String> memo_key_list = new ArrayList<String>();
    private ArrayList<Integer> position_list = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_edit);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        Log.i("checking f_code", f_code);
        final String user_gam = intent.getStringExtra("user_gam");
        final String user_color = intent.getStringExtra("user_color");
        final String user_name = intent.getStringExtra("user_name");
        final String family_name = intent.getStringExtra("family_name");
        final String introduce = intent.getStringExtra("introduce");
        final String count = intent.getStringExtra("count");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //본문의 메모장 띄우는 부분
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //가져온 f_code에 해당하는 member 수 세기

                //각 배열 초기화
                title.clear();
                contents.clear();
                writer.clear();
                date.clear();
                style.clear();

                //차례로 돌면서 메모장에 대한 여러 정보들을 각 배열에 순서대로 담기
                Iterator<DataSnapshot> memos = dataSnapshot.child(f_code).getChildren().iterator(); //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                while (memos.hasNext()) { //boolean hasNext() 메소드는 읽어 올 요소가 남아있는지 확인하는 메소드. 있으면 true, 없으면 false를 반환
                    String this_memo = memos.next().getKey();

                    memo_key_list.add(this_memo);

                    String this_title = dataSnapshot.child(f_code).child(this_memo).child("title").getValue(String.class);
                    String this_contents = dataSnapshot.child(f_code).child(this_memo).child("contents").getValue(String.class);
                    String this_style = dataSnapshot.child(f_code).child(this_memo).child("style").getValue(String.class);
                    String this_date = dataSnapshot.child(f_code).child(this_memo).child("date").getValue(String.class);
                    String this_uid = dataSnapshot.child(f_code).child(this_memo).child("writer").getValue(String.class);

                    title.add(this_title);
                    contents.add(this_contents);
                    style.add(this_style);
                    date.add(this_date);
                    writer.add(this_uid);
                }


                // 그리드 세로 줄 세팅
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                // 어댑터와 연결
                RecyclerView.Adapter adapter = new list_adapter_edit(Todolist_edit.this,title,contents,date,writer,style);

                // 어댑터를 리사이클뷰랑 연결
                recyclerView.setAdapter(adapter);

                position_list.clear();


                //선택한 메모 지우기

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (position_list.contains(position)){
                            position_list.remove(position);
                        }
                        else{
                            position_list.add(position);
                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        //삭제하기
        Button edit_ok = (Button) findViewById(R.id.edit_ok);
        edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<position_list.size();i++){
                    Integer delete_position = position_list.get(i);
                    Object memo_key_item = memo_key_list.get(delete_position);

                    //배열에 담아뒀던 삭제 키 값 사용해서 데이터베이스에서 제거
                    reference.child(f_code).child(String.valueOf(memo_key_item)).removeValue();
                }

                Toast.makeText(getApplicationContext(), "메모를 성공적으로 삭제했어요!", Toast.LENGTH_LONG).show();

                // 다시 이동
                Intent intent = new Intent(getApplicationContext(), Todolist_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });


        //전체 삭제
        Button all_edit = (Button)findViewById(R.id.all_edit);
        all_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todolist");
                reference.child(f_code).removeValue();

                Toast.makeText(getApplicationContext(), "모든 메모를 성공적으로 삭제했어요!", Toast.LENGTH_LONG).show();

                Intent intentt = new Intent(getApplicationContext(), Todolist_Activity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intentt);
            }
        });



        //뒤로가기
        TextView goback = (TextView)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intentt = new Intent(getApplicationContext(), Todolist_Activity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intentt);
            }
        });


        //마이페이지 버튼
        TextView mypage = (TextView) findViewById(R.id.btn_mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intent);
            }
        });


        //왔다감 버튼
        ImageButton go_qna = (ImageButton) findViewById(R.id.qna_btn);
        go_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QNA_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
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
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

        //메인 버튼
        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn);
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LodingPage_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
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
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
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
                intent.putExtra("f_code", f_code);
                intent.putExtra("introduce", introduce);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_color", user_color);
                intent.putExtra("count", count);
                intent.putExtra("user_gam", user_gam);
                startActivity(intent);
                overridePendingTransition(0, 0); //intent시 효과 없애기
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Todolist_edit.this, Todolist_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Todolist_edit.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
