package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
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

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private TextView initials;
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
        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(Profile.this, Login.class));
            finish();
        });


    }

    private void initViews(){
        initials = findViewById(R.id.initials);
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
                    assert name != null;
                    initials.setText(getInitials(name));

                } else {
                    Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Profile.this, CreateProfile.class));
                    finish();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static String getInitials(String name) {
        StringBuilder initials = new StringBuilder();

        // Split the string into words
        String[] words = name.trim().split("\\s+");

        // Loop through the words and take the first letter of each
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }

        // If there are more than two initials, just take the first two
        if (initials.length() > 2) {
            return initials.substring(0, 2).toUpperCase();
        }

        return initials.toString().toUpperCase();
    }


}