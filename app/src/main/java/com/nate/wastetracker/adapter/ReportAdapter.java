package com.nate.wastetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.wastetracker.R;
import com.nate.wastetracker.model.ReportModel;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ReportModel> reportModels;

    public ReportAdapter(Context context, ArrayList<ReportModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportModel reportModel = reportModels.get(position);
        holder.email.setText(reportModel.getEmail());
        holder.houseNo.setText(reportModel.getHouseNo());
        holder.date.setText(reportModel.getDate());
        holder.report.setText(reportModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView email;
        private TextView houseNo;
        private TextView date;
        private TextView report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email);
            houseNo = itemView.findViewById(R.id.houseNo);
            date = itemView.findViewById(R.id.date);
            report = itemView.findViewById(R.id.report);

        }

    }
}
