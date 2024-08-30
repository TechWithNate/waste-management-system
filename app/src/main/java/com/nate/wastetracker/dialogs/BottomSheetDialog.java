package com.nate.wastetracker.dialogs;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.wastetracker.R;
import com.nate.wastetracker.activities.Report;
import com.nate.wastetracker.activities.Waste;
import com.nate.wastetracker.model.BalanceUpdater;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private EditText etAddress;
    private TextView date;
    private TextView day;
    private Button btnSubmit;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String wasteType;
    private AutoCompleteTextView type;
    private MaterialCardView dateLayout;
    private Calendar calendar;
    private BalanceUpdater balanceUpdater;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        balanceUpdater = new BalanceUpdater(view.getContext(), null);

        etAddress = view.findViewById(R.id.address);
        date = view.findViewById(R.id.date);
        day = view.findViewById(R.id.day);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        type = view.findViewById(R.id.type);
        dateLayout = view.findViewById(R.id.date_layout);

        String[] wasteTypeOptions = {"Organic waste", "Recyclable waste (paper, plastics, glass)", "Hazardous waste (batteries, chemicals)",
                "E-waste (electronics)", "General waste (non-recyclable)", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, wasteTypeOptions);
        type.setAdapter(adapter);
        type.setOnItemClickListener((parent, view1, position, id) ->
                wasteType = (String) parent.getItemAtPosition(position)
        );

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int tday = calendar.get(Calendar.DAY_OF_MONTH);

        dateLayout.setOnClickListener(v -> showDatePickerDialog(date, day, year, month, tday));


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes").child(firebaseAuth.getUid()).child("schedule");

        btnSubmit.setOnClickListener(v -> {
            balanceUpdater.withdrawAmount(50.00);


            String address = etAddress.getText().toString().trim();
            String pday = day.getText().toString().trim();
            String pdate = date.getText().toString().trim();
            String ptype = type.getText().toString().trim();
            String status = "Incomplete";

            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(pday) || TextUtils.isEmpty(pdate) || TextUtils.isEmpty(ptype)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = databaseReference.push().getKey();
            if (id != null) {
                Waste addressModel = new Waste(firebaseAuth.getUid(), id, pdate, pday,address, ptype, status);
                databaseReference.child(id).setValue(addressModel)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Data Submitted", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "Failed to submit data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
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
                getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day
        );
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }


}
