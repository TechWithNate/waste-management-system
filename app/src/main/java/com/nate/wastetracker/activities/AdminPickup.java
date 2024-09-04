package com.nate.wastetracker.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.wastetracker.R;
import com.nate.wastetracker.adapter.AdminAdapter;

import java.util.ArrayList;
import java.util.Map;

public class AdminPickup extends AppCompatActivity implements AdminAdapter.SwitchListener {

    private ArrayList<Waste> pickUpModels;
    private RecyclerView pickUpLists;
    private AdminAdapter adapter;
    private Waste model;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pickup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        topAppBar.setNavigationOnClickListener(v -> {
            finish();
        });



        //adapter = new AdminAdapter(this, pickUpModels);
        adapter = new AdminAdapter(this, pickUpModels, this);

        pickUpLists.setHasFixedSize(true);
        pickUpLists.setLayoutManager(new LinearLayoutManager(this));
        pickUpLists.setAdapter(adapter);
        retrieveAllPickUps();
    }

    private void initViews() {
        pickUpLists = findViewById(R.id.all_pick_up_list);
        pickUpModels = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes");
        topAppBar = findViewById(R.id.topAppBar);
    }


    private void retrieveAllPickUps() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wastes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pickUpModels.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot pickUpSnapshot: userSnapshot.child("schedule").getChildren()) {
                            Waste wastePickup = pickUpSnapshot.getValue(Waste.class);
                            pickUpModels.add(wastePickup);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminPickup.this, "No data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onSwitchClick(int position, boolean isChecked) {
        Waste waste = pickUpModels.get(position);

        // Update the status
        String newStatus = isChecked ? "Complete" : "Incomplete";
        waste.setStatus(newStatus);

        // Update Firebase with the new status
        updateStatusInDatabase(position);
    }

    @Override
    public void switchPositionSelected(int position) {
        updateSwitch(pickUpModels.get(position));
    }

    private void updateSwitch(Waste waste) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("wastes")
                .child(waste.getId()) // Use the correct path.child(pickUp.getId()) // Make sure to use the correct path
                .child("schedule");

                //.child(waste.getWasteID()); // or other unique ID under schedule
        Toast.makeText(AdminPickup.this, "ID is: "+waste.getId(), Toast.LENGTH_SHORT).show();

        //databaseReference.child("status").setValue(waste.getStatus());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                pickUpModels.clear();
//                if (snapshot.exists()) {
//                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                        for (DataSnapshot pickUpSnapshot : userSnapshot.child("schedule").getChildren()) {
//                            Waste wastePickup = pickUpSnapshot.getValue(Waste.class);
//
//                            if (wastePickup == null){
//                                Toast.makeText(context, "Waste is null", Toast.LENGTH_SHORT).show();
//                            }else{
//                                databaseReference.child(wastePickup.getId()).child(wastePickup.getWasteID()).updateChildren(statusMap).addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(context, "Waste Dispatched", Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                Toast.makeText(context, "Not null", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    }
//
//                } else {
//                    Toast.makeText(context, "No data Exists", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    private void updateStatusInDatabase(int position) {

        model = pickUpModels.get(position);
        if (model != null) {
            // Assuming you have an order ID or user ID to identify the order
            String userId = model.getId();

            // Update the status in Firebase
            databaseReference.child(model.getId()).child("schedule").child(model.getWasteID()).child("status").setValue("Completed")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminPickup.this, "Status updated to ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminPickup.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No order to update", Toast.LENGTH_SHORT).show();
        }
        // Assuming `databaseReference` is your Firebase reference
//        if (firebaseAuth.getUid() != null && waste.getId() != null) {
//
//            databaseReference.child("schedule").child(waste.getId()).child(waste.getWasteID()).child("status")
//                    .setValue(waste)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(this, "Status updated to " + waste.getStatus(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        }
    }


}