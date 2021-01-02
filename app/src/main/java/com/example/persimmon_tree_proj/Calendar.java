package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Calendar extends AppCompatActivity {
    private LinearLayout layout;
    private ImageView iv;
    private String count;
    private String[] gam_arr = {"","","","",""};
    private String[] color_arr = {"","","","",""};
    private int index1 = 0;
    private int index2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ImageView iv = new ImageView(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");

                reference1.child(myfcode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count = dataSnapshot.child("count").getValue(String.class);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

                reference1.child(myfcode).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> members = dataSnapshot.getChildren().iterator();
                        do{
                            String color_number = members.next().child("user_color").getValue(String.class);
                            color_arr[index1++] = color_number;
                            Log.i("lalatoooindex11", String.valueOf(index1));
                        }
                        while(members.hasNext());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

                reference1.child(myfcode).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> members = dataSnapshot.getChildren().iterator();
                        do{
                            String gam_number = members.next().child("user_gam").getValue(String.class);
                            gam_arr[index2++] = gam_number;
                            Log.i("lalatoooindex22", String.valueOf(index2));
                        }
                        while(members.hasNext());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });



        ImageButton go_main = (ImageButton) findViewById(R.id.main_btn); //왔다감 버튼
        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 왔다감으로 이동
                Intent intent = new Intent(Calendar.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton go_calendar = (ImageButton) findViewById(R.id.calender_btn); //캘린더 버튼
        go_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //누르면 캘린더 새로고침
                Intent intent = new Intent(Calendar.this, Calendar.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //        // imageView가 추가될 linearLayout
    public void addview(int index){
        LinearLayout linear = (LinearLayout)findViewById(R.id.calender);
        Log.i("toooindex infront ", String.valueOf(index2));
        for(int i=0; i<=index1; i++) {
            Log.i("tooo2", color_arr[index1]);
            Log.i("tooo2", gam_arr[index1]);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.width = 70;
            param.height = 70;
            param.setMargins(0,0,0,0);
            Log.i("lalatooo", gam_arr[index1]);
            switch (gam_arr[i]) {
                case "1":
                    iv.setImageResource(R.drawable.gam1);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "2":
                    iv.setImageResource(R.drawable.gam2);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "3":
                    iv.setImageResource(R.drawable.gam3);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "4":
                    iv.setImageResource(R.drawable.gam4);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "5":
                    iv.setImageResource(R.drawable.gam5);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "6":
                    iv.setImageResource(R.drawable.gam6);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "7":
                    iv.setImageResource(R.drawable.gam7);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                case "8":
                    iv.setImageResource(R.drawable.gam8);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
                default:
                    iv.setImageResource(R.drawable.gam1);
                    linear.addView(iv,param); // 기존 linearLayout에 imageView 추가
            }
        }
    }

}
