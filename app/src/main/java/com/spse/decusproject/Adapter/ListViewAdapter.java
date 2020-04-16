package com.spse.decusproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.decus.R;

import com.spse.decusproject.ScannedIngredientsPopUp;

import java.util.ArrayList;
import java.util.Locale;

import static com.spse.decusproject.ScannedIngredientsPopUp.arrayOfIngredients;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        arraylist = new ArrayList<String>();
        arraylist.addAll(arrayOfIngredients);
        System.out.println("zaciatok " + arraylist.size());
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        System.out.println("size: " + arrayOfIngredients.size());
        System.out.println("arraylist size: " + arraylist.size());
        return ScannedIngredientsPopUp.arrayOfIngredients.size();

    }

    @Override
    public String getItem(int position) {
        return ScannedIngredientsPopUp.arrayOfIngredients.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.name =  view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("" + ScannedIngredientsPopUp.arrayOfIngredients.get(position));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ScannedIngredientsPopUp.arrayOfIngredients.clear();
        Log.d("vsetky", "ahoj");
        if (charText.length() == 0) {
            ScannedIngredientsPopUp.arrayOfIngredients.addAll(arraylist);
            System.out.println("vsetky");
        } else {
            for (String wp : arraylist) {
                String sp = String.valueOf(wp);
                if (sp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    ScannedIngredientsPopUp.arrayOfIngredients.add(wp);

                }
            }
        }
        notifyDataSetChanged();
    }

}