package com.example.craftsman;

import android.widget.Filter;

import java.util.ArrayList;

public class ReportCustomFilter extends Filter {
    ReportMyAdapter adapter;
    ArrayList<ReportModel> filterList;

    public ReportCustomFilter( ArrayList<ReportModel> filterList, ReportMyAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }
    @Override
    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Filter.FilterResults results = new Filter.FilterResults();
        // check charSequence validity
        if(charSequence != null && charSequence.length()>0)
        {
            //change to uppercase
            charSequence = charSequence.toString().toUpperCase();
            //store our filtered models
            ArrayList<ReportModel> filteredModel = new ArrayList<>();
            for (int i=0;i<filterList.size(); i++)
            {
                //check
                if(filterList.get(i).getUsername().toUpperCase().contains(charSequence)){
                    //add Model to filtered models
                    filteredModel.add(filterList.get(i));
                }
            }
            results.count = filteredModel.size();
            results.values = filteredModel;
        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
        adapter.models = (ArrayList<ReportModel>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
