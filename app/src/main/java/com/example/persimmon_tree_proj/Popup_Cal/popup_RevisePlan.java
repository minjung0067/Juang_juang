package com.example.persimmon_tree_proj.Popup_Cal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.ShareCalendarActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class popup_RevisePlan extends Activity {

    TextView yearmonth_text;
    TextView day_text;
    EditText member_plan;
    TextView member_intro;
    ImageView member_color;

    private String f_code;
    private String day;
    private String month;
    private String year;
    private String user_name;
    private String name;
    private String plan_id;
    private String user_color;
    private String plan_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup__revise_plan);
        //테두리 둥글게 했을 때 뒤에 깔리는 까만 배경 없애기
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //UI 객체생성
        yearmonth_text = (TextView) findViewById(R.id.yearmonth_text);
        day_text = (TextView) findViewById(R.id.day_text);
        member_plan = (EditText) findViewById(R.id.member_plan);
        member_intro = (TextView) findViewById(R.id.member_intro);
        member_color = (ImageView) findViewById(R.id.member_color);


        //데이터 가져오기
        Intent intent = getIntent();
        ArrayList<String> arr = intent.getStringArrayListExtra("arr");
        day = intent.getStringExtra("day");
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        f_code = intent.getStringExtra("f_code");
        user_color = arr.get(0);
        plan_name = arr.get(2);
        user_name = arr.get(1);
        name = arr.get(3);
        plan_id = arr.get(4);


        yearmonth_text.setText(year + "년 " + month + "월 ");
        day_text.setText( day + "일 ");
        member_plan.setText(plan_name);
        member_intro.setText(user_name);
        GradientDrawable gd = (GradientDrawable) member_color.getBackground(); //앞에 뜨는 동그라미 부분 색깔 바꾸기
        gd.setColor(Color.parseColor(user_color));

        //일정 수정하는
        ImageButton yes = (ImageButton) findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("family");
                reference.child(f_code).child("calendar").child(year).child(month).child(day).child(name).child(plan_id).setValue(member_plan.getText().toString());
                Intent intent = new Intent(getApplicationContext(), ShareCalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("f_code",f_code);
                overridePendingTransition(0, 0); //intent시 효과 없애기
                startActivity(intent);
                Toast.makeText(popup_RevisePlan.this, "선택한 일정이 수정 되었다감! ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
    //삭제 취소 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
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