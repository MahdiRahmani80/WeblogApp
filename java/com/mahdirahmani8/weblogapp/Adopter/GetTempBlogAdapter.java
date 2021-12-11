package com.mahdirahmani8.weblogapp.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;
import com.mahdirahmani8.weblogapp.write_blog;

import java.util.List;

public class GetTempBlogAdapter extends RecyclerView.Adapter<GetTempBlogAdapter.ViewHolder> {

    LayoutInflater context;
    List<Blog> data ;
    public GetTempBlogAdapter(Context context,List<Blog> data) {
        this.context = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.inflate(R.layout.temp_blog_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataBase db = new DataBase(context.getContext());
        int pos = data.size() - position -1;
        holder.subject.setText(db.getTempBlog(db.getId()).get( pos ).getSubject() );
        holder.date.setText(db.getTempBlog(db.getId()).get(pos).getDate() );

        holder.trash.setOnClickListener(v -> {

            db.deleteTempBlog(data.get(pos).getId());

            // todo : show Alert

            data.remove(pos);
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context.getContext() , write_blog.class );
            intent.putExtra ("TMP_SUBJECT",db.getTempBlog(db.getId()).get( pos ).getSubject());
            intent.putExtra("TMP_TEXT",db.getTempBlog(db.getId()).get( pos ).getText());
            intent.putExtra("FROM_TMP", true );
            intent.putExtra("TMP_ID",data.get(pos).getId());
            context.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView subject,date;
        ImageView trash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.txt_subject);
            date = itemView.findViewById(R.id.txt_date);
            trash = itemView.findViewById(R.id.img_trash);
        }
    }
}
