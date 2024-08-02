package com.nate.wastetracker.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nate.wastetracker.R;
import com.nate.wastetracker.model.BalanceUpdater;

public class AddBalance extends AppCompatActivity {

    private EditText amount;
    private EditText contact;
    private ImageView backBtn;
    private MaterialButton payBtn;
    private BalanceUpdater balanceUpdater;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_balance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        backBtn.setOnClickListener(v -> finish());
        payBtn.setOnClickListener(v -> checkFields());

    }

    private void initViews(){
        amount = findViewById(R.id.amount);
        contact = findViewById(R.id.contact);
        backBtn = findViewById(R.id.back_btn);
        payBtn = findViewById(R.id.add_balance);
        progressBar = findViewById(R.id.progress_bar);

        balanceUpdater = new BalanceUpdater(this, progressBar);
    }

    private void checkFields(){
        if (amount.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter amount to deposit", Toast.LENGTH_SHORT).show();
        }else if (contact.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter your mobile money number", Toast.LENGTH_SHORT).show();
        }else{
            balanceUpdater.addAmount(Double.parseDouble(amount.getText().toString()));
        }
    }

}