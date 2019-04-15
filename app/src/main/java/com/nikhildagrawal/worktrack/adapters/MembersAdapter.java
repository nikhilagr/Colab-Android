package com.nikhildagrawal.worktrack.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {


    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MembersViewHolder extends RecyclerView.ViewHolder {
        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
