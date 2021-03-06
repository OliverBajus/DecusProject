package com.spse.decusproject.Fragment;

import android.annotation.SuppressLint;
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
import com.spse.decusproject.PopUp.DeleteProductPopUp;
import com.spse.decusproject.PopUp.AddProductPopUp;
import com.spse.decusproject.Objects.Product;
import com.spse.decusproject.Objects.ProductsViewHolder;
import com.spse.decusproject.PopUp.UpdateProductPopUp;

import java.util.Objects;

public class ProductFragment extends Fragment {

    private Button addProduct,deleteProduct,editProduct;
    private RecyclerView recyclerView;

    private  FirebaseRecyclerOptions<Product> options;

    private Query databaseProductsQuery;

    public ProductFragment() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_product, container, false);
        findViews(view);
        fillRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), AddProductPopUp.class));
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), DeleteProductPopUp.class));
            }
        });
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), UpdateProductPopUp.class));
            }
        });
    }

    private void fillRecyclerView() {

        options= new FirebaseRecyclerOptions.Builder<Product>().setQuery(databaseProductsQuery,Product.class).build();
        FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull Product model) {

                holder.getName().setText(model.getName());
                holder.getBrand().setText("Brand: " + model.getBrand());
                holder.getCategory().setText("Category: " + model.getCategory());
                holder.getDate().setText(model.getDate());
                switch (model.getCategory()) {
                    case "Acid":
                        holder.getImage().setImageResource(R.drawable.acid);
                        break;
                    case "Mask":
                        holder.getImage().setImageResource(R.drawable.mask);
                        break;
                    case "Cleanser":
                        holder.getImage().setImageResource(R.drawable.cleanser);
                        break;
                    case "Moisturizer":
                        holder.getImage().setImageResource(R.drawable.moisturizer);
                        break;
                    case "Oil":
                        holder.getImage().setImageResource(R.drawable.oil);
                        break;
                    case "Make Up":
                        holder.getImage().setImageResource(R.drawable.makeup);
                        break;
                    case "Fragrance":
                        holder.getImage().setImageResource(R.drawable.fragrance);
                        break;
                    case "Nails care":
                        holder.getImage().setImageResource(R.drawable.nailcare);
                        break;
                }
            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_layout, parent, false);
                return new ProductsViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void findViews(View view){
        addProduct = view.findViewById(R.id.addProduct);
        deleteProduct = view.findViewById(R.id.deleteProduct);
        editProduct = view.findViewById(R.id.editProduct);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        databaseProductsQuery =  FirebaseDatabase.getInstance().getReference("productsDatabase").child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());

    }
}
