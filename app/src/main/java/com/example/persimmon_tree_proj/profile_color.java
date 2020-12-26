package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.Juang_juang.R;

public class profile_color extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_color);
        Button gam = (Button) findViewById(R.id.gam);     //감캐릭터 고르기 버튼
        gam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_color.this, profile_gam.class);
                startActivity(intent);
            }
        });

        ImageButton go_back = (ImageButton) findViewById(R.id.go_back);    //뒤로가기
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_color.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Click(View view) {  //버튼 클릭시마다 switch문으로 다른 색깔 선택
            switch (view.getId())
            {
                case R.id.c1:
                    break;
                case R.id.c2:
                    break;
                case R.id.c3:
                    break;
                case R.id.c4:
                    break;
                case R.id.c5:
                    break;
                case R.id.c6:
                    break;
                case R.id.c7:
                    break;
                case R.id.c8:
                    break;
                case R.id.c9:
                    break;
            }
        }

    }
