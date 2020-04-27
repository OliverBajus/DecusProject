package com.spse.decusproject.Objects;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    private TextView name,brand,category,date;
    private ImageView image;

    public ProductsViewHolder(@NonNull final View itemView) {
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
