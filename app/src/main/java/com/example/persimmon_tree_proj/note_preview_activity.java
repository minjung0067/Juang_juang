package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.example.Juang_juang.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class note_preview_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_activity);

        Intent intent = getIntent();

        TextView note = (TextView) findViewById(R.id.note);

        if (intent != null) {
            Object event = intent.getParcelableExtra(MainActivity.EVENT);

            if(event instanceof MyEventDay){
                MyEventDay myEventDay = (MyEventDay)event;

                getSupportActionBar().setTitle(getFormattedDate(myEventDay.getCalendar().getTime()));
                note.setText(myEventDay.getNote());

                return;
            }

            if(event instanceof EventDay){
                EventDay eventDay = (EventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(eventDay.getCalendar().getTime()));
            }
        }
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}