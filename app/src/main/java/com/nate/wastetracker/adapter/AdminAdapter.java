package com.nate.wastetracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.wastetracker.R;

import com.nate.wastetracker.activities.PickUpModel;
import com.nate.wastetracker.activities.Waste;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Waste> pickUpModels;
    DatabaseReference databaseReference;
    private SwitchListener switchListener;


    public interface SwitchListener{
        void switchPositionSelected(int position);
    }

    public AdminAdapter(Context context, ArrayList<Waste> pickUpModels, SwitchListener switchListener) {
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes");
        this.context = context;
        this.pickUpModels = pickUpModels;
        this.switchListener = switchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pickup, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Waste wastepickUp = pickUpModels.get(position);
        holder.status.setText(wastepickUp.getStatus());
        holder.houseNo.setText(wastepickUp.getAddress());
        switchListener.switchPositionSelected(position);

        if (null != wastepickUp){
            holder.switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {

                    holder.status.setText("Complete");
                    //updateStatusInDatabase(wastepickUp);
                    //pickUpModel.setStatus("Complete");
                    updatePickUpStatus(wastepickUp, Map.of("status", "Completed"));
                    buttonView.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
                } else {
                    holder.status.setText("Incomplete");
                    // pickUpModel.setStatus("Incomplete");
                    //updateStatusInDatabase(wastepickUp);
                    updatePickUpStatus(wastepickUp, Map.of("status", "Incomplete"));
                    buttonView.setTextColor(context.getResources().getColor(android.R.color.white));
                }
            });
        }else {
            Toast.makeText(context, "Eppty", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public int getItemCount() {
        return pickUpModels.size();
    }

    private void updateStatusInDatabase(Waste pickUp) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("wastes")
                .child(pickUp.getId()) // Use the correct path.child(pickUp.getId()) // Make sure to use the correct path
                .child("schedule")
                .child(pickUp.getWasteID()); // or other unique ID under schedule
        Toast.makeText(context, pickUp.getWasteID(), Toast.LENGTH_SHORT).show();

        databaseReference.child("status").setValue(pickUp.getStatus());
    }

    private void updatePickUpStatus(Waste pickUp, Map<String, Object> statusMap) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pickUpModels.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot pickUpSnapshot : userSnapshot.child("schedule").getChildren()) {
                            Waste wastePickup = pickUpSnapshot.getValue(Waste.class);

                            if (wastePickup == null){
                                Toast.makeText(context, "Waste is null", Toast.LENGTH_SHORT).show();
                            }else{
                                databaseReference.child(wastePickup.getId()).child(wastePickup.getWasteID()).updateChildren(statusMap).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "Waste Dispatched", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(context, "Not null", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                } else {
                    Toast.makeText(context, "No data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView status;
            private TextView houseNo;
            private SwitchMaterial switchMaterial;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                status = itemView.findViewById(R.id.status);
                houseNo = itemView.findViewById(R.id.houseNo);
                switchMaterial = itemView.findViewById(R.id.switchMaterial);

            }

        }

}
