package com.mahdirahmani8.weblogapp.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahdirahmani8.Model.Category;
import com.mahdirahmani8.weblogapp.R;

import java.util.ArrayList;
import java.util.List;

public class ShowCategoriesAdapter extends RecyclerView.Adapter<ShowCategoriesAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Category> data;
    public static ArrayList<Category> categoryList = new ArrayList<>() ;

    public ShowCategoriesAdapter(Context context, List<Category> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view) ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(inflater.getContext()).load(data.get(position).getImage()).into(holder.img_category);
        holder.txt_category.setText(data.get(position).getName()+"");


        holder.itemView.setOnClickListener(v -> {
            categoryList.add(data.get(position));
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_category;
        TextView txt_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img_category = itemView.findViewById(R.id.img_category);
            txt_category = itemView.findViewById(R.id.txt_category);

        }
    }

}
