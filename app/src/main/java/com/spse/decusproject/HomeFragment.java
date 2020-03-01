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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

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
        Bundle extras = intent.getExtras();
        if(extras != null)
            data = extras.getString("ingrediencie"); // retr
    }

    public HomeFragment(){

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
