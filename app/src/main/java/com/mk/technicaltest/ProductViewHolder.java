package com.mk.technicaltest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    ImageView productImage;
    TextView productName;
    TextView productDesc;
    TextView productPrice;
    View view;

    ProductViewHolder(View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.productImg);
        productName = itemView.findViewById(R.id.productName);
        productPrice = itemView.findViewById(R.id.productPrice);
        productDesc = itemView.findViewById(R.id.productDesc);
        view = itemView;
    }
}
