package com.spse.decusproject.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.decus.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

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

    public TextView getName() {
        return name;
    }

    public TextView getBrand() {
        return brand;
    }

    public TextView getCategory() {
        return category;
    }

    public TextView getDate() {
        return date;
    }

    public ImageView getImage() {
        return image;
    }
}
