package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.Juang_juang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class Make_FamilyProfile extends AppCompatActivity {

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
        setContentView(R.layout.activity_make__familyprofile);

        whoami = (EditText) findViewById(R.id.whoami);

        //확인 버튼 누르면 main으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Make_FamilyProfile.this, MakeProfile.class);
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
    }
}




