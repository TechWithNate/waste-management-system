package com.nate.wastetracker.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.wastetracker.R;
import com.nate.wastetracker.adapter.ReportAdapter;
import com.nate.wastetracker.model.ReportModel;

import java.util.ArrayList;

public class AdminReports extends AppCompatActivity {

    private RecyclerView reportsList;
    private MaterialToolbar topAppBar;
    private ArrayList<ReportModel> reportModels;
    private ReportAdapter reportAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        topAppBar.setNavigationOnClickListener(v -> finish());

        reportsList.setHasFixedSize(true);
        reportsList.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new ReportAdapter(this, reportModels);
        reportsList.setAdapter(reportAdapter);

        fetchAllReportsFromFirebase();

    }

    private void initViews(){
        reportsList = findViewById(R.id.reports_list);
        topAppBar = findViewById(R.id.toolbar);


        reportModels = new ArrayList<>();
    }

    //Fetch reports from database
    private void fetchReports(){
        //TODO: Fetch reports from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wastes");
        //databaseReference.child()

    }


    private void fetchAllReportsFromFirebase() {
        DatabaseReference allOrders = FirebaseDatabase.getInstance().getReference("wastes");
        allOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportModels.clear(); // Clear the existing list to avoid duplicates

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) { // Iterate over each user
                    for (DataSnapshot orderSnapshot : userSnapshot.child("reports").getChildren()) { // Iterate over each order
                        ReportModel model = orderSnapshot.getValue(ReportModel.class);
                        if (model != null) {
                            reportModels.add(model); // Add order to the list
                        }
                    }
                }

                if (reportModels.isEmpty()) {
                    Toast.makeText(AdminReports.this, "No reports found", Toast.LENGTH_SHORT).show();
                }

                reportAdapter.notifyDataSetChanged(); // Notify the adapter about data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminReports.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}