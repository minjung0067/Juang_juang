package com.example.persimmon_tree_proj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import com.example.Juang_juang.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MakeProfile extends AppCompatActivity {

    ImageView imageView;
    Button button;
    private StorageReference mStorageRef; //이미지 구글 firebase storage에 업로드 하기 위함임

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);
        imageView = (ImageView)findViewById(R.id.profile_image);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //갤러리 열 때 사용 !!
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        //이미지 데이터 베이스 삽입
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    // 감 그림 대신 선택한 이미지 표시
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