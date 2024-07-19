package com.nate.wastetracker.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private EditText username;
    private EditText address;
    private EditText contact;
    private MaterialButton editBtn;
    private MaterialButton logoutBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        loadUserData();


    }

    private void initViews(){
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        editBtn = findViewById(R.id.edit_profile_btn);
        logoutBtn = findViewById(R.id.logout_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes");
    }

    private void loadUserData() {
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    username.setText(name);
                    address.setText(snapshot.child("address").getValue(String.class));
                    contact.setText(snapshot.child("contact").getValue(String.class));

                } else {
                    Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }


}