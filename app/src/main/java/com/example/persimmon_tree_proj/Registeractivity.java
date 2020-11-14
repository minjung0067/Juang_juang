package com.example.persimmon_tree_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registeractivity extends AppCompatActivity {
    private static final String TAG = "Registeractivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override

            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_register:
                        signUp();
                        break;
                }
            }
    };

    private void signUp() {
        String email = ((EditText)findViewById(R.id.edit_id)).getText().toString();
        String password = ((EditText)findViewById(R.id.edit_pwd)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Registeractivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Registeractivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // ...
                    }
                });
    }
}