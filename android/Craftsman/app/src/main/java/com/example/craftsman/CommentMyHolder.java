package com.example.craftsman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentMyHolder extends RecyclerView.ViewHolder{

    TextView comment;
    public CommentMyHolder(@NonNull View itemView) {
        super(itemView);
        comment = itemView.findViewById(R.id.modelComment);
    }
}
