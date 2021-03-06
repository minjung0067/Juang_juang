package com.example.persimmon_tree_proj.Mypage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class MakeProfilemain extends AppCompatActivity {

    ImageView imageView;
    ImageButton change_btn; //사진 바꾸기버튼
    private StorageReference mStorageRef; //이미지 구글 firebase storage에 업로드 하기 위함임
    Button ok; //확인버튼
    private EditText whoami; //한줄 소개 칸
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String myfcode;
    private String user_name;
    private String gam_number;
    private String color_number;
    private String introduce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profilemain);

//        Button go_back = (Button) findViewById(R.id.go_back);    //뒤로가기
//        go_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MakeProfile.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기

        whoami = (EditText) findViewById(R.id.whoami);

        //확인 버튼 누르면 mypage으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introduce = whoami.getText().toString();
                if(introduce.getBytes().length <= 0){//빈값이 넘어올때의 처리

                    Toast.makeText(MakeProfilemain.this, "한 줄 소래를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    mDatabase = FirebaseDatabase.getInstance();
                    //입력한 한줄소개 현재 로그인한 사람 uid 통해서 그 사람 introduce에 넣기
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                    mDatabase.getReference("users").child(user.getUid()).child("introduce").setValue(introduce); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기
                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myfcode = dataSnapshot.child("fcode").getValue(String.class);
                            user_name = dataSnapshot.child("name").getValue(String.class);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user.getUid()).child("introduce").setValue(introduce);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });

        imageView = (ImageView) findViewById(R.id.profile_image);
        change_btn = (ImageButton) findViewById(R.id.ok_btn);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                introduce = whoami.getText().toString();
                if(introduce.getBytes().length <= 0){//빈값이 넘어올때의 처리
                    Toast.makeText(MakeProfilemain.this, "한 줄 소래를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    mDatabase = FirebaseDatabase.getInstance();
                    //입력한 한줄소개 현재 로그인한 사람 uid 통해서 그 사람 introduce에 넣기
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                    mDatabase.getReference("users").child(user.getUid()).child("introduce").setValue(introduce); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기
                    reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myfcode = dataSnapshot.child("fcode").getValue(String.class);
                            user_name = dataSnapshot.child("name").getValue(String.class);
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user.getUid()).child("introduce").setValue(introduce);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }



                //Intent intent = new Intent(getApplicationContext(), profile_gam_main.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                //finish();
            }
        });

        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myfcode = dataSnapshot.child("fcode").getValue(String.class);
                user_name = dataSnapshot.child("name").getValue(String.class);
                introduce = dataSnapshot.child("introduce").getValue(String.class);
                if (introduce != null){
                    whoami.setText(introduce);
                }
                DatabaseReference reference_family = FirebaseDatabase.getInstance().getReference("family");
                reference_family.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        color_number = dataSnapshot.child(myfcode).child("members").child(user.getUid()).child("user_color").getValue(String.class);
                        gam_number = dataSnapshot.child(myfcode).child("members").child(user.getUid()).child("user_gam").getValue(String.class);
                        if(gam_number != null && color_number!=null){
                            switch (gam_number) {
                                case "1":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd1 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd1.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam1);
                                    break;
                                case "2":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd2 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd2.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam2);
                                    break;
                                case "3":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd3 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd3.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam3);
                                    break;
                                case "4":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd4 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd4.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam4);
                                    break;
                                case "5":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd5 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd5.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam5);
                                    break;
                                case "6":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd6 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd6.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam6);
                                    break;
                                case "7":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd7 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd7.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam7);
                                    break;
                                case "8":
                                    imageView.setBackgroundResource(R.drawable.profile_outline); //테두리 drawable
                                    GradientDrawable gd8 = (GradientDrawable) imageView.getBackground(); //동적으로 테두리 색 바꿈
                                    gd8.setStroke(200, Color.parseColor(color_number)); //배열에 담긴 색깔로 테두리 설정
                                    imageView.setImageResource(R.drawable.gam8);
                                    break;
                                default:
                                    imageView.setBackgroundColor(Color.parseColor("#fff"));
                                    imageView.setImageResource(R.drawable.gam1);
                                    break;
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }


                });}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MypageActivity.class); //코드 생성 activity로 이동
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        //안드로이드 백버튼 막기
        return;
    }
}
//갤러리 열기
//        imageView = (ImageView)findViewById(R.id.profile_image);
//        change_photo_btn = (Button)findViewById(R.id.change_photo_btn); //사진 바꾸는 +버튼 누르면
//        change_photo_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                //아래 3줄 갤러리 열 때 사용 !!
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//            }
//        });
//        //이미지 데이터 베이스 삽입
//        mStorageRef = FirebaseStorage.getInstance().getReference();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //위의 startactivity for result 함수랑 이어짐
//        // Check which request we're responding to
//        if (requestCode == 1) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                try {// 선택한 이미지에서 비트맵 생성
//                    //data.getData()로 받은 것은 파일 주소
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//                    // 이미지가 너무 크면 못 불러오니까 사이즈를 줄임
//                    int nh = (int) (img.getHeight() * (1024.0 / img.getWidth()));
//                    Bitmap scaled = Bitmap.createScaledBitmap(img, 1024, nh, true);
//                    // 감 그림 대신 선택한 이미지를 imageview 에 띄우기
//                    imageView.setImageBitmap(img);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }