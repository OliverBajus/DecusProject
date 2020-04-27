package com.spse.decusproject.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;
import com.spse.decusproject.Activity.Login;
import com.spse.decusproject.Activity.OCR;
import com.spse.decusproject.PopUp.PopUpActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private TextView username;
    private View myFragment;
    private Button scanButton;
    private ImageView menuBtn;
    private CircleImageView circleImageView;

    private SearchView editsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment =  inflater.inflate(R.layout.fragment_home, container, false);
        findViews();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return myFragment;
    }

    private void findViews() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        username = myFragment.findViewById(R.id.username);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        circleImageView = myFragment.findViewById(R.id.profileImage);
        menuBtn = myFragment.findViewById(R.id.menuBtn);
        editsearch = myFragment.findViewById(R.id.search);
        scanButton = myFragment.findViewById(R.id.scan_button);

        StorageReference profileRef = storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
            }
        });

        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("fName")+"!");
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CosmeticDatabase database = null;
                try {
                    database = new CosmeticDatabase(s);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                assert database != null;
                if(database.getFunction() != null ){
                    Intent intent = new Intent(getActivity(), PopUpActivity.class);
                    intent.putExtra("NAME", database.getName());
                    intent.putExtra("FUNCTION", database.getFunction());
                    startActivity(intent);
                    return false;
                }else {
                    Toast.makeText(getActivity(), "Ingredient not found", Toast.LENGTH_SHORT).show();
                }
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
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.logout:{
                                FirebaseAuth.getInstance().signOut(); //logout
                                startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), Login.class));
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
                assert data != null;
                Uri imageUri = data.getData();

                circleImageView.setImageURI(imageUri);

            }
        }



    }


}