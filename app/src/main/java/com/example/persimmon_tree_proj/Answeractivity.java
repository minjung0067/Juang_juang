package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Answeractivity extends AppCompatActivity {

    private Button btn_answer;
    private EditText edit_answer;
    private TextView textView;
    public String msg; //edit_answer의 텍스트 값을 받아 msg에 저장

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answeractivity);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");

        textView =(TextView)findViewById(R.id.txt_question2);
        textView.setText(question);
        edit_answer = (EditText)findViewById(R.id.edit_answer);
        Button answer = (Button)findViewById(R.id.btn_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = edit_answer.getText().toString();
                databaseReference.child("answer").push().setValue(msg);
                Intent intent = new Intent(Answeractivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
