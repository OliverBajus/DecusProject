package com.spse.decusproject.PopUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spse.decusproject.Objects.Allergen;
import com.spse.decusproject.Objects.DailyRoutine;
import com.spse.decusproject.FirebaseLoadDone;
import com.spse.decusproject.Objects.Product;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DayRoutinePopUp extends Activity implements FirebaseLoadDone {

    private Button chooseProduct;
    private ImageView goBack;
    private SearchableSpinner spinner;

    private FirebaseAuth fAuth;

    private DatabaseReference databaseProducts;
    private DatabaseReference databaseRoutine;

    private FirebaseLoadDone firebaseLoadDone;
    private String dateIntent,dayPart;
    private List<Product> products;
    private Product choosenProdcut;
    private boolean isFirstTimeClicked= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_routine_pop_up);

        fAuth = FirebaseAuth.getInstance();
        chooseProduct = findViewById(R.id.chooseProduct);
        goBack = findViewById(R.id.goBack);
        spinner = findViewById(R.id.spinner);
        spinner.setTitle("Choose product");

        Intent intent=getIntent();
        dateIntent= intent.getStringExtra("date");
        dayPart = intent.getStringExtra("dayPart");
        databaseProducts = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid());
        databaseRoutine = FirebaseDatabase.getInstance().getReference("routineDatabase").child(fAuth.getCurrentUser().getUid()).child(dateIntent);

        firebaseLoadDone=this;

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
        chooseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DayRoutinePopUp.this,"Select your product",Toast.LENGTH_LONG).show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(!isFirstTimeClicked){
                             choosenProdcut=products.get(position);
                            chooseProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    chooseProduct();
                                }
                            });
                        }
                        else isFirstTimeClicked=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    private void chooseProduct() {
        String id=databaseRoutine.push().getKey();
        String productID=choosenProdcut.getProductID();
        DailyRoutine routine = new DailyRoutine(id,productID,dayPart,dateIntent);
        databaseRoutine.child(id).setValue(routine);
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
