package com.spse.decusproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spse.decusproject.cosmetic_database.CosmeticDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class  HomeFragment extends Fragment {

    Intent intent;
    String data;
    TextView currentDate;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentDate = (TextView) view.findViewById(R.id.current_date);
        currentDate.setText(getDate());
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH);
        Date myDate = new Date();
        String sMyDate = "" + sdf.format(myDate);
        return sMyDate;
    }



    public HomeFragment(){

        //Bundle extras = intent.getExtras();
        //if(extras != null)
          //  data = extras.getString("ingrediencie"); // retr

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        CosmeticDatabase cd = new CosmeticDatabase("Butylene Glycol");
        try {
            System.out.println("TU SOOOM " + cd.getDataFromDatabase());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
