package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.nate.wastetracker.R;
import com.nate.wastetracker.dialogs.BottomSheetDialog;

public class HomePage extends AppCompatActivity {

    private MaterialCardView schedule;
    private MaterialCardView report;
    private MaterialCardView wallet;
    private MaterialCardView viewPickups;
    private MaterialCardView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        schedule.setOnClickListener(v -> {
            //Open Bottom Sheet to select address, date and item type and send to db
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
            bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
        });

        report.setOnClickListener(v -> {
            // Enter email, number, address message and send to db
            startActivity(new Intent(HomePage.this, Report.class));
        });

        wallet.setOnClickListener(v -> {
            // Open wallet Page
            startActivity(new Intent(HomePage.this, ViewWallet.class));
        });

        viewPickups.setOnClickListener(v -> {
            // Open Pickup Page
            startActivity(new Intent(HomePage.this, AllPickUps.class));
        });

        profile.setOnClickListener(v -> {
            // Open Profile Page
            startActivity(new Intent(HomePage.this, Profile.class));
        });

    }


    private void initViews(){
        schedule = findViewById(R.id.schedule_card);
        report = findViewById(R.id.missed_schedule_card);
        wallet = findViewById(R.id.wallet_card);
        viewPickups = findViewById(R.id.pickups_card);
        profile = findViewById(R.id.account);
    }

}