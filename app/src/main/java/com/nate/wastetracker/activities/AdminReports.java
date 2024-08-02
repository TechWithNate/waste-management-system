package com.nate.wastetracker.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.nate.wastetracker.R;
import com.nate.wastetracker.adapter.ReportAdapter;
import com.nate.wastetracker.model.ReportModel;

import java.util.ArrayList;

public class AdminReports extends AppCompatActivity {

    private RecyclerView reportsList;
    private MaterialToolbar toolbar;
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

        reportsList.setHasFixedSize(true);
        reportsList.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new ReportAdapter(this, reportModels);
        reportsList.setAdapter(reportAdapter);


    }

    private void initViews(){
        reportsList = findViewById(R.id.reports_list);
        toolbar = findViewById(R.id.toolbar);

        reportModels = new ArrayList<>();
    }

    //Fetch reports from database
    private void fetchReports(){
        //TODO: Fetch reports from database

    }

}