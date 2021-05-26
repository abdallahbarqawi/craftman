package com.example.craftsman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportMyAdapter extends RecyclerView.Adapter<ReportMyHolder> implements Filterable {
    Context c ;
    ArrayList<ReportModel> models, filterList;
    ReportCustomFilter filter;
    ReportMyAdapter(Context c, ArrayList<ReportModel> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;

    }
    @NonNull
    @Override
    public ReportMyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_model,null);
        return new ReportMyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportMyHolder holder, int position) {
        holder.userName.setText(models.get(position).getUsername());
        holder.reportType.setText(models.get(position).getReportType());
        holder.reportTitle.setText(models.get(position).getReport());
        holder.reportId.setText(models.get(position).getReportId());
        //animation to determain the display the view

        Animation animation = AnimationUtils.loadAnimation(c,android.R.anim.slide_in_left);
        //start animation
        holder.itemView.startAnimation(animation);

        // use when you want to put each item data to same activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //get data from item clicked
                String name = models.get(pos).getUsername();
                String type = models.get(pos).getReportType();
                String report = models.get(pos).getReport();
                String id = models.get(pos).getReportId();


                Intent intent = new Intent(c ,ReportDetails.class);
                intent.putExtra("name", name);
                intent.putExtra("type", type);
                intent.putExtra("report", report);
                intent.putExtra("id", id);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new ReportCustomFilter(filterList, this);
        }
        return filter;
    }
}
