package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),log_inactivity.class);
                startActivity(intent);
                finish();
            }
        },2000); //splash screen 이 등장하는 시간

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(splash.this,MainActivity.class);
        }else{
            Intent intent = new Intent(splash.this,log_inactivity.class);
        }

    }
    protected void onPause(){
        super.onPause();
        finish();
    }
}