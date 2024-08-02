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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.wastetracker.R;
import com.nate.wastetracker.adapter.AdminAdapter;

import java.util.ArrayList;

public class AdminPickup extends AppCompatActivity {

    private ArrayList<Waste> pickUpModels;
    private RecyclerView pickUpLists;
    private AdminAdapter adapter;

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

        adapter = new AdminAdapter(this, pickUpModels);
        pickUpLists.setHasFixedSize(true);
        pickUpLists.setLayoutManager(new LinearLayoutManager(this));
        pickUpLists.setAdapter(adapter);
        retrieveAllPickUps();
    }

    private void initViews() {
        pickUpLists = findViewById(R.id.all_pick_up_list);
        pickUpModels = new ArrayList<>();
    }


    private void retrieveAllPickUps() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wastes");

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

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        PickUpModel pickUpModel = dataSnapshot.getValue(PickUpModel.class);
//                        pickUpModels.add(pickUpModel);
//                    }
//                    adapter.notifyDataSetChanged();
//                }else{
//                    Toast.makeText(AdminPickup.this, "No data Exists", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}