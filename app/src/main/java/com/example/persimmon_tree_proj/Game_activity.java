package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Main.MainActivity;

public class Game_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

        Intent intent = getIntent();
        final String f_code = intent.getStringExtra("f_code");
        Log.i("myfcode", f_code);


        //뒤로가기
        ImageButton goback = (ImageButton)findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intentt = new Intent(getApplicationContext(), MainActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.putExtra("f_code",f_code);
                startActivity(intentt);
            }
        });
    }
}