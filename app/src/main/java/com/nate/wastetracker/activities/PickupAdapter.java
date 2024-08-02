package com.nate.wastetracker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        //We need to get the pick up ID and mark the status to complete in the database then display it to the user.


        /*
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wastes").child("schedule");

        ///

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        PickUpModel pickUpModel = dataSnapshot.getValue(PickUpModel.class);
                        String id = pickUpModel.getId();
                        pickUpModels.add(pickUpModel);
                    }

                }else{
                    Toast.makeText(context, "No data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        ///

        holder.status.setText(pickup.getStatus());
        if (holder.status.getText().equals("Incomplete")){
            holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        }else{
            holder.status.setTextColor(context.getResources().getColor(R.color.primary_color));
        }
        holder.date.setText(pickup.getDate());
    }

    @Override
    public int getItemCount() {
        return pickUpModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView status;
        private TextView date;
        public ViewHolder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
        }
    }

}
