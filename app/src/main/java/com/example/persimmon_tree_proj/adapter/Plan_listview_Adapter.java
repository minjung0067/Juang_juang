package com.example.persimmon_tree_proj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.popup_plan;

import java.util.ArrayList;

public class Plan_listview_Adapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<popup_plan> listViewItemList = new ArrayList<popup_plan>() ;

    // ListViewAdapter의 생성자
    public Plan_listview_Adapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popup_plan, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView color_icon = (ImageView) convertView.findViewById(R.id.member_color) ;
        TextView introTextView = (TextView) convertView.findViewById(R.id.member_intro) ;
        TextView planTextView = (TextView) convertView.findViewById(R.id.member_plan) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        popup_plan listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        GradientDrawable gd = (GradientDrawable) color_icon.getBackground(); //앞에 뜨는 동그라미 부분 색깔 바꾸기
        if((listViewItem.getIcon().equals("") == false)&&(listViewItem.getNick_name().equals("") == false)){
            //일정 있는 날
            gd.setColor(Color.parseColor(listViewItem.getIcon()));
            introTextView.setText(listViewItem.getNick_name());
            planTextView.setText(listViewItem.getPlan_name());
        }
        else{
            //일정 없는 날
            gd.setColor(Color.parseColor("#ffffff"));
            if(listViewItem.getPlan_name().equals("") == false){
                planTextView.setText(listViewItem.getPlan_name());
                planTextView.setTextColor(Color.parseColor("#B9B3BD"));
                planTextView.setGravity(Gravity.CENTER_VERTICAL);
                planTextView.setHeight(200);
                planTextView.setTextSize(21);
            }
        }

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String color, String intro, String desc,String username, String planid) {
        popup_plan item = new popup_plan();

        item.setIcon(color);
        item.setNick_name(intro);
        item.setPlan_name(desc);
        item.setUser_name(username);
        item.setPlan_id(planid);

        listViewItemList.add(item);
    }
}
