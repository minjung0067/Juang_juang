package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.Juang_juang.R;

public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn); //왔다감 버튼
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 왔다감으로 이동
                Intent intent = new Intent(Calendar.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton go_calendar = (ImageButton) findViewById(R.id.calender_btn); //캘린더 버튼
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더 새로고침
                Intent intent = new Intent(Calendar.this, Calendar.class);
                startActivity(intent);
                finish();
            }
        });
    }
}