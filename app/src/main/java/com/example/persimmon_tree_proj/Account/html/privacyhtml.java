package com.example.persimmon_tree_proj.Account.html;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.Juang_juang.R;

public class privacyhtml extends AppCompatActivity {

    private WebView wv;
    private Button btn_view;
    String urlAddress = "file:///android_asset/www/privacy.html";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacyhtml);

        wv = (WebView)findViewById(R.id.web_view);
        wv.loadUrl("file:///android_asset/www/privacy.html");




        btn_view = (Button) findViewById(R.id.ok_btn);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



    }
}