package com.spse.decusproject.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spse.decusproject.Adapter.ProductsArrayListAdapter;
import com.spse.decusproject.DailyRoutine;
import com.spse.decusproject.DayRoutinePopUp;
import com.spse.decusproject.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import be.greifmatthias.horizontalcalendarstrip.HorizontalCalendar;

public class LogFragment extends Fragment  {

    private Button morningBtn,dayBtn,eveningBtn;
    FirebaseAuth fAuth;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseRoutine;
    HorizontalCalendar calendar;
    ListView listViewMorning,listViewDay,listViewEvening;

    List<Product> productListMorning;
    List<Product> productListDay;
    List<Product> productListEvening;
    List<DailyRoutine> dailyRoutines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_log,container,false);
        findViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Calendar

        calendar.setOnChanged(new HorizontalCalendar.onChangeListener() {
            @Override
            public void selectChanged(Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                databaseRoutine = FirebaseDatabase.getInstance().getReference("routineDatabase").child(fAuth.getCurrentUser().getUid()).child(date.toString());
                setListViewdata();

            }

            @Override
            public void labelChanged(Date sourceDate) {

            }
        });

        morningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),DayRoutinePopUp.class);
                intent.putExtra("dayPart","morning");
                intent.putExtra("date",calendar.getSelected().toString());
                startActivity(intent);

            }
        });
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),DayRoutinePopUp.class);
                intent.putExtra("dayPart","day");
                intent.putExtra("date",calendar.getSelected().toString());
                startActivity(intent);

            }
        });
        eveningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),DayRoutinePopUp.class);
                intent.putExtra("dayPart","evening");
                intent.putExtra("date",calendar.getSelected().toString());
                startActivity(intent);

            }
        });




    }



    @Override
    public void onStart() {
        super.onStart();
        setListViewdata();


    }

    private void setListViewdata() {
        databaseRoutine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dailyRoutines.clear();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    DailyRoutine dailyRoutine=productSnapshot.getValue(DailyRoutine.class);
                    dailyRoutines.add(dailyRoutine);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productListMorning.clear();
                productListDay.clear();
                productListEvening.clear();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    for (DailyRoutine dailyRoutine:dailyRoutines){
                        String productID=dailyRoutine.getProductID();
                        String dayPart=dailyRoutine.getDayPart();
                        Product product=productSnapshot.getValue(Product.class);
                        if (product.getProductID().trim().equals(productID.trim())){
                            if (dayPart.equals("morning")) productListMorning.add(product);
                            else if (dayPart.equals("day")) productListDay.add(product);
                            else if (dayPart.equals("evening")) productListEvening.add(product);
                        }
                    }

                }
                ProductsArrayListAdapter adapterMorning = new ProductsArrayListAdapter(getActivity(),productListMorning);
                ProductsArrayListAdapter adapterDay = new ProductsArrayListAdapter(getActivity(),productListDay);
                ProductsArrayListAdapter adapterEvening = new ProductsArrayListAdapter(getActivity(),productListEvening);
                listViewMorning.setAdapter(adapterMorning);
                listViewDay.setAdapter(adapterDay);
                listViewEvening.setAdapter(adapterEvening);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findViews(View view) {
        morningBtn=view.findViewById(R.id.morningBtn);
        dayBtn = view.findViewById(R.id.dayBtn);
        eveningBtn = view.findViewById(R.id.eveningBtn);
        fAuth  = FirebaseAuth.getInstance();
        calendar = view.findViewById(R.id.hcCalendar);
        listViewMorning = view.findViewById(R.id.listViewMorning);
        listViewDay = view.findViewById(R.id.listViewDay);
        listViewEvening = view.findViewById(R.id.listViewEvening);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -35);
        calendar.setSelected(c.getTime());
        Date d = new Date();
        calendar.setSelected(d);
        productListMorning= new ArrayList<>();
        productListDay = new ArrayList<>();
        productListEvening = new ArrayList<>();
        dailyRoutines= new ArrayList<>();
        databaseProducts = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid());
        databaseRoutine = FirebaseDatabase.getInstance().getReference("routineDatabase").child(fAuth.getCurrentUser().getUid()).child(calendar.getSelected().toString());


    }



}
