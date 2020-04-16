package com.spse.decusproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spse.decusproject.Fragment.ProductsViewHolder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DayRoutinePopUp extends Activity implements FirebaseLoadDone {

    Button chooseProduct;
    ImageView goBack;
    SearchableSpinner spinner;

    FirebaseAuth fAuth;

    DatabaseReference databaseProducts;
    DatabaseReference databaseRoutine;

    FirebaseLoadDone firebaseLoadDone;
    String dateIntent,dayPart;
    List<Product> products;
    Product choosenProdcut;
    boolean isFirstTimeClicked= true;
    Date date = null;
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



       // SimpleDateFormat formatter3=new SimpleDateFormat("E MMM dd hh:mm:ss ZZZ yyyy");

       // try {
      //      date=formatter3.parse(dateIntent);
      //  } catch (ParseException e) {
      //      e.printStackTrace();
      //  }
     //   chosenProduct.setText(date+","+dateIntent);



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
    public void onFirebaseLoadFailed(String message) {

    }
}
