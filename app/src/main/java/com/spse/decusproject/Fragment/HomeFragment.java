package com.spse.decusproject.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spse.decusproject.Adapter.SectionPagerAdapter;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;
import com.spse.decusproject.Login;
import com.spse.decusproject.OCR;
import com.spse.decusproject.PopUpActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    TextView username;
    View myFragment;
    Button scanButton;
    ImageView menuBtn;
    CircleImageView circleImageView;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    StorageReference storageReference;

    private SearchView editsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment =  inflater.inflate(R.layout.fragment_home, container, false);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        username = myFragment.findViewById(R.id.username);
        storageReference = FirebaseStorage.getInstance().getReference();
        circleImageView = myFragment.findViewById(R.id.profileImage);
        menuBtn = myFragment.findViewById(R.id.menuBtn);

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        return myFragment;
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH);
        Date myDate = new Date();
        String sMyDate = "" + sdf.format(myDate);
        return sMyDate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("fName")+"!");
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editsearch = myFragment.findViewById(R.id.search);
        scanButton = myFragment.findViewById(R.id.scan_button);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                CosmeticDatabase database = null;
                try {
                    database = new CosmeticDatabase(s);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(database.getName());
                System.out.println(database.getFunction());
                Intent intent = new Intent(getActivity(), PopUpActivity.class);
                intent.putExtra("NAME", database.getName());
                intent.putExtra("FUNCTION", database.getFunction());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        scanButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OCR.class);
                startActivity(intent);
            } });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.logout:{
                                FirebaseAuth.getInstance().signOut();//logout
                                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                                getActivity().finish();
                            }  return true;

                            case R.id.quit:{
                                getActivity().finish();
                                System.exit(0);
                            } return true;

                            default: return false;
                        }
                    }
                });
                popup.inflate(R.menu.home_menu);
                popup.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                circleImageView.setImageURI(imageUri);

            }
        }

    }


}