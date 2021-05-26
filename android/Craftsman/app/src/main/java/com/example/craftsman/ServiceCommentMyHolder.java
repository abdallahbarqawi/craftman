package com.example.craftsman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceCommentMyHolder extends RecyclerView.ViewHolder{

    TextView comment;
    public ServiceCommentMyHolder(@NonNull View itemView) {
        super(itemView);
        comment = itemView.findViewById(R.id.modelComment);
    }
}
