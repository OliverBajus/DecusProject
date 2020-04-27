package com.spse.decusproject.PopUp;

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
import com.spse.decusproject.Objects.Allergen;
import com.spse.decusproject.FirebaseLoadDone;
import com.spse.decusproject.Objects.Product;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeleteAllergenPopUp extends Activity implements FirebaseLoadDone {

    private Button deleteAllergen;
    private ImageView goBack;
    private SearchableSpinner spinner;

    private FirebaseAuth fAuth;

    private DatabaseReference databaseAllergens;

    private List<Allergen> allergens;
    private Allergen choosenAllergen;
    private FirebaseLoadDone firebaseLoadDone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_allergen_popup);
        fAuth = FirebaseAuth.getInstance();
        goBack = findViewById(R.id.goBack);
        spinner = findViewById(R.id.spinner);
        deleteAllergen = findViewById(R.id.deleteAllergen);
        spinner.setTitle("Choose allergen");

        databaseAllergens = FirebaseDatabase.getInstance().getReference("allergensDatabase").child(fAuth.getCurrentUser().getUid());


        databaseAllergens.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Allergen> allergens = new ArrayList<>();
                for (DataSnapshot productsSnapshot:dataSnapshot.getChildren()) allergens.add(productsSnapshot.getValue(Allergen.class));
                firebaseLoadDone.onFirebaseLoadSuccessAllergens(allergens);
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

                choosenAllergen=allergens.get(position);
                deleteAllergen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProduct(choosenAllergen.getId());
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void deleteProduct(String allergenID) {
        DatabaseReference dP =  FirebaseDatabase.getInstance().getReference("allergensDatabase").child(fAuth.getCurrentUser().getUid()).child(allergenID);
        dP.removeValue();
        finish();
    }



    @Override
    public void onFirebaseLoadSuccess(List<Product> productList) {

    }

    @Override
    public void onFirebaseLoadSuccessAllergens(List<Allergen> allergenList) {
        allergens = allergenList;

        List<String> name_list = new ArrayList<>();

        for (Allergen allergen:allergenList) name_list.add(allergen.getIngredientName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
