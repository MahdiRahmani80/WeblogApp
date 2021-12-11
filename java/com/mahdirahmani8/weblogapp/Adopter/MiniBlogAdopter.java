package com.mahdirahmani8.weblogapp.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdirahmani8.Model.AllMiniBlogs;
import com.mahdirahmani8.weblogapp.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class MiniBlogAdopter  extends RecyclerView.Adapter<MiniBlogAdopter.ViewHolder>{

    AllMiniBlogs data;
    private final LayoutInflater context;

    public MiniBlogAdopter(AllMiniBlogs data, Context context) {
        this.data = data;
        this.context = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.inflate(R.layout.miniblog_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.user.setText(data.getResults().get(position).getUsername());

        String txt = data.getResults().get(position).getText();
        holder.text.setText(Jsoup.clean(txt,new Whitelist()).replace("&nbsp;"," "));
        holder.like_count.setText(data.getResults().get(position).getLike()+" Liked,");
        holder.date.setText(data.getResults().get(position).getDate().substring(0,10).replace("-","/"));


        if (data.getResults().get(position).getRepublishd().getRepublished() != 0){
            holder.republish_user.setText(data.getResults().get(position).getRep());
        }else {
            holder.republish_user.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {

                // todo : create Like
                Toast.makeText(context.getContext(),"TODO : Like!",Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return data.getResults().size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView  user, text, read_more, republish_user,like_count,date;
        ImageView republish, save, like, read, edit, delete;

        RelativeLayout rel_blog;
        GifImageView gifView;
        CircleImageView civ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            user = itemView.findViewById(R.id.username);
            read_more = itemView.findViewById(R.id.read);
            republish_user = itemView.findViewById(R.id.republish_user);

            edit = itemView.findViewById(R.id.edit);
            date = itemView.findViewById(R.id.txt_mini_date);
            delete = itemView.findViewById(R.id.delete);
            republish = itemView.findViewById(R.id.republish);
            save = itemView.findViewById(R.id.save);
            like = itemView.findViewById(R.id.like);
            read = itemView.findViewById(R.id.read_more);
            rel_blog = itemView.findViewById(R.id.RELblog);
            gifView = itemView.findViewById(R.id.love_blog);
            civ = itemView.findViewById(R.id.img_user);
            like_count = itemView.findViewById(R.id.txt_date);
        }
    }
}
