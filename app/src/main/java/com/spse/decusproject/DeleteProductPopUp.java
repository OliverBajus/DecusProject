package com.spse.decusproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeleteProductPopUp extends Activity implements FirebaseLoadDone{

    Button deleteProduct;
    ImageView goBack;
    SearchableSpinner spinner;

    FirebaseAuth fAuth;

    DatabaseReference databaseProducts;

    List<Product> products;
    Product choosenProdcut;
    FirebaseLoadDone firebaseLoadDone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product_popup);
        fAuth = FirebaseAuth.getInstance();
        goBack = findViewById(R.id.goBack);
        spinner = findViewById(R.id.spinner);
        deleteProduct = findViewById(R.id.deleteProduct);
        spinner.setTitle("Choose product");

        databaseProducts = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid());


        databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot productsSnapshot:dataSnapshot.getChildren()) products.add(productsSnapshot.getValue(Product.class));
                firebaseLoadDone.onFirebaseLoadSuccess(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });

        firebaseLoadDone=this;
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width= dm.widthPixels;
        int height =dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    choosenProdcut=products.get(position);
                    deleteProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteProduct(choosenProdcut.getProductID());
                        }
                    });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void deleteProduct(String productID) {

        DatabaseReference dP = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid()).child(productID);
        dP.removeValue();
       finish();
    }



    @Override
    public void onFirebaseLoadSuccess(List<Product> productList) {
        products = productList;

        List<String> name_list = new ArrayList<>();

        for (Product product:productList) name_list.add(product.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadSuccessAllergens(List<Allergen> allergenList) {

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
