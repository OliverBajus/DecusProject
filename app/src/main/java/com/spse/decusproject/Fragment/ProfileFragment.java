package com.spse.decusproject.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.decus.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spse.decusproject.Adapter.SectionPagerAdapter;
import com.spse.decusproject.Login;
import com.spse.decusproject.Product;
import com.spse.decusproject.Fragment.ProductsViewHolder;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    TextView fullName,email,verificationTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    Button changeProfileImg;
    ImageView settingsImg,verifyIcon;
    CircleImageView profileImage;

    DatabaseReference databaseProducts;
    FirebaseFirestore firebaseFirestore;
    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter;

    StorageReference storageReference;

    Query databaseProductsQuery;

    ViewPager viewPager;
    TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);

        if (!user.isEmailVerified()){
            verifyIcon.setVisibility(View.VISIBLE);
            verificationTxt.setVisibility(View.VISIBLE);

            verificationTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),"Verification email has been sent",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Error!!"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                System.out.println("tu");
            }
        });

        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(user.getEmail());
            }
        });


        settingsImg.setOnClickListener(new View.OnClickListener() {
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

                            case R.id.changeEmail:{
                                final EditText resetEmail = new EditText(v.getContext());
                                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                                passwordResetDialog.setTitle("Reset email ?");
                                passwordResetDialog.setMessage("Enter your new email.");
                                passwordResetDialog.setView(resetEmail);

                                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // extract the email and send reset link
                                        final String newEmail = resetEmail.getText().toString();
                                        user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(),"Email changed successfully",Toast.LENGTH_LONG).show();
                                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(),"Verification email has been sent",Toast.LENGTH_LONG).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(),"Error!!"+e.getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                email.setText(newEmail);
                                                verificationTxt.setVisibility(View.VISIBLE);
                                                verifyIcon.setVisibility(View.VISIBLE);
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(),"Email change failed!",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                });

                                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // close the dialog
                                    }
                                });

                                passwordResetDialog.create().show();
                            } return true;

                            case R.id.changePassword:{
                                    final EditText resetPassword = new EditText(v.getContext());
                                    final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                                    passwordResetDialog.setTitle("Reset password ?");
                                    passwordResetDialog.setMessage("Enter your new password.");
                                    passwordResetDialog.setView(resetPassword);

                                    passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // extract the email and send reset link
                                            String newPass = resetPassword.getText().toString();
                                            user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(),"Password changed successfully",Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(),"Password change failed!",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });

                                    passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // close the dialog
                                        }
                                    });

                                    passwordResetDialog.create().show();

                                } return true;
                            default: return false;
                        }
                    }
                });
                popup.inflate(R.menu.settings_menu);
                popup.show();
            }
        });
        changeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ProductFragment(), "Products");
        adapter.addFragment(new IngredientFragment(), "Ingredients");

        viewPager.setAdapter(adapter);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findViews(View view) {
        fullName = view.findViewById(R.id.profileName);
        email    = view.findViewById(R.id.profileEmail);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user=fAuth.getCurrentUser();
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseProductsQuery = FirebaseDatabase.getInstance().getReference("products")
                .orderByChild("userID")
                .equalTo(fAuth.getCurrentUser().getUid());
        firebaseFirestore=FirebaseFirestore.getInstance();
        settingsImg=view.findViewById(R.id.settingsImg);
        verifyIcon=view.findViewById(R.id.verifyIcon);
        verificationTxt=view.findViewById(R.id.verificatioMsg);
        changeProfileImg=view.findViewById(R.id.changeProfileImg);
        profileImage = view.findViewById(R.id.profileImage);
        storageReference = FirebaseStorage.getInstance().getReference();

        viewPager = view.findViewById(R.id.viewPager);
        setUpViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }




}