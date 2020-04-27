package com.spse.decusproject.Objects;

import android.view.View;
import android.widget.TextView;

import com.example.decus.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientViewHolder extends RecyclerView.ViewHolder {

    private TextView name,description;

    public IngredientViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.textViewName);
        description=itemView.findViewById(R.id.textViewDescription);
    }

    public TextView getName() {
        return name;
    }

    public TextView getDescription() {
        return description;
    }
}
