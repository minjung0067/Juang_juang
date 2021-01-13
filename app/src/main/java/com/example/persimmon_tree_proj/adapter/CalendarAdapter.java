package com.example.persimmon_tree_proj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.Juang_juang.R;
import com.example.persimmon_tree_proj.domain.DayInfo;
import com.example.persimmon_tree_proj.plan_bar;
import com.example.persimmon_tree_proj.sub_answer;


/**
 * BaseAdapter를 상속받아 구현한 CalendarAdapter
 *
 * @author croute
 * @since 2011.03.08
 */
public class CalendarAdapter extends BaseAdapter
{
    private final ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;
    private ArrayList<String> mwhen_whos_what_plan_arr;
    private HashMap<String,String> mname_color_map;
    private int mdayOfMonth;
    /**
     * Adpater 생성자
     *
     * @param context
     *            컨텍스트
     * @param textResource
     *            레이아웃 리소스
     * @param dayList
     *            날짜정보가 들어있는 리스트
     */
    public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList, ArrayList<String> when_whos_what_plan_arr, HashMap name_color_map, int dayOfMonth)
    {
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mwhen_whos_what_plan_arr = when_whos_what_plan_arr;
        this.mname_color_map = name_color_map;
        this.mdayOfMonth = dayOfMonth;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mDayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DayInfo day = mDayList.get(position);

        DayViewHolde dayViewHolder;


        if(convertView == null)
        {
            convertView = mLiInflater.inflate(mResource, null);

            if(position % 7 == 6)
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
            }
            else
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
            }


            dayViewHolder = new DayViewHolde();

            dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_cell_ll_background);
            dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);
            dayViewHolder.containers = (LinearLayout) convertView.findViewById(R.id.plan_view);
//일정이 있는 부분에 bar 추가하는 부분 !!!!!!!!!
            int when_index=0;
            dayViewHolder.containers.removeAllViewsInLayout();  //한번 싹 지우고
            int size = mwhen_whos_what_plan_arr.size();
            while(when_index > size){
//                    dayViewHolder.containers.setHasTransientState(true);
//                    Log.i("size nono",String.valueOf(mwhen_whos_what_plan_arr.get(when)));
                int day_num = (position - mdayOfMonth + 2); //날짜 번호
//                    Log.i("size nono day_num",String.valueOf(day_num));
                if(mwhen_whos_what_plan_arr.get(when_index).equals(String.valueOf(day_num))){  //그 position에 일정이 있으면
                    //해당 dayViewHolder.에 동적 view추가
                    //arr에 [날짜, 일정 주인이름, 일정이름] 이렇게 들어가 있음
                    //when에 해당하는 게 날짜(3개씩 건너뜀), when+1에 해당하는게 사람 이름, when+2에 해당하는게 일정이름
                    plan_bar bar = new plan_bar(mContext);  //동적 layout 생성
                    TextView plans = bar.findViewById(R.id.plan);  //각각 ID 찾아서
                    //i+1값으로 hashmap에  접근해서 해당 user의 색깔로 바 만듦
                    Button plan = new Button(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    plan.setLayoutParams(params);
                    plan.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    plan.setTextSize(8);
                    plan.setBackgroundColor(Color.parseColor("#808080")); //테두리 drawable
                    plan.setText(mwhen_whos_what_plan_arr.get(when_index+2));//그 bar의 text는 i+2
                    dayViewHolder.containers.addView(plan);
                    mwhen_whos_what_plan_arr.remove(when_index);  //날짜 지우기
                    mwhen_whos_what_plan_arr.remove(when_index); //이름 지우기 지워지니까 index그대로 when
                    mwhen_whos_what_plan_arr.remove(when_index); //일정 지우기
                    //세 값 삭제 해 줘야함
//                        dayViewHolder.containers.setHasTransientState(false);
                }
                else {
                    when_index += 3; //없으면 그냥 세 칸씩 건너 뛰어야함
                    //날짜 , 이름 , 일정 이렇게 3개가 한 세트라서!
                }
            }

            convertView.setTag(dayViewHolder);
        }
        else
        {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }

        if(day != null)
        {
            dayViewHolder.tvDay.setText(day.getDay());

            if(day.isInMonth())
            {
                                if(position % 7 == 0)
                {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6)
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                }
                else
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                }
            }
            else
            {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
            }

        }

        return convertView;
    }


    public class DayViewHolde
    {
        public LinearLayout llBackground;
        public TextView tvDay;
        public LinearLayout containers;
    }

    private int getCellWidthDP()
    {
      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        //int cellWidth = 720/7;
        int cellWidth = width/7;


        return cellWidth;
    }

    private int getRestCellWidthDP()
    {
      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        //int cellWidth = 720%7;
        int cellWidth = width%7;


        return cellWidth;
    }

    private int getCellHeightDP()
    {
       int height = mContext.getResources().getDisplayMetrics().heightPixels;
        //int cellHeight = 1280/6;
        int cellHeight = height/10;

        return cellHeight;
    }

}
