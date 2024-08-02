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

import java.util.ArrayList;

public class AllPickUps extends AppCompatActivity {

    private RecyclerView allPickUps;
    private PickupAdapter pickupAdapter;
    private ArrayList<PickUpModel> pickUpModels;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_pick_ups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        toolbar.setNavigationOnClickListener(v -> finish());

        pickUpModels = new ArrayList<>();
//        pickUpModels.add(new PickUpModel("1", "Uncompleted", ""));
//        pickUpModels.add(new PickUpModel("2", "Uncompleted", ""));
//        pickUpModels.add(new PickUpModel("3", "Completed", ""));
        pickupAdapter = new PickupAdapter(this, pickUpModels);
        allPickUps.setAdapter(pickupAdapter);

        retrievePickUps();

    }

    private void initViews() {
        allPickUps = findViewById(R.id.all_pick_up_list);
        allPickUps.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.topAppBar);

    }

    private void retrievePickUps(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wastes").child(firebaseAuth.getUid()).child("schedule");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        PickUpModel pickUpModel = dataSnapshot.getValue(PickUpModel.class);
                        pickUpModels.add(pickUpModel);
                    }
                    pickupAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(AllPickUps.this, "No data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllPickUps.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}