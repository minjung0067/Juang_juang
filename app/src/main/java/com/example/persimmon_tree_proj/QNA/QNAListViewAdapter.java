package com.example.persimmon_tree_proj.QNA;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.Family.ListViewAdapter;
import com.example.persimmon_tree_proj.Family.ListViewItem;

import java.util.ArrayList;
import java.util.List;

public class QNAListViewAdapter extends BaseAdapter {

    ListView listView;
    ArrayAdapter<String> adapter;
    private TextView Number;
    private TextView line;
    private TextView question;
    private ImageButton gotoQNA;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<QNAListViewItem> listViewItemList = new ArrayList<QNAListViewItem>();

    //ListViewAdapter의 생성자
    public QNAListViewAdapter(){

    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }


    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        Context context = parent.getContext();

        // qlistview_item Layout을 inflate하여 convertView참조 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.qlistview_item,parent,false);

            //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            Number = (TextView) convertView.findViewById(R.id.qlist_questionnum);
            line= (TextView) convertView.findViewById(R.id.thinline);
            question= (TextView) convertView.findViewById(R.id.qlist_question);
            gotoQNA = (ImageButton) convertView.findViewById(R.id.qlist_gotoQnA);

            QNAListViewItem listViewItem = listViewItemList.get(position);

            Number.setText(listViewItem.getNum());
            question.setText(listViewItem.getContent());

        }
        return null;
    }

    public void addItem(String num, String question){
        ListViewItem item = new ListViewItem();



    }
}