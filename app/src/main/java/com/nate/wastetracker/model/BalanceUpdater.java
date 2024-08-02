package com.nate.wastetracker.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.wastetracker.activities.ViewWallet;

public class BalanceUpdater {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Context context;
    private ProgressBar progressBar;


    public BalanceUpdater(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("wastes").child(firebaseAuth.getUid()).child("balance");
    }

    public void addAmount(double amountToAdd) {
        if (amountToAdd > 1) {
            progressBar.setVisibility(View.VISIBLE);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Double currentBalance = dataSnapshot.getValue(Double.class);
                    if (currentBalance == null) {
                        currentBalance = 0.0; // Assuming a default balance of 0 if not set
                    }
                    double newBalance = currentBalance + amountToAdd;
                    databaseReference.setValue(newBalance).addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Update successful
                            Intent intent = new Intent(context, ViewWallet.class);
                            context.startActivity(intent);
                            Toast.makeText(context, "Balance updated successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Balance updated successfully.");
                        } else {
                            // Failed to update balance
                            Toast.makeText(context, "Failed to update balance: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to update balance: " + task.getException().getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    // Handle possible errors.
                    System.out.println("Error fetching balance: " + databaseError.getMessage());
                    Toast.makeText(context, "Error fetching balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Amount is not greater than 1
            System.out.println("Amount to add must be greater than 1.");
            Toast.makeText(context, "Amount to add must be greater than 1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchBalance(final BalanceCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double currentBalance = dataSnapshot.getValue(Double.class);
                if (currentBalance == null) {
                    currentBalance = 0.0; // Assuming a default balance of 0 if not set
                }
                callback.onBalanceFetched(currentBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });

    }
        public interface BalanceCallback {
        void onBalanceFetched(double balance);
        void onError(Exception e);
    }

    public void withdrawAmount(double amountToWithdraw) {
        if (amountToWithdraw > 0) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Double currentBalance = dataSnapshot.getValue(Double.class);
                    if (currentBalance == null) {
                        currentBalance = 0.0; // Assuming a default balance of 0 if not set
                    }
                    if (currentBalance >= amountToWithdraw) {
                        double newBalance = currentBalance - amountToWithdraw;
                        databaseReference.setValue(newBalance).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Update successful, navigate to another page
                            } else {
                                // Failed to update balance
                                System.out.println("Failed to update balance: " + task.getException().getMessage());
                            }
                        });
                    } else {

                        System.out.println("Insufficient balance to withdraw.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    // Handle possible errors.
                    System.out.println("Error fetching balance: " + databaseError.getMessage());
                }
            });
        } else {
            // Amount to withdraw is not positive
            System.out.println("Amount to withdraw must be greater than 0.");
        }
    }



}

