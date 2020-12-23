package com.example.persimmon_tree_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class Make_FamilyProfile extends AppCompatActivity {

    private StorageReference mStorageRef; //이미지 구글 firebase storage에 업로드 하기 위함임
    Button ok; //확인버튼
    private EditText counts; //가족 몇명
    private EditText about_familys; //가족 이름
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseAuth firebaseAuth; //파이어베이스 인증 객체 생성
    private String fcount;
    private String about_myfamily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make__familyprofile);

        counts = (EditText) findViewById(R.id.count);
        about_familys = (EditText) findViewById(R.id.about_family);

        fcount = counts.getText().toString();
        about_myfamily = about_familys.getText().toString();


        //확인 버튼 누르면 main으로
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Make_FamilyProfile.this, MakeProfile.class);
                startActivity(intent);
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String myfcode = dataSnapshot.child("fcode").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("count").setValue(fcount);
                        FirebaseDatabase.getInstance().getReference("family").child(myfcode).child("family_name").setValue(about_myfamily);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

            }
        });
    }
}




