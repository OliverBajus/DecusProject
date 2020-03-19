package com.spse.decusproject;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
    import android.widget.Toast;

    import com.example.decus.R;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

        EditText xName,xEmail,xPassword;
        Button xBtnRegister;
        TextView xLinkLogin;
        FirebaseAuth fAuth;
        ProgressBar progressBar;



    @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            xName=findViewById(R.id.txtName);
            xEmail=findViewById(R.id.txtEmail);
            xPassword=findViewById(R.id.txtPwd);
            xBtnRegister=findViewById(R.id.btnRegister);
            xLinkLogin=findViewById(R.id.lnkLogin);

            fAuth=FirebaseAuth.getInstance();
            progressBar=findViewById(R.id.progressBar);

            if (fAuth.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
            xBtnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String email=xEmail.getText().toString().trim();
                  String password=xPassword.getText().toString().trim();

                  if (TextUtils.isEmpty(email)){
                      xEmail.setError("Email is required");
                      return;
                  }

                  if (TextUtils.isEmpty(password)){
                      xPassword.setError("Password is required");
                      return;
                  }

                  if (password.length()< 6){
                      xPassword.setError("Password must have at least 6 characters");
                      return;
                  }


                  progressBar.setVisibility(View.VISIBLE);

                  //Registracia usera

                   fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(Register.this, "User created",Toast.LENGTH_LONG).show();
                               startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           }
                           else Toast.makeText(Register.this,"ERROR!!" + task.getException().getMessage(),Toast.LENGTH_LONG).show();

                       }
                   });

                }
            });







        }


}
