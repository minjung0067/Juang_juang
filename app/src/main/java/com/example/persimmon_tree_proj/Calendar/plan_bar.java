package com.example.persimmon_tree_proj.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.example.Juang_juang.R;

////캘린더 페이지 속 모든 가족 감 프로필 불러오기 위해 동적으로 생성하는 레이아웃
//public class plan_bar extends LinearLayout{
//
//    public plan_bar(Context context) {
//        super(context);
//
//        init(context);
//    }
//    private void init(Context context){
//        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.plan_bar,this,true);
//    }
//}

public class plan_bar {
    private String coloricon ;
    private String plan_name ;

    public void setIcon(String icon) {
        coloricon = icon;
    }
    public void setPlan_name(String planname) {
        plan_name = planname ;
    }


    public String getIcon() {
        return this.coloricon ;
    }
    public String getPlan_name() {
        return this.plan_name ;
    }
}