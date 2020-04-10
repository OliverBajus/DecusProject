package com.spse.decusproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.decus.R;

import com.spse.decusproject.Fragment.SearchFragment;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Integer> arraylist;

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        arraylist = new ArrayList<>();
        arraylist.addAll(SearchFragment.itemsArrayList);
        System.out.println("zaciatok " + arraylist.size());
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        System.out.println("size: " + SearchFragment.itemsArrayList.size());
        System.out.println("arraylist size: " + arraylist.size());
        return SearchFragment.itemsArrayList.size();

    }

    @Override
    public Integer getItem(int position) {
        return SearchFragment.itemsArrayList.get(position).intValue();
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
        holder.name.setText("" + SearchFragment.itemsArrayList.get(position));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SearchFragment.itemsArrayList.clear();
        Log.d("vsetky", "ahoj");
        if (charText.length() == 0) {
            SearchFragment.itemsArrayList.addAll(arraylist);
            System.out.println("vsetky");
        } else {
            for (int wp : arraylist) {
                String sp = String.valueOf(wp);
                if (sp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    SearchFragment.itemsArrayList.add(wp);

                }
            }
        }
        notifyDataSetChanged();
    }

}