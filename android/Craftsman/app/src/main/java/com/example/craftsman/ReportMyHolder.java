package com.example.craftsman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportMyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView userName, reportType, reportTitle, reportId;
    ItemClickListener itemClickListener;

    public ReportMyHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.modelReportUser);
        reportType = itemView.findViewById(R.id.modelReportType);
        reportTitle = itemView.findViewById(R.id.modelReportTitle);
        reportId = itemView.findViewById(R.id.modelReportId);

        reportId.setVisibility(View.GONE);
        itemView.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view , getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
