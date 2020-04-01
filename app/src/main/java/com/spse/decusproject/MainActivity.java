package com.spse.decusproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    TextView fullName,email,changePassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        changePassword = findViewById(R.id.changePassword);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home :
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_log :
                            selectedFragment = new LogFragment();
                            break;
                        case R.id.nav_profile: {
                            selectedFragment = new ProfileFragment();
                            break;}
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


    public void changePassword(View view) {
      Intent i=new Intent(getApplicationContext(),PopActivity.class);
       startActivity(i);
    }

    public void changeEmail(View view) {
    }
}