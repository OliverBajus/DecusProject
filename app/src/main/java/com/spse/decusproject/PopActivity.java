package com.spse.decusproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class PopActivity extends Activity implements DatePickerDialog.OnDateSetListener {
    EditText productName;
    Button addProduct;
    Spinner spinner;
    private TextView dateText;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pop);

            productName=findViewById(R.id.productName);
            addProduct = findViewById(R.id.addProduct);
            spinner = findViewById(R.id.spinner);
            dateText = findViewById(R.id.date_text);

            DisplayMetrics dm= new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);

            int width= dm.widthPixels;
            int height =dm.heightPixels;

            getWindow().setLayout((int)(width*.8),(int)(height*.6));

            WindowManager.LayoutParams params=getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.x=0;
            params.y=-20;

            getWindow().setAttributes(params);
            getWindow().setAttributes(params);
            List<String> list= new ArrayList<>();
            list.add("Acids");
            list.add("Masks");
            list.add("Cleaners");
            list.add("Moisturizes");
            list.add("Oils");

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String itemValue=parent.getItemAtPosition(position).toString();

                    Toast.makeText(PopActivity.this,"Selected"+itemValue, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        findViewById(R.id.date_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
        public void showDatePickerDialog(){
               DatePickerDialog datePickerDialog = new DatePickerDialog(this,  this,Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
               datePickerDialog.show();

         }


       public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "   Expiration date: " + month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }
   }