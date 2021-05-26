package com.example.craftsman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserMyHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView user_id, user_name,user_phone, user_address,user_email, user_state;
    ItemClickListener itemClickListener;

    public UserMyHolder(@NonNull View itemView) {
        super(itemView);
        user_id = itemView.findViewById(R.id.modelUserId);
        user_name = itemView.findViewById(R.id.modelUserName);
        user_phone = itemView.findViewById(R.id.modelUserPhone);
        user_address = itemView.findViewById(R.id.modelUserAddress);
        user_email = itemView.findViewById(R.id.modelUserEmail);
        user_state = itemView.findViewById(R.id.modelUserState);

        user_id.setVisibility(View.GONE);
        user_email.setVisibility(View.GONE);
        user_state.setVisibility(View.GONE);

        itemView.setOnClickListener( this);
    }


    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view , getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
