package com.spse.decusproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.decus.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spse.decusproject.Adapter.SectionPagerAdapter;
import com.spse.decusproject.cosmetic_database.CosmeticDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

    Intent intent;
    String data;
    TextView currentDate;

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;


    public HomeFragment() {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment =  inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        return myFragment;
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH);
        Date myDate = new Date();
        String sMyDate = "" + sdf.format(myDate);
        return sMyDate;
    }

    //Call onActivity Create method


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        currentDate = (TextView) myFragment.findViewById(R.id.current_date);
        currentDate.setText(getDate());

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());


        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new ScanFragment(), "Scan");

        viewPager.setAdapter(adapter);
    }
}
