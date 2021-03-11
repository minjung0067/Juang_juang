package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Account.log_inactivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), log_inactivity.class);
                startActivity(intent);
                finish();
            }
        },3000); //splash screen 이 등장하는 시간
    }

    protected void onPause(){
        super.onPause();
        finish();
    }
}