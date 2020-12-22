package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


public class MakeProfile extends AppCompatActivity {

    ImageView imageView;
    Button change_photo_btn; //사진 바꾸기버튼
    private StorageReference mStorageRef; //이미지 구글 firebase storage에 업로드 하기 위함임
    Button ok; //확인버튼
    private EditText whoami; //한줄 소개 칸
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);
        whoami = (EditText) findViewById(R.id.whoami);

        //확인 버튼 누르면 main으로
        ok = (Button)findViewById(R.id.ok_btn);
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

        imageView = (ImageView)findViewById(R.id.profile_image);
        change_photo_btn = (Button)findViewById(R.id.change_photo_btn); //사진 바꾸는 +버튼 누르면
        change_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //아래 3줄 갤러리 열 때 사용 !!
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        //이미지 데이터 베이스 삽입
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //위의 startactivity for result 함수랑 이어짐
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {// 선택한 이미지에서 비트맵 생성
                    //data.getData()로 받은 것은 파일 주소
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지가 너무 크면 못 불러오니까 사이즈를 줄임
                    int nh = (int) (img.getHeight() * (1024.0 / img.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(img, 1024, nh, true);
                    // 감 그림 대신 선택한 이미지를 imageview 에 띄우기
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //프로필 이미지는 firebase storage에,
    //그리고 그 프로필 이미지의 url을 user의 회원 정보 속 프로필 사진가지에 추가
    //한 줄 소개도 같이 추가



}