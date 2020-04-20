package com.spse.decusproject.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    TextView name,brand,category,date;
    ImageView image,delete;

    public ProductsViewHolder(@NonNull final View itemView) {
        super(itemView);

        name=itemView.findViewById(R.id.textViewName);
        brand=itemView.findViewById(R.id.textViewBrand);
        category=itemView.findViewById(R.id.textViewCategory);
        date=itemView.findViewById(R.id.textViewExpirationDate);
        image=itemView.findViewById(R.id.productImageView);
        delete= itemView.findViewById(R.id.deleteBtn);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), "Ajoj", Toast.LENGTH_SHORT).show();
            }
        });
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
