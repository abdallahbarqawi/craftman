package com.example.craftsman;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {
    ServiceMyAdapter adapter;
    ArrayList<ServiceModel> filterList;

    public CustomFilter( ArrayList<ServiceModel> filterList, ServiceMyAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        // check charSequence validity
        if(charSequence != null && charSequence.length()>0)
        {
            //change to uppercase
            charSequence = charSequence.toString().toUpperCase();
            //store our filtered models
            ArrayList<ServiceModel> filteredModel = new ArrayList<>();
            for (int i=0;i<filterList.size(); i++)
            {
                //check
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence)){
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
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.models = (ArrayList<ServiceModel>) filterResults.values;
        adapter.notifyDataSetChanged();
    }

}
