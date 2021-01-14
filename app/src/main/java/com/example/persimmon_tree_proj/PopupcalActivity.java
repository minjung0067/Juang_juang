package com.example.persimmon_tree_proj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.Juang_juang.R;

public class PopupcalActivity extends Activity {

    TextView day_text;
    TextView yearmonth_text;
    private String f_code;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popupcal);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //UI 객체생성
        yearmonth_text = (TextView)findViewById(R.id.yearmonth_text);
        day_text = (TextView)findViewById(R.id.day_text);

        //데이터 가져오기
        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String year = intent.getStringExtra("year");
        String month = intent.getStringExtra("month");
        f_code = intent.getStringExtra("f_code");

        //년 월 보여주기
        yearmonth_text.setText( year + "년 " + month + "월 ");
        //날짜 보여주기
        day_text.setText( day +"일");
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
//        Intent intent = new Intent(getApplicationContext(),ShareCalendarActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("f_code",f_code);
//        startActivity(intent);
        //일정은 그냥 창만 닫으면 되는 거라 주석처리 했어용
        finish();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
