package com.spse.decusproject;

import androidx.appcompat.app.AppCompatActivity;
import com.example.decus.R;
import com.spse.decusproject.Adapter.ListViewAdapter;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ScannedIngredientsPopUp extends AppCompatActivity {

    private ListView list;
    private ListViewAdapter adapter;
    public static ArrayList<String> arrayOfIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_ingredients_pop);

        list = findViewById(R.id.listview);
        arrayOfIngredients = getIntent().getStringArrayListExtra("ARRAYLIST");

        for (String object : arrayOfIngredients) {
            adapter = new ListViewAdapter(ScannedIngredientsPopUp.this);
            list.setAdapter(adapter);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(arrayOfIngredients.get(position).trim());

                String ingredient = arrayOfIngredients.get(position).toString();
                CosmeticDatabase database = null;
                try {
                    database = new CosmeticDatabase(ingredient.trim());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(database.getName());
                System.out.println(database.getFunction());
                Intent intent = new Intent(ScannedIngredientsPopUp.this, PopUpActivity.class);
                intent.putExtra("NAME", database.getName());
                intent.putExtra("FUNCTION", database.getFunction());
                startActivity(intent);
            }
        });

    }
}
