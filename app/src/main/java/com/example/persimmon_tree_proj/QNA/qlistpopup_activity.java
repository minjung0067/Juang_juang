package com.example.persimmon_tree_proj.QNA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.ListViewAdapter;
import com.example.persimmon_tree_proj.Family.ListViewItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class qlistpopup_activity extends AppCompatActivity {

    private QNAListViewAdapter Qadapter;
    private ListView listView;
    private int untilnum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlistpopup);

        Intent intent = getIntent();
        String untilnumber = intent.getStringExtra("Qnum");
        untilnum = Integer.parseInt(untilnumber);
        //Adapter 생성
        Qadapter = new QNAListViewAdapter();

        //리스트 뷰 참조 및 adpater달기
        listView = (ListView) findViewById(R.id.Listview1);
        listView.setAdapter(Qadapter);


        DatabaseReference reference_q = FirebaseDatabase.getInstance().getReference("question");
        reference_q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; i <= untilnum ; i++) {
                    String this_question = dataSnapshot.child(String.valueOf(i)).getValue(String.class);
                        if(this_question ==null){
                            break;
                        }
                        else{
                            Qadapter.addItem(String.valueOf(i), this_question);     //몇 번째 감, question순서
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        Qadapter.notifyDataSetInvalidated(); //어댑터의 변경을 알림
    }
}
