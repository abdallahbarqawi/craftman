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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WorkMyAdapter extends RecyclerView.Adapter<WorkMyHolder> implements Filterable {
    Context c ;
    ArrayList<WorkModel> models , filterList;
    WorkCustomeFilter filter;
    WorkMyAdapter(Context c, ArrayList<WorkModel> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;
    }
    @NonNull
    @Override
    public WorkMyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_model,null);
        return new WorkMyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkMyHolder holder, int position) {
        //bind data to our holder

        holder.title.setText(models.get(position).getTitle());
        holder.category.setText(models.get(position).getCategory());
        holder.price.setText(models.get(position).getPrice());
        holder.address.setText(models.get(position).getAddress());
        holder.call.setText(models.get(position).getCall());
        holder.id.setText(models.get(position).getId());
        holder.description.setText(models.get(position).getDescription());
        //holder.mImageIv.setImageResource(models.get(position).getImg());
        Glide.with(c).load(models.get(position).getWorkImg()).into(holder.workImg);
        Glide.with(c).load(models.get(position).getCertificate()).into(holder.certificate);


        //animation to determain the display the view

        Animation animation = AnimationUtils.loadAnimation(c,android.R.anim.slide_in_left);
        //start animation
        holder.itemView.startAnimation(animation);
        // use when you want to put each item data to same activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String phone = models.get(pos).getCall();
                String image = models.get(pos).getWorkImg();
                String certificate = models.get(pos).getCertificate();
                String category = models.get(pos).getCategory();
                String title = models.get(pos).getTitle();
                String address = models.get(pos).getAddress();
                String id = models.get(pos).getId();
                String description = models.get(pos).getDescription();
                String price = models.get(pos).getPrice();

                Intent intent = new Intent(c , WorkDetails.class);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("category", category);
                intent.putExtra("address", address);
                intent.putExtra("id", id);
                intent.putExtra("image", image);
                intent.putExtra("certificate", certificate);
                intent.putExtra("phone", phone);
                intent.putExtra("price", price);
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
            filter = new WorkCustomeFilter(filterList, this);
        }
        return filter;
    }
}
