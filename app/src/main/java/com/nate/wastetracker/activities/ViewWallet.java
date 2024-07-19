package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nate.wastetracker.R;

public class ViewWallet extends AppCompatActivity {

    private TextView balance;
    private MaterialButton addBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_wallet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        addBalance.setOnClickListener(v -> {
           startActivity(new Intent(ViewWallet.this, AddBalance.class));
        });

    }

    private void initViews(){
        balance = findViewById(R.id.balance);
        addBalance = findViewById(R.id.add_balance);
    }

}