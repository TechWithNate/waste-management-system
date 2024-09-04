package com.nate.wastetracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.wastetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Report extends AppCompatActivity {

    private ImageView backBtn;
    private EditText email;
    private EditText message;
    private MaterialCardView dateLayout;
    private MaterialButton reportBtn;
    private TextView date;
    private TextView tday;
    private ProgressBar progressBar;
    private Calendar calendar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateLayout.setOnClickListener(v -> showDatePickerDialog(date, tday, year, month, day));

        reportBtn.setOnClickListener(v -> {
            checkFields();
        });

        backBtn.setOnClickListener(v -> finish());

    }

    private void initViews(){
        email = findViewById(R.id.email);
        message = findViewById(R.id.report);
        dateLayout = findViewById(R.id.date_layout);
        reportBtn = findViewById(R.id.report_btn);
        date = findViewById(R.id.tvDate);
        tday = findViewById(R.id.tvDay);
        progressBar = findViewById(R.id.progress_bar);
        backBtn = findViewById(R.id.back_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes");


    }

    private void checkFields(){
        if (email.getText().toString().isEmpty()){
            email.setError("Email is required");
        } else if (message.getText().toString().isEmpty()){
            message.setError("Message is required");
        } else if (date.getText().toString().isEmpty() && tday.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
        }else {
            sendDataToDb(email.getText().toString(), message.getText().toString(), date.getText().toString(), tday.getText().toString());
        }
    }

    private void sendDataToDb(String email, String message, String date, String day){
        progressBar.setVisibility(View.VISIBLE);
        String id = databaseReference.push().getKey();
        Waste waste = new Waste(id, email, message, date, day);
        if (null != id && firebaseAuth.getUid() != null){
            databaseReference.child(firebaseAuth.getUid()).child("reports").child(id).setValue(waste).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()){
                            navigateToHome();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Report.this, "Report Sent", Toast.LENGTH_SHORT).show();
                        }else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Report.this, "Failed to send report", Toast.LENGTH_SHORT).show();
                        }

                    }
            ).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Report.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }else{
            progressBar.setVisibility(View.GONE);
        }

    }

    private void navigateToHome() {
        startActivity(new Intent(Report.this, HomePage.class));
        finish();
    }

    private void showDatePickerDialog(TextView dateField, TextView dayField, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener setListener = (view, yearSelected, monthSelected, dayOfMonthSelected) -> {
            calendar.set(yearSelected, monthSelected, dayOfMonthSelected);
            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            String formattedDate = dateFormat.format(date);
            String formattedDay = dayFormat.format(date);

            dateField.setText(formattedDate);
            dayField.setText(formattedDay);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Report.this, android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day
        );
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }


}