package com.example.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import model.AddToCart;


public class myCartAdapter extends RecyclerView.Adapter<myCartAdapter.MyViewHolder> {

    Context context;

    ArrayList<AddToCart> list;


    public myCartAdapter(Context context, ArrayList<AddToCart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orderitem,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AddToCart addToCart = list.get(position);
        holder.itemName.setText(addToCart.getItem());
        holder.itemPrice.setText(addToCart.getPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemname);
            itemPrice = itemView.findViewById(R.id.itemprice);

        }
    }


}
