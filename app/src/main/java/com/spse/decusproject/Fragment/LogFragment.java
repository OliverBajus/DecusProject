package com.spse.decusproject.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

public class LogFragment extends Fragment {

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
    List<DailyRoutine> dailyRoutinesM;
    List<DailyRoutine> dailyRoutinesD;
    List<DailyRoutine> dailyRoutinesE;

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
        final HorizontalCalendar calendar = getView().findViewById(R.id.hcCalendar);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -35);
        calendar.setSelected(c.getTime());
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        calendar.setSelected(d);

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


        listViewMorning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyRoutine dailyRoutine = dailyRoutinesM.get(position);
                showUpdateDeleteDialog(dailyRoutine.getId(), dailyRoutine.getDate());
            }
        });
        listViewDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyRoutine dailyRoutine = dailyRoutinesD.get(position);
                showUpdateDeleteDialog(dailyRoutine.getId(), dailyRoutine.getDate());
            }
        });
        listViewEvening.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyRoutine dailyRoutine = dailyRoutinesE.get(position);
                showUpdateDeleteDialog(dailyRoutine.getId(), dailyRoutine.getDate());
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
                dailyRoutinesM.clear();
                dailyRoutinesD.clear();
                dailyRoutinesE.clear();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    for (DailyRoutine dailyRoutine:dailyRoutines){
                        String productID=dailyRoutine.getProductID();
                        String dayPart=dailyRoutine.getDayPart();
                        Product product=productSnapshot.getValue(Product.class);
                        if (product.getProductID().trim().equals(productID.trim())){
                            if (dayPart.equals("morning")){
                                productListMorning.add(product);
                                dailyRoutinesM.add(dailyRoutine);
                            }
                            else if (dayPart.equals("day")){
                                productListDay.add(product);
                                dailyRoutinesD.add(dailyRoutine);
                            }
                            else if (dayPart.equals("evening")) {
                                productListEvening.add(product);
                                dailyRoutinesM.add(dailyRoutine);
                            }
                        }
                    }

                }
                if (getActivity() != null) {
                ProductsArrayListAdapter adapterMorning = new ProductsArrayListAdapter(getActivity(),productListMorning);
                ProductsArrayListAdapter adapterDay = new ProductsArrayListAdapter(getActivity(),productListDay);
                ProductsArrayListAdapter adapterEvening = new ProductsArrayListAdapter(getActivity(),productListEvening);
                listViewMorning.setAdapter(adapterMorning);
                listViewDay.setAdapter(adapterDay);
                listViewEvening.setAdapter(adapterEvening);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDeleteDialog(final String id, final String date) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);


        dialogBuilder.setTitle("Delete chosen product from calendar");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteDailyRoutine);


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteArtist(id,date);
                b.dismiss();
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
        SimpleDateFormat formatter3=new SimpleDateFormat("E MMM dd hh:mm:ss ZZZ yyyy");
        Date date = new Date();
        String dat=formatter3.format(date);
        productListMorning= new ArrayList<>(); dailyRoutinesM= new ArrayList<>();
        productListDay = new ArrayList<>(); dailyRoutinesD = new ArrayList<>();
        productListEvening = new ArrayList<>(); dailyRoutinesE =  new ArrayList<>();
        dailyRoutines= new ArrayList<>();
        databaseProducts = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid());
        databaseRoutine = FirebaseDatabase.getInstance().getReference("routineDatabase").child(fAuth.getCurrentUser().getUid()).child(calendar.getSelected().toString());
        ProductsArrayListAdapter adapterMorning = new ProductsArrayListAdapter(getActivity(),productListMorning);
        ProductsArrayListAdapter adapterDay = new ProductsArrayListAdapter(getActivity(),productListDay);
        ProductsArrayListAdapter adapterEvening = new ProductsArrayListAdapter(getActivity(),productListEvening);
        listViewMorning.setAdapter(adapterMorning);
        listViewDay.setAdapter(adapterDay);
        listViewEvening.setAdapter(adapterEvening);


    }

    private boolean deleteArtist(String id,String date) {
       DatabaseReference dR = FirebaseDatabase.getInstance().getReference("routineDatabase").child(fAuth.getCurrentUser().getUid()).child(date).child(id);
        dR.removeValue();
        setListViewdata();

        return true;
    }




}