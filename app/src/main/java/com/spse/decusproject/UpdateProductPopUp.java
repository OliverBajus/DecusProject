package com.spse.decusproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UpdateProductPopUp extends Activity implements DatePickerDialog.OnDateSetListener,FirebaseLoadDone{

    EditText productName,productBrand;
    TextView productExpDate;
    Spinner spinner;
    Button editBtn;
    String date;
    ImageView goBack;
    SearchableSpinner spinnerSearch;
    FirebaseLoadDone firebaseLoadDone;
    DatabaseReference databaseProducts;
    FirebaseAuth fAuth;

    List<Product> products;
    Product choosenProdcut;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_popup);
        productName = findViewById(R.id.productName);
        productBrand = findViewById(R.id.productBrand);
        productExpDate = findViewById(R.id.dateTextView);
        spinner = findViewById(R.id.spinner);
        editBtn = findViewById(R.id.editProduct);
        goBack = findViewById(R.id.goBack);
        spinnerSearch =findViewById(R.id.spinnerSearch);
        fAuth = FirebaseAuth.getInstance();

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

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

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
        list.add("Make up and colour cosmetics");
        list.add("Fragrances");
        list.add("Nails care");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

       productExpDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDatePickerDialog();
           }
       });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }


        });
        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                choosenProdcut=products.get(position);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editProduct(choosenProdcut.getProductID(),productName.getText().toString(),productBrand.getText().toString(),spinner.getSelectedItem().toString(),date);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void editProduct(String id, String name,String brand,String category,String date) {
        DatabaseReference dP = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid()).child(id);

        if (!TextUtils.isEmpty(name)){
            if (!TextUtils.isEmpty(brand)){
                if (!productExpDate.getText().toString().contains("Choose expiration date.")){

                    Product product = new Product(name, brand,category,date,id);
                    dP.setValue(product);
                     Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
                else Toast.makeText(UpdateProductPopUp.this,"Choose product expiration date.",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(UpdateProductPopUp.this,"Enter product brand.",Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(UpdateProductPopUp.this,"Enter product name.",Toast.LENGTH_LONG).show();

    }

    public void showDatePickerDialog(){
         DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = month + "/" + dayOfMonth + "/" + year;
        productExpDate.setText("   Expiration date: " +date);
    }



    @Override
    public void onFirebaseLoadSuccess(List<Product> productList) {
        products = productList;

        List<String> name_list = new ArrayList<>();

        for (Product product:productList) name_list.add(product.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        spinnerSearch.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadSuccessAllergens(List<Allergen> allergenList) {

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
