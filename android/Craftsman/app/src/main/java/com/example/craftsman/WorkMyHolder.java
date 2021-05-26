package com.example.craftsman;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WorkMyHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView workImg, certificate;
    TextView title, category, price, call,address, id , description;
    ItemClickListener itemClickListener;

    WorkMyHolder( View itemView) {
        super(itemView);
        workImg = itemView.findViewById(R.id.modelImageW);
        certificate = itemView.findViewById(R.id.modelImageCertificate);
        title = itemView.findViewById(R.id.modelTitletv);
        category = itemView.findViewById(R.id.modelCategory);
        price = itemView.findViewById(R.id.modelPrice);
        call = itemView.findViewById(R.id.modelCall);
        address = itemView.findViewById(R.id.modelAddTv);
        id = itemView.findViewById(R.id.modelIdtv);
        description = itemView.findViewById(R.id.modelDescriptiontv);

        certificate.setVisibility(View.GONE);
        id.setVisibility(View.GONE);
        description.setVisibility(View.GONE);

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
