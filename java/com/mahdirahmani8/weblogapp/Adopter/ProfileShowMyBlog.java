package com.mahdirahmani8.weblogapp.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.HomeAllBlog;
import com.mahdirahmani8.Model.Like;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;
import com.mahdirahmani8.weblogapp.Read;
import com.mahdirahmani8.weblogapp.write_blog;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import okhttp3.Credentials;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileShowMyBlog extends RecyclerView.Adapter<ProfileShowMyBlog.ViewHolder> {

    final LayoutInflater context;
    HomeAllBlog data;
    int blog_like;
    APIInterface request;
    Boolean isLikeOnClick;

    public ProfileShowMyBlog(Context context, HomeAllBlog data) {
        this.context = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.inflate(R.layout.item_blog_profile,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int blog_id = data.getResults().get(position).getId();
        String html = data.getResults().get(position).getText();

        holder.text.setText(
                Jsoup.clean(data.getResults().get(position).getText(),new Whitelist())
                .replace("&nbsp;"," ")
                .substring(0,90)+ " ..."
        );
        holder.txt_date.setText(data.getResults().get(position).getDate().substring(0,10).replace("-","/"));
        holder.like_count.setText(data.getResults().get(position).getLike() + " Liked,");
        holder.txt_subject.setText(data.getResults().get(position).getSubject());

        if (data.getResults().get(position).getRepublish() != 0) {
            holder.txt_reply.setText(data.getResults().get(position).getRep());
        } else {
            holder.txt_reply.setVisibility(View.GONE);
        }

        DataBase db = new DataBase(context.getContext());
        if (db.isLike(data.getResults().get(position).getId())){
            holder.img_like.setImageResource(R.drawable.is_like);
        }else {
            holder.img_like.setImageResource(R.drawable.like);
        }

        // Read
        holder.img_read.setOnClickListener(v -> {
            Intent read = new Intent(context.getContext(), Read.class);
            read.putExtra("TEXT", html);
            read.putExtra("SUBJECT", data.getResults().get(position).getSubject());
            read.putExtra("ID", data.getResults().get(position).getId());
            read.putExtra("LIKE", data.getResults().get(position).getLike());
            context.getContext().startActivity(read);
        });

        holder.txt_read.setOnClickListener(v ->{
            Intent read = new Intent(context.getContext(), Read.class);
            read.putExtra("TEXT", html);
            read.putExtra("SUBJECT", data.getResults().get(position).getSubject());
            read.putExtra("ID", data.getResults().get(position).getId());
            read.putExtra("LIKE", data.getResults().get(position).getLike());
            context.getContext().startActivity(read);
        });

        // Like
        holder.img_like.setOnClickListener(v -> {
            if (!db.isLike(blog_id)) {

                db.likeBlog(blog_id, 1);
                holder.img_like.setImageResource(R.drawable.is_like);
                int newLikeCount = like(1, data.getResults().get(position).getId(), position);
                holder.like_count.setText(newLikeCount + " Liked,");
                if (newLikeCount > 1000){
                    holder.like_count.setText(newLikeCount / 1000 + "K Liked,");
                }
                isLikeOnClick = false;
            } else {//          update dislike in database
                db.desLikeBlog(blog_id, 1);
                holder.img_like.setImageResource(R.drawable.like);
                int newLikeCount =  like(-1, data.getResults().get(position).getId(), position);
                holder.like_count.setText(newLikeCount + " Liked,");
                if (newLikeCount > 1000){
                    holder.like_count.setText(newLikeCount / 1000 + "K Liked,");
                }
                isLikeOnClick = true;
            }
        });

        // Delete
        holder.img_delete.setOnClickListener(v -> deleteOnClick(data.getResults().get(position).getSubject(),
                data.getResults().get(position).getId(),
                position
        ));

        // preview
        holder.itemView.setOnLongClickListener(v -> {
            showDialog(data.getResults().get(position).getSubject(),
                    Jsoup.clean(data.getResults().get(position).getText(),new Whitelist())
                            .substring(0,150).replace("&nbsp;"," ") + " ...",
                    html,
                    position);
            return false;
        });

        // Edit
        holder.img_edit.setOnClickListener(v -> {
            Intent intent = new Intent(context.getContext(), write_blog.class);
            intent.putExtra("TEXT",data.getResults().get(position).getText());
            intent.putExtra("SUB",data.getResults().get(position).getSubject());
            intent.putExtra("REPUBLISH_BLOG_ID",data.getResults().get(position).getRepublish());
            intent.putExtra("LIKE",data.getResults().get(position).getLike());
            intent.putExtra("DATE",data.getResults().get(position).getDate());
            intent.putExtra("FROM",8);
            intent.putExtra("ID",data.getResults().get(position).getId());

            context.getContext().startActivity(intent);
        });

        holder.itemView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {
                if (!db.isLike(blog_id)) {
                    holder.img_like.setImageResource(R.drawable.is_like);
                    holder.gifView.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        holder.gifView.setVisibility(View.GONE);
                        DataBase db = new DataBase(context.getContext());
                        db.likeBlog(data.getResults().get(holder.getPosition()).getId(), db.getId());
                    }, 1200);   // After 1.2s
//
                    int newLikeCount =like(1, data.getResults().get(holder.getPosition()).getId(), holder.getPosition());
                    holder.like_count.setText(newLikeCount + " Liked,");
                    if (newLikeCount > 1000){
                        holder.like_count.setText(newLikeCount / 1000 + "K Liked,");
                    }

//                    isLikeOnClick = false;
                } else {
                    db.desLikeBlog(blog_id, 1);
                    holder.img_like.setImageResource(R.drawable.like);
                    int newLikeCount = like(-1, data.getResults().get(holder.getPosition()).getId(), holder.getPosition());
                    holder.like_count.setText(newLikeCount + " Liked,");
                    if (newLikeCount > 1000){
                        holder.like_count.setText(newLikeCount / 1000 + "K Liked,");
                    }
                    isLikeOnClick = true;

                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return data.getResults().size();
    }

    private int like(int bool, int blog_id, int position) {

//        holder.gifView.setVisibility(View.GONE);

        blog_like = data.getResults().get(position).getLike();
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        Call<Like> call = request.likeBlog(
                Credentials.basic("mahdi", "M@hdi1380*"),
                blog_id,
                blog_like + bool
        );

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {

            }
        });
        data.getResults().get(position).setLike(data.getResults().get(position).getLike() + bool);
        return blog_like + bool;
    }

    private void deleteOnClick(String sub, int ID, int del_index) {

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context.getContext())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle(sub)
                .setMessage("Are you sure you want to delete " + "'" + sub + "' ?")
                .setIcon(R.drawable.ic)
                .addButton("DELETE", -1, -1,
                        CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {

                            request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
                            Call<ResponseBody> call = request.deleteBlog(APIInterface.auth, ID);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    Toast.makeText(context.getContext(),
                                            "Delete"
                                            , Toast.LENGTH_LONG).show();

                                    data.getResults().remove(data.getResults().get(del_index));
                                    filterList(data);


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                            dialog.dismiss();
                        });

