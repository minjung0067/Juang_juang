package com.example.persimmon_tree_proj.Account.html;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.Juang_juang.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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




        btn_view = (Button) findViewById(R.id.btn_ok);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



    }
}