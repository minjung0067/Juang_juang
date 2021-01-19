package com.example.persimmon_tree_proj;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;

public class servicehtml extends AppCompatActivity {

    private WebView wv;
    private Button btn_view;
    String urlAddress = "file:///android_asset/www/service.html";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicehtml);

        wv = (WebView)findViewById(R.id.web_view);
        wv.loadUrl("file:///android_asset/www/service.html");




        btn_view = (Button) findViewById(R.id.btn_ok);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



    }
}