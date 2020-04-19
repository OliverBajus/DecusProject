package com.spse.decusproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.decus.R;
import com.spse.decusproject.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductsArrayListAdapter extends ArrayAdapter<Product> {

    private Activity context;
    private List<Product> arrayList;

    public ProductsArrayListAdapter(Activity context, List<Product> arrayList) {
        super(context,R.layout.list_view_item_products,arrayList);
        this.context=context;
        this.arrayList=arrayList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();

        View listItemView=layoutInflater.inflate(R.layout.list_view_item_products,null,true);

        TextView name = (TextView) listItemView.findViewById(R.id.tvName);
        TextView brand = (TextView) listItemView.findViewById(R.id.tvBrand);

        Product product= arrayList.get(position);

        name.setText(product.getName());
        brand.setText(product.getBrand());

        return listItemView;
    }
}
