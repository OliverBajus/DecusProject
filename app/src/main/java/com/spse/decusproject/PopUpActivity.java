package com.spse.decusproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.decus.R;

public class PopUpActivity extends Activity {

    TextView ingredientName, ingredientFunction, ingredientDescription;
    Button saveButton, cancelButton;
    String name, function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        ingredientName = findViewById(R.id.ingredient_name);
        ingredientDescription = findViewById(R.id.ingredient_description);
        ingredientFunction = findViewById(R.id.ingredient_function);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        if (getIntent().getExtras() != null) {
            name = getIntent().getStringExtra("NAME");
            function = getIntent().getStringExtra("FUNCTION");
        }

        ingredientName.setText(name);
        ingredientFunction.setText(function);

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

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
