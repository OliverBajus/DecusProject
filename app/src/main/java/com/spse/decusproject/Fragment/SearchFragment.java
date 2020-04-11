package com.spse.decusproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.decus.R;
import com.spse.decusproject.CosmeticDatabase.CosmeticDatabase;
import com.spse.decusproject.Adapter.ListViewAdapter;
import com.spse.decusproject.PopUpActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.j
 */
public class SearchFragment extends Fragment{


    private View myFragment;
    private SearchView editsearch;
    private ListView list;
    private ListViewAdapter adapter;
    public static ArrayList<Integer> itemsArrayList = new ArrayList<>();

    public SearchFragment() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment =  inflater.inflate(R.layout.fragment_search, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editsearch = myFragment.findViewById(R.id.search);
        list =  myFragment.findViewById(R.id.listview);

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

    }
}
