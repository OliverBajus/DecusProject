package com.spse.decusproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FotgotPasswordActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText email;
    Button sendReset,goBack;


    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotgot_password);



        progressBar=findViewById(R.id.progressBar);
        email=findViewById(R.id.emailForReset);
        sendReset=findViewById(R.id.btnSendReset);
        goBack=findViewById(R.id.goBackBtn);

        firebaseAuth= FirebaseAuth.getInstance();

        sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String userEmail = email.getText().toString().trim();

                if(TextUtils.isEmpty(userEmail)){
                    email.setError("Email is Required.");
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(FotgotPasswordActivity.this,"We sent you password reset email",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                }
                                else  Toast.makeText(FotgotPasswordActivity.this,"Error!!",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
