package com.spse.decusproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.decus.R;
import com.spse.decusproject.PopUp.ScannedIngredientsPopUp;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public ListViewAdapter(Context context) {
        // Declare Variables
        inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return ScannedIngredientsPopUp.arrayListOfIngredients.size();
    }

    @Override
    public String getItem(int position) {
        return ScannedIngredientsPopUp.arrayListOfIngredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
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
        holder.name.setText("" + ScannedIngredientsPopUp.arrayListOfIngredients.get(position));
        return view;
    }
}