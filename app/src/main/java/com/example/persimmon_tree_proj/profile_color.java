package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.Juang_juang.R;

public class profile_color extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_color);
    }
    ImageButton goback = (ImageButton)findViewById(R.id.go_back);
    public void onClick(View v) {
        Intent intent = new Intent(profile_color.this, MypageActivity.class);
        startActivity(intent);
        }
}