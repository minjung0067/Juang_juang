package com.example.persimmon_tree_proj.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.example.Juang_juang.R;

//캘린더 페이지 속 모든 가족 감 프로필 불러오기 위해 동적으로 생성하는 레이아웃
public class Sub extends LinearLayout{

        public Sub(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public Sub(Context context) {
            super(context);

            init(context);
        }
        private void init(Context context){
            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.sub,this,true);
        }
    }