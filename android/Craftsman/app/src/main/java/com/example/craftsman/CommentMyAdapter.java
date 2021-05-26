package com.example.craftsman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentMyAdapter extends RecyclerView.Adapter<CommentMyHolder>{
    Context c ;
    ArrayList<CommentModel> models;
    CommentMyAdapter(Context c, ArrayList<CommentModel> models) {
        this.c = c;
        this.models = models;

    }

    @NonNull
    @Override
    public CommentMyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_model,null);
        return new CommentMyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentMyHolder holder, int position) {
        holder.comment.setText(models.get(position).getComment());
        //animation to determain the display the view

        Animation animation = AnimationUtils.loadAnimation(c,android.R.anim.slide_in_left);
        //start animation
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
