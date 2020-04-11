package com.spse.decusproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spse.decusproject.Login;
import com.spse.decusproject.PopActivity;
import com.spse.decusproject.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {

    TextView fullName,email,changePassword,changeEmail;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    Button logOut,addAllergen,addProduct;
    RecyclerView recyclerView;

    DatabaseReference databaseProducts;
    FirebaseFirestore firebaseFirestore;
    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter;

    Query databaseProductsQuery;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        fillRecyclerView();

        return view;
    }

    private void fillRecyclerView() {

        options= new FirebaseRecyclerOptions.Builder<Product>().setQuery(databaseProductsQuery,Product.class).build();
        adapter= new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull Product model) {

                    holder.name.setText(model.getName());
                    holder.brand.setText("Brand: "+model.getBrand());
                    holder.category.setText("Category: "+model.getCategory());
                    holder.date.setText(model.getDate());
                    if (model.getCategory().equals("Moisturizes"))
                        holder.image.setImageResource(R.drawable.moisturizes);
                    if (model.getCategory().equals("Nails care"))
                        holder.image.setImageResource(R.drawable.nails_care);
                    if (model.getCategory().equals("Fragrances"))
                        holder.image.setImageResource(R.drawable.fragrances);
                    if (model.getCategory().equals("Make up and colour cosmetics"))
                        holder.image.setImageResource(R.drawable.make_up);



            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_layout,parent,false);
                return new ProductsViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                getActivity().finish();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();


            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetEmail= new EditText(v.getContext());

                final AlertDialog.Builder emailResetDialog = new AlertDialog.Builder(v.getContext());
                emailResetDialog.setTitle("Reset Email ?");
                emailResetDialog.setMessage("Enter your new email.");
                emailResetDialog.setView(resetEmail);

                emailResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newEmail = resetEmail.getText().toString();
                        user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Email changed Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Email changed Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                emailResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                emailResetDialog.create().show();

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), PopActivity.class));

            }
        });



    }



    private void findViews(View view) {
        fullName = view.findViewById(R.id.profileName);
        email    = view.findViewById(R.id.profileEmail);
        changePassword = view.findViewById(R.id.changePassword);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        logOut=view.findViewById(R.id.logout);
        changePassword=view.findViewById(R.id.changePassword);
        changeEmail=view.findViewById(R.id.changeEmail);
        addAllergen = view.findViewById(R.id.addAllergen);
        addProduct = view.findViewById(R.id.addProduct);
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseProductsQuery = FirebaseDatabase.getInstance().getReference("products")
                .orderByChild("userID")
                .equalTo(fAuth.getCurrentUser().getUid());
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
    }




}
