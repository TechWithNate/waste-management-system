package com.nate.wastetracker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.wastetracker.R;

import java.util.ArrayList;

public class PickupAdapter extends RecyclerView.Adapter<PickupAdapter.ViewHolder>{

    private Context context;
    private ArrayList<PickUpModel> pickUpModels;

    public PickupAdapter(Context context, ArrayList<PickUpModel> pickUpModels) {
        this.context = context;
        this.pickUpModels = pickUpModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickUpModel pickup = pickUpModels.get(position);
        holder.status.setText(pickup.getStatus());
    }

    @Override
    public int getItemCount() {
        return pickUpModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView status;
        public ViewHolder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
        }
    }

}
