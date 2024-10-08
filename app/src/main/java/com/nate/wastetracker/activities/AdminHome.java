package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.nate.wastetracker.R;

public class AdminHome extends AppCompatActivity {

    private MaterialCardView allPickUps;
    private MaterialCardView reports;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout_btn){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AdminHome.this, Login.class));
                    finish();
                }
                return false;
            }
        });

        allPickUps.setOnClickListener(v -> {
            startActivity(new Intent(AdminHome.this, AdminPickup.class));
        });

        reports.setOnClickListener(v -> {
            startActivity(new Intent(AdminHome.this, AdminReports.class));
        });

    }

    private void initViews(){
        allPickUps = findViewById(R.id.card1);
        reports = findViewById(R.id.card2);
        toolbar = findViewById(R.id.toolbar);
    }

}