package com.spse.decusproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.decus.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spse.decusproject.PopActivity;
import com.spse.decusproject.Product;
import com.spse.decusproject.ProductsViewHolder;

public class ProductFragment extends Fragment {

    Button addProduct;
    RecyclerView recyclerView;

    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter;

    Query databaseProductsQuery;

    public ProductFragment() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_product, container, false);
        findViews(view);
        fillRecyclerView();

        System.out.println("Som v produktoch");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), PopActivity.class));
            }
        });
    }

    private void fillRecyclerView() {

        options= new FirebaseRecyclerOptions.Builder<Product>().setQuery(databaseProductsQuery,Product.class).build();
        adapter= new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull Product model) {

                holder.getName().setText(model.getName());
                holder.getBrand().setText("Brand: "+model.getBrand());
                holder.getCategory().setText("Category: "+model.getCategory());
                holder.getDate().setText(model.getDate());
                if (model.getCategory().equals("Moisturizes"))
                    holder.getImage().setImageResource(R.drawable.moisturizes);
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

    private void findViews(View view){
        addProduct = view.findViewById(R.id.addProduct);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        databaseProductsQuery = FirebaseDatabase.getInstance().getReference("products")
                .orderByChild("userID")
                .equalTo(fAuth.getCurrentUser().getUid());
    }
}