// Show the alert
        builder.show();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(HomeAllBlog filterList) {

        data = filterList;
        notifyDataSetChanged();
    }

    private void showDialog(String sub, String msg, String html, int position) {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context.getContext())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                .setTitle(sub)
                .setMessage(msg)
                .setIcon(R.drawable.ic)
                .addButton("READ MORE", -1, -1,
                        CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {

                            Intent read = new Intent(context.getContext(), Read.class);
                            read.putExtra("TEXT", html);
                            read.putExtra("SUBJECT", data.getResults().get(position).getSubject());
                            read.putExtra("ID", data.getResults().get(position).getId());
                            read.putExtra("LIKE", data.getResults().get(position).getLike());

                            context.getContext().startActivity(read);
                            dialog.dismiss();
                        });

// Show the alert
        builder.show();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_subject,text,txt_reply,like_count,txt_date,txt_read;
        ImageView img_like,img_edit,img_delete, img_read;
        GifImageView gifView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_subject = itemView.findViewById(R.id.subject_blog_profile);
            text = itemView.findViewById(R.id.txt_blog);
            txt_reply = itemView.findViewById(R.id.tv_reply_profile);
            like_count = itemView.findViewById(R.id.tv_like_count);
            txt_date = itemView.findViewById(R.id.tv_date);
            txt_read = itemView.findViewById(R.id.tv_read);

            img_like = itemView.findViewById(R.id.like_blog_profile);
            img_edit = itemView.findViewById(R.id.edit_blog_profile);
            img_delete = itemView.findViewById(R.id.img_trash);
            img_read = itemView.findViewById(R.id.img_READ);
            gifView = itemView.findViewById(R.id.love_blog);
        }
    }
}
