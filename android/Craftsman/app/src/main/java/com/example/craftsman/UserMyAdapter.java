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

public class UserMyAdapter extends RecyclerView.Adapter<UserMyHolder> implements Filterable {



    Context c ;
    ArrayList<UserModel> models , filterList;
    UserCustomFilter filter;
    public UserMyAdapter(Context c, ArrayList<UserModel> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;

    }

    @NonNull
    @Override
    public UserMyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_model,null);
        return new UserMyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMyHolder holder, int position) {
        holder.user_id.setText(models.get(position).getUserId());
        holder.user_name.setText(models.get(position).getUserName());
        holder.user_email.setText(models.get(position).getUserEmail());
        holder.user_phone.setText(models.get(position).getUserPhone());
        holder.user_address.setText(models.get(position).getUserAddress());
        holder.user_state.setText(models.get(position).getUserState());

        Animation animation = AnimationUtils.loadAnimation(c,android.R.anim.slide_in_left);
        //start animation
        holder.itemView.startAnimation(animation);

        // use when you want to put each item data to same activity
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //get data from item clicked
                String name = models.get(pos).getUserName();
                String id = models.get(pos).getUserId();
                String email = models.get(pos).getUserEmail();
                String address = models.get(pos).getUserAddress();
                String state = models.get(pos).getUserState();
                String phone = models.get(pos).getUserPhone();


                Intent intent = new Intent(c ,UserDetails.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("phone", phone);
                intent.putExtra("state", state);
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
            filter = new UserCustomFilter( filterList ,this);
        }
        return filter;
    }
}
