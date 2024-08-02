package com.nate.wastetracker.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.wastetracker.R;
import com.nate.wastetracker.model.BalanceUpdater;

public class ViewWallet extends AppCompatActivity {

    private ImageView backBtn;
    private TextView tvBalance;
    private MaterialButton addBalance;
    private BalanceUpdater balanceUpdater;


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

        backBtn.setOnClickListener(v -> finish());
        getBalance();

    }

    private void initViews(){
        tvBalance = findViewById(R.id.balance);
        backBtn = findViewById(R.id.back_btn);
        addBalance = findViewById(R.id.add_balance);
        balanceUpdater = new BalanceUpdater(this, null);
    }

    private void getBalance(){


        // Fetch balance and handle the result
        balanceUpdater.fetchBalance(new BalanceUpdater.BalanceCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onBalanceFetched(double balance) {
                // Handle the fetched balance here
                tvBalance.setText("GHC "+ String.format("%.2f", balance));
                System.out.println("Current balance: " + balance);
                // You can update UI components with the fetched balance here
            }

            @Override
            public void onError(Exception e) {
                // Handle the error here
                Toast.makeText(ViewWallet.this, "Error fetching balance: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Error fetching balance: " + e.getMessage());
            }
        });
    }

    }

