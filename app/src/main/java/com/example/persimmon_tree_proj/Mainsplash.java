package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.Juang_juang.R;

public class Mainsplash extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsplash);

        final Intent intent = getIntent();
        final String index = intent.getStringExtra("index_splash");
        textView = (TextView)findViewById(R.id.loading_txt);
        textView.setText(index + "번째 문제를 가져오고 있감");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        },5000);

    }
    protected void onPause() {
        super.onPause();
        finish();
    }
}


