package com.nate.wastetracker.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.wastetracker.R;

import java.util.ArrayList;

public class AllPickUps extends AppCompatActivity {

    private RecyclerView allPickUps;
    private PickupAdapter pickupAdapter;
    private ArrayList<PickUpModel> pickUpModels;

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

        pickUpModels = new ArrayList<>();
        pickUpModels.add(new PickUpModel("1", "Uncompleted"));
        pickUpModels.add(new PickUpModel("2", "Uncompleted"));
        pickUpModels.add(new PickUpModel("3", "Completed"));
        pickupAdapter = new PickupAdapter(this, pickUpModels);
        allPickUps.setAdapter(pickupAdapter);

    }

    private void initViews() {
        allPickUps = findViewById(R.id.all_pick_up_list);
        allPickUps.setLayoutManager(new LinearLayoutManager(this));

    }

}