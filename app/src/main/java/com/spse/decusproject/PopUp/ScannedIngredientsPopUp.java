package com.spse.decusproject.PopUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spse.decusproject.Adapter.ListViewAdapter;
import com.spse.decusproject.Objects.Allergen;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ScannedIngredientsPopUp extends AppCompatActivity {

    private ListView list;
    private ListViewAdapter adapter;
    public static ArrayList<String> arrayListOfIngredients = new ArrayList<>();
    private static ArrayList<Allergen> arrayList=new ArrayList<Allergen>();
    private DatabaseReference databaseAllergens;
    private  FirebaseAuth fAuth;
    private TextView warning_text;
    private ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_ingredients_pop);

        list = findViewById(R.id.listview);
        warning_text = findViewById(R.id.warning);
        goback = findViewById(R.id.goBack);
        arrayListOfIngredients = getIntent().getStringArrayListExtra("ARRAYLIST");

        fAuth = FirebaseAuth.getInstance();
        databaseAllergens = FirebaseDatabase.getInstance().getReference("allergensDatabase").child(fAuth.getCurrentUser().getUid());

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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        for (String object : arrayListOfIngredients) {
            adapter = new ListViewAdapter(ScannedIngredientsPopUp.this);
            list.setAdapter(adapter);

            Allergen allergen = new Allergen(object);

            for (Allergen allergen1:arrayList) {
                System.out.println(allergen1.getIngredientName());
                if (allergen.getIngredientName().toLowerCase().equals(allergen1.getIngredientName().toLowerCase())){
                   warning_text.setText("Be careful, this product contains your allergens");return;
                }
            }
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ingredient = arrayListOfIngredients.get(position);
                CosmeticDatabase database = null;
                try {
                    database = new CosmeticDatabase(ingredient.trim());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(database.getFunction() != null ){
                    Intent intent = new Intent(ScannedIngredientsPopUp.this, PopUpActivity.class);
                    intent.putExtra("NAME", database.getName());
                    intent.putExtra("FUNCTION", database.getFunction());
                    startActivity(intent);
                }else {
                    Toast.makeText(ScannedIngredientsPopUp.this, "Ingredient not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
