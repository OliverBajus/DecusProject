package com.spse.decusproject;

    import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

    import com.example.decus.R;
    import com.google.firebase.auth.FirebaseAuth;

    import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

        EditText name,email,password;
        Button btnRegister;
        TextView mLinkLogin;
        FirebaseAuth fAuth;
        ProgressBar progressBar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);





        }


}
