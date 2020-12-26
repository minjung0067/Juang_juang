package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.Juang_juang.R;

public class profile_gam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gam);
        Button color = (Button)findViewById(R.id.color);   //색상 버튼
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_gam.this, profile_color.class);
                startActivity(intent);
            }
        });
        ImageButton go_back = (ImageButton)findViewById(R.id.go_back);  //뒤로가기 버튼
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_gam.this, MypageActivity.class);
                startActivity(intent);
            }
        });

    }

    public void Click(View view) {  //버튼 클릭시마다 switch문으로 다른 이벤트
        switch (view.getId())
        {
            case R.id.gam1:
                break;
            case R.id.gam2:
                break;
            case R.id.gam3:
                break;
            case R.id.gam4:
                break;
            case R.id.gam5:
                break;
            case R.id.gam6:
                break;
            case R.id.gam7:
                break;
            case R.id.gam8:
                break;
        }
    }
}
