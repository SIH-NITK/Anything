package com.example.sih;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.DetailsViewHolder> {
    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder{

    public DetailsViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
}
