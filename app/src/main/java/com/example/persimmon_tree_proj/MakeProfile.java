package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


public class MakeProfile extends AppCompatActivity {

    ImageView imageView;
    Button change_btn; //사진 바꾸기버튼
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);

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

        //확인 버튼 누르면 main으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                startActivity(intent);
                String introduce = whoami.getText().toString();
                mDatabase = FirebaseDatabase.getInstance();
                //입력한 한줄소개 현재 로그인한 사람 uid 통해서 그 사람 introduce에 넣기
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");  //users에서 현 uid 가진 사람 찾기
                mDatabase.getReference("users").child(user.getUid()).child("introduce").setValue(introduce); //database user의 정보 부분에 한줄 소개 내용 덮어쓰기

            }
        });

        imageView = (ImageView) findViewById(R.id.profile_image);
        change_btn = (Button) findViewById(R.id.change_profile);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeProfile.this, profile_gam.class);
                startActivity(intent);
            }
        });

        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myfcode = dataSnapshot.child("fcode").getValue(String.class);
                user_name = dataSnapshot.child("name").getValue(String.class);
                DatabaseReference reference_family = FirebaseDatabase.getInstance().getReference("family");
                reference_family.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        color_number = dataSnapshot.child(myfcode).child("members").child(user_name).child("user_color").getValue(String.class);
                        gam_number = dataSnapshot.child(myfcode).child("members").child(user_name).child("user_gam").getValue(String.class);

                        //나중에 주석 처리해서 지울 부분
                        if (gam_number == null){
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_gam").setValue("1");
                        }
                        if (color_number == null){
                            FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("members").child(user_name).child("user_color").setValue("#9FFFBB33");
                        }
                        // 나중엔 여기까지 지우기 !

                        switch (gam_number) {
                            case "1":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam1);
                                break;
                            case "2":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam2);
                                break;
                            case "3":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam3);
                                break;
                            case "4":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam4);
                                break;
                            case "5":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam5);
                                break;
                            case "6":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam6);
                                break;
                            case "7":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam7);
                                break;
                            case "8":
                                imageView.setBackgroundColor(Color.parseColor(color_number));
                                imageView.setImageResource(R.drawable.gam8);
                                break;
                            default:
                                imageView.setBackgroundColor(Color.parseColor("#ffffff"));
                                imageView.setImageResource(R.drawable.gam1);
                                break;
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
