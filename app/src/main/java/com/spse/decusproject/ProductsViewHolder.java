package com.spse.decusproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.decus.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ProductsViewHolder extends RecyclerView.ViewHolder {
    TextView name,brand,category,date;
    ImageView image;
    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        name=itemView.findViewById(R.id.textViewName);
        brand=itemView.findViewById(R.id.textViewBrand);
        category=itemView.findViewById(R.id.textViewCategory);
        date=itemView.findViewById(R.id.textViewExpirationDate);
        image=itemView.findViewById(R.id.productImageView);
    }
}
