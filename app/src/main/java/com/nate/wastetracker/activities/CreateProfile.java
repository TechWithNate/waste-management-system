package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private EditText username;
    private EditText phoneNumber;
    private EditText address;
    private MaterialButton createBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        createBtn.setOnClickListener(v -> {
            checkFields();
        });
    }

    private void initViews(){
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        createBtn = findViewById(R.id.create_btn);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes");

    }

    private void checkFields(){
        if (username.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your contact number", Toast.LENGTH_SHORT).show();
        }else if (address.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        }else {
            createUser(username.getText().toString(), phoneNumber.getText().toString(), address.getText().toString());
        }
    }

    private void createUser(String username, String phoneNumber, String address){
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("username", username);
            userMap.put("contact", phoneNumber);
            userMap.put("address", address);
            sendUserData(userMap);
    }

    private void sendUserData(Map<String, Object> userMap) {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(firebaseAuth.getUid()).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                openHomePage();
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }else {
                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }

    private void openHomePage() {
        Intent intent = new Intent(CreateProfile.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}