package com.spse.decusproject.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.decus.R;
import com.google.android.material.tabs.TabLayout;
import com.spse.decusproject.Adapter.SectionPagerAdapter;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;
import com.spse.decusproject.OCR;
import com.spse.decusproject.PopUpActivity;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

    TextView currentDate;
    View myFragment;
    Button scanButton;

    private SearchView editsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment =  inflater.inflate(R.layout.fragment_home, container, false);
        return myFragment;
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", java.util.Locale.ENGLISH);
        Date myDate = new Date();
        String sMyDate = "" + sdf.format(myDate);
        return sMyDate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        currentDate = myFragment.findViewById(R.id.current_date);
        currentDate.setText(getDate());

        editsearch = myFragment.findViewById(R.id.search);
        scanButton = myFragment.findViewById(R.id.scan_button);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                CosmeticDatabase database = null;
                try {
                    database = new CosmeticDatabase(s);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(database.getName());
                System.out.println(database.getFunction());
                Intent intent = new Intent(getActivity(), PopUpActivity.class);
                intent.putExtra("NAME", database.getName());
                intent.putExtra("FUNCTION", database.getFunction());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        scanButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OCR.class);
                startActivity(intent);
            } });
    }
}