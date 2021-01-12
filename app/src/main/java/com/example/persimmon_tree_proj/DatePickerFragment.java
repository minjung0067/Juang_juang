//package com.example.persimmon_tree_proj;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import android.widget.DatePicker;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
//import com.applikeysolutions.cosmocalendar.view.CalendarView;
//import com.example.Juang_juang.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link DatePickerFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
///* 이건 range할려면 이거가져와야되나 싶어서 가져온거 ! 지워도됨
//        List<Calendar> days = calendarView.getSelectedDates();
//        String result="";
//        for( int i=0; i<days.size(); i++)
//        {
//
//            java.util.Calendar calendar = days.get(i);
//            final int day = calendar.get(calendar.DAY_OF_MONTH);
//            final int month = calendar.get(calendar.MONTH);
//            final int year = calendar.get(calendar.YEAR);
//            String week = new SimpleDateFormat("EE").format(calendar.getTime());
//            String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";
//            result += (day_full + "\n");
//        }
//        Toast.makeText(Calendar_activity.this, result, Toast.LENGTH_LONG).show();
// */
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        return new DatePickerDialog(getActivity(),this,year,month,day);
//    }
//
//    @Override
//    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//        Calendar_activity activity = (Calendar_activity) getActivity();
//        activity.processDatePickerResult(year,month,day);
//    }
//}