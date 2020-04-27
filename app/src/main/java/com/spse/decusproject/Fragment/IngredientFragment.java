package com.spse.decusproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.decus.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spse.decusproject.Objects.Allergen;
import com.spse.decusproject.PopUp.DeleteAllergenPopUp;
import com.spse.decusproject.Objects.IngredientViewHolder;

import java.util.Objects;

public class IngredientFragment extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseAuth fAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseRecyclerOptions<Allergen> options;
    private FirebaseRecyclerAdapter<Allergen, IngredientViewHolder> adapter;
    private DatabaseReference allergensDatabase;
    private Button deleteAllergen;

    public IngredientFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_ingredient, container, false);
        findViews(view);
        fillRecyclerView();
        deleteAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeleteAllergenPopUp.class));
            }
        });
        return view;
    }

    private void fillRecyclerView() {

        options= new FirebaseRecyclerOptions.Builder<Allergen>().setQuery(allergensDatabase, Allergen.class).build();
        adapter= new FirebaseRecyclerAdapter<Allergen, IngredientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IngredientViewHolder holder, int position, @NonNull Allergen model) {
                holder.getName().setText(model.getIngredientName());
            }

            @NonNull
            @Override
            public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.allergen_list_layout,parent,false);
                return new IngredientViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void findViews(View view){
        deleteAllergen= view.findViewById(R.id.deleteAllergen);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        allergensDatabase =  FirebaseDatabase.getInstance().getReference("allergensDatabase").child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
    }
}
