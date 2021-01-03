package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.CamcorderProfile.get;

public class Calendar extends AppCompatActivity {
    private Context context;
    private int count=0;
    private int count_gam=0;
    private int count_color=0;
    private int count_introduce=0;
    private LinearLayout container;
    private ArrayList<String> gam_arr = new ArrayList<String>();
    private ArrayList<String> color_arr = new ArrayList<String>();
    private ArrayList<String> introduce_arr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        container = findViewById(R.id.con); //가족들 프로필 보여줄 layout
        context = this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("family");

                reference1.child(myfcode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //현재 가족으로 묶인 member 수 세기
                        Iterator<DataSnapshot> members = snapshot.child("members").getChildren().iterator();
                        while (members.hasNext()){
                            String member_num = members.next().getKey();
                            count++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

                //현재 구성원들 데이터베이스 하나씩 돌면서 color gam introduce 각각 배열에 저장 -> 따로 해야함
                reference1.child(myfcode).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> members1 = dataSnapshot.getChildren().iterator();
                        while (members1.hasNext()) {
                            String color_number = members1.next().child("user_color").getValue(String.class);
                            if (color_number != null) {
                                color_arr.add(count_color++,color_number);
                                Log.i("lala color ", color_arr.get(count_color-1));
                            }
                        }
                        Iterator<DataSnapshot> members2 = dataSnapshot.getChildren().iterator();
                        while (members2.hasNext()) {
                            String gam_number = members2.next().child("user_gam").getValue(String.class);
                            if (gam_number != null) {
                                gam_arr.add(count_gam++,gam_number);
                                Log.i("lala gam ", gam_arr.get(count_gam-1));
                            }
                        }
                        Iterator<DataSnapshot> members3 = dataSnapshot.getChildren().iterator();
                        while (members3.hasNext()) {
                            String introduce = members3.next().child("introduce").getValue(String.class);
                            if (introduce != null) {
                                introduce_arr.add(count_introduce++,introduce);
                                Log.i("lala introduce", introduce_arr.get(count_introduce-1));
                            }

                        }
                        //저장해 준 것들 하나씩 꺼내서 캘린더 상단에 모든 가족 구성원 프로필 표시
                        //위에서 셌던 현재 구성원 수만큼 동적으로 layout 생성하고 각각 view 지정 다르게
                        for(int i=0; i<count; i++){
                            String gamgam = gam_arr.get(i); //감 프로필 사진 번호가 뭐냐에 따라 나눴삼
                            if(gamgam.equals("1")) {
                                Sub n_layout1 = new Sub(getApplicationContext());  //동적 layout 생성
                                ImageView iv = n_layout1.findViewById(R.id.gam_image);
                                TextView user_name = n_layout1.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam1);  //이미지
                                iv.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                GradientDrawable gd1 = (GradientDrawable) iv.getBackground();
                                gd1.setStroke(23,Color.parseColor(color_arr.get(i))); //배열에 담긴 색깔 테두리 설정
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout1); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("2")) {  //이하동일
                                Sub n_layout2 = new Sub(getApplicationContext());
                                ImageView iv = n_layout2.findViewById(R.id.gam_image);
                                TextView user_name = n_layout2.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam2);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd2 = (GradientDrawable) iv.getBackground();
                                gd2.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout2); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("3")) {
                                Sub n_layout3 = new Sub(getApplicationContext());
                                ImageView iv = n_layout3.findViewById(R.id.gam_image);
                                TextView user_name = n_layout3.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam3);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd3 = (GradientDrawable) iv.getBackground();
                                gd3.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout3); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("4")) {
                                Sub n_layout4 = new Sub(getApplicationContext());
                                ImageView iv = n_layout4.findViewById(R.id.gam_image);
                                TextView user_name = n_layout4.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam4);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd4 = (GradientDrawable) iv.getBackground();
                                gd4.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout4); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("5")) {
                                Sub n_layout5 = new Sub(getApplicationContext());
                                ImageView iv = n_layout5.findViewById(R.id.gam_image);
                                TextView user_name = n_layout5.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam5);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd5 = (GradientDrawable) iv.getBackground();
                                gd5.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout5); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("6")) {
                                Sub n_layout6 = new Sub(getApplicationContext());
                                ImageView iv = n_layout6.findViewById(R.id.gam_image);
                                TextView user_name = n_layout6.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam6);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd6 = (GradientDrawable) iv.getBackground();
                                gd6.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout6); // 기존 linearLayout에 imageView 추가
                            }
                            else if(gamgam.equals("7")) {
                                Sub n_layout7 = new Sub(getApplicationContext());
                                ImageView iv = n_layout7.findViewById(R.id.gam_image);
                                TextView user_name = n_layout7.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam7);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd7 = (GradientDrawable) iv.getBackground();
                                gd7.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout7); // 기존 linearLayout에 imageView 추가
                            }
                            else {
                                Sub n_layout8 = new Sub(getApplicationContext());
                                ImageView iv = n_layout8.findViewById(R.id.gam_image);
                                TextView user_name = n_layout8.findViewById(R.id.user_name);
                                iv.setImageResource(R.drawable.gam8);
                                iv.setBackgroundResource(R.drawable.profile_outline);
                                GradientDrawable gd8 = (GradientDrawable) iv.getBackground();
                                gd8.setStroke(23,Color.parseColor(color_arr.get(i)));
                                user_name.setText(introduce_arr.get(i));
                                container.addView(n_layout8); // 기존 linearLayout에 imageView 추가
                            }
                        }
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

}
