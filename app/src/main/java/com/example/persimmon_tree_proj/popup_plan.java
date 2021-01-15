package com.example.persimmon_tree_proj;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.example.Juang_juang.R;

//날짜 선택시 뜨는 팝업 속 일정 바 만들기
public class popup_plan extends LinearLayout{

    public popup_plan(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public popup_plan(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_plan,this,true);
    }
}