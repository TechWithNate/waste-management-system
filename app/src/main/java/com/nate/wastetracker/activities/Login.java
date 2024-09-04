package com.nate.wastetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nate.wastetracker.R;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton loginBtn;
    private ProgressBar progressbar;
    private TextView createAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        loginBtn.setOnClickListener(v -> {
            checkFields();
        });

        createAccount.setOnClickListener(v -> {
            openAccount();
        });
    }

    private void initViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        progressbar = findViewById(R.id.progress_bar);
        createAccount = findViewById(R.id.create_acc_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkFields(){
        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }else{
            loginUserIn(email.getText().toString(), password.getText().toString());
        }


    }

    private void openAccount(){
        startActivity(new Intent(Login.this, CreateAccount.class));
        finish();
    }

    private void loginUserIn(String email, String password){
        progressbar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        String userEmail = user.getEmail();
                        if ("francowasteadmin@gmail.com".equals(userEmail)) {
                            openAdminHomePage();
                        } else {
                            openHomePage();
                        }
                    }
                    progressbar.setVisibility(View.GONE);

                }else{
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openHomePage() {
        startActivity(new Intent(Login.this, HomePage.class));
        finish();
    }

    private void openAdminHomePage() {
        startActivity(new Intent(Login.this, AdminHome.class));
        finish();
    }

}