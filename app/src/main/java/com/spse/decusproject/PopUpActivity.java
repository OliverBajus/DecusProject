package com.spse.decusproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class PopUpActivity extends Activity {

    TextView ingredientName, ingredientFunction, ingredientDescription;
    Button saveButton;
    ImageView goback;
    String name, function;

    DatabaseReference databaseAllergens,databaseIngredients, databaseFunctions;
    FirebaseAuth fAuth;
    ArrayList<Allergen> arrayList=new ArrayList<Allergen>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient);
        ingredientName = findViewById(R.id.ingredient_name);
        ingredientDescription = findViewById(R.id.ingredient_description);
        ingredientFunction = findViewById(R.id.ingredient_function);
        saveButton = findViewById(R.id.save_button);
        goback = findViewById(R.id.goBack);
        fAuth = FirebaseAuth.getInstance();

        databaseAllergens = FirebaseDatabase.getInstance().getReference("allergensDatabase").child(fAuth.getCurrentUser().getUid());
        databaseIngredients = FirebaseDatabase.getInstance().getReference("ingredientsDatabase");
        databaseFunctions = FirebaseDatabase.getInstance().getReference("functionsDatabase");

        Function f = new Function("PROPELLANT","blabla propellant");
        databaseFunctions.child("PROPELLANT").setValue(f);



        databaseAllergens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    Allergen allergen=productSnapshot.getValue(Allergen.class);
                    arrayList.add(allergen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (getIntent().getExtras() != null) {
            name = getIntent().getStringExtra("NAME");
            function = getIntent().getStringExtra("FUNCTION");
        }

        try{

        ingredientName.setText(name);
        ingredientFunction.setText(function);


        DatabaseReference ingredients=databaseIngredients.child(name);
        DatabaseReference descriptions = ingredients.child("ingredientDescription");

        descriptions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ingredientDescription.setText(dataSnapshot.getValue(String.class));
                if (ingredientDescription.getText().toString().isEmpty())ingredientDescription.setText("none");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("tag", "onCancelled", databaseError.toException());
            }
        });
        }
        catch (NullPointerException e){
            saveButton.setVisibility(View.GONE);
            ingredientName.setText("No result");
            ingredientDescription.setText("No result");
            ingredientFunction.setText("No result");
        }

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width= dm.widthPixels;
        int height =dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.9));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;

        getWindow().setAttributes(params);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save ingredient's name to database of allergens
                addAllergen();

            }
        });

       goback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }



    private void addAllergen() {
        String name=ingredientName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)){
                    String id=databaseAllergens.push().getKey();
                    Allergen allergen = new Allergen(name);

                   for (Allergen allergen1:arrayList) {
                                if (allergen.getIngredientName().equals(allergen1.getIngredientName())){
                                    Toast.makeText(PopUpActivity.this,"This ingredient is already saved as allergen",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(PopUpActivity.this,MainActivity.class));return;
                                }
                         }
            databaseAllergens.child(id).setValue(allergen);
            Toast.makeText(PopUpActivity.this,"Ingredient added as allergen successfully.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(PopUpActivity.this,MainActivity.class));return;


        }
        else Toast.makeText(PopUpActivity.this,"Error! Something went wrong.",Toast.LENGTH_LONG).show();
    }


}
