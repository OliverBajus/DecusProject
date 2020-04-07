package com.spse.decusproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.decus.R;
import com.spse.decusproject.cosmetic_database.CosmeticDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class  HomeFragment extends Fragment {

    Intent intent;
    String data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = sdf.parse("28/12/2013");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("EEE, d MMM yyyy");
        String sMyDate = sdf.format(myDate);
        System.out.println("TU SOOOM datum " + sMyDate);
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
