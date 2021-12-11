package com.mahdirahmani8.weblogapp.Adopter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBlog_Adopter extends RecyclerView.Adapter<GetBlog_Adopter.ViewHolder> {

    private final LayoutInflater context;
    static HomeAllBlog blog;
    int blog_like;
    public static int pos =0 ;
    String txt = "";
    public static String MY_IMG ="";

    boolean isLikeOnClick;
    APIInterface request;
    DataBase db;


    public GetBlog_Adopter(Context context, HomeAllBlog blog) {
        this.context = LayoutInflater.from(context);
        this.blog = blog;
        db = new DataBase(this.context.getContext());
    }

    public static void change(HomeAllBlog h) {
        blog = h;
    }

    @NonNull
    @Override
    public GetBlog_Adopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.inflate(R.layout.blog_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int blog_id = blog.getResults().get(position).getId();

        String sub = blog.getResults().get(position).getSubject();
        holder.subject.setText(sub);
        holder.user.setText(blog.getResults().get(position).getUsername());
        String html = blog.getResults().get(position).getText();
        txt = Jsoup.clean(html, new Whitelist());
        txt = txt.replace("&nbsp;", " ").substring(0, 90) + " ...";
        holder.text.setText(txt);
        int likeCount = blog.getResults().get(position).getLike();
        holder.like_count.setText(likeCount + " Liked,");
        holder.date.setText(blog.getResults().get(position).getDate().replace("-","/").substring(0,10));

        // TODO : Follow

        String userImg = blog.getResults().get(position).getUser_img();
//        MY_IMG = blog.getResults().get(0).getUser().getImgPro();

        if ( userImg != null && userImg.trim().length() > 44 ){
            Glide.with(context.getContext()).load(userImg).into( holder.civ);
        } else {
            Glide.with(context.getContext()).load(R.drawable.ic).into( holder.civ);
        }


//         show status like
        if (db.isLike(blog_id)) {
            holder.like.setImageResource(R.drawable.is_like);
        } else {
            holder.like.setImageResource(R.drawable.like);
        }

        if (db.isSavedBlog(blog_id)) {
            holder.save.setImageResource(R.drawable.is_pin);
        } else {
            holder.save.setImageResource(R.drawable.pin);
        }

        if ( db.getId() == blog.getResults().get(position).getUser()) {
            holder.republish.setVisibility(View.GONE);
            holder.save.setVisibility(View.GONE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);


        }else {
            holder.republish.setVisibility(View.VISIBLE);
            holder.save.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        int finalPosition9 = position;
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), write_blog.class);
                intent.putExtra("TEXT",blog.getResults().get(finalPosition9).getText());
                intent.putExtra("SUB",blog.getResults().get(finalPosition9).getSubject());
                intent.putExtra("REPUBLISH_BLOG_ID",blog.getResults().get(finalPosition9).getRepublish());
                intent.putExtra("LIKE",blog.getResults().get(finalPosition9).getLike());
                intent.putExtra("DATE",blog.getResults().get(finalPosition9).getDate());
                intent.putExtra("FROM",8);
                intent.putExtra("ID",blog.getResults().get(finalPosition9).getId());

                context.getContext().startActivity(intent);
            }
        });

        int finalPosition2 = position;
        holder.delete.setOnClickListener(v -> deleteOnClick(blog.getResults().get(finalPosition2).getSubject(),
                blog.getResults().get(finalPosition2).getId(),
                finalPosition2
        ));


        int finalPosition8 = position;
        holder.save.setOnClickListener(v -> {

            if(db.isSavedBlog(blog_id)){
                holder.save.setImageResource(R.drawable.pin);
                db.removeSaveBlog(blog_id);
                blog.getResults().remove(blog.getResults().get(position));
                filterList(blog);

            }else {
                holder.save.setImageResource(R.drawable.is_pin);
                int blogID = blog.getResults().get(finalPosition8).getId();
                String username = blog.getResults().get(finalPosition8).getUsername();
                String subj = blog.getResults().get(finalPosition8).getSubject();
                String text = blog.getResults().get(finalPosition8).getText();
                String date = blog.getResults().get(finalPosition8).getDate();
                int like = blog.getResults().get(finalPosition8).getLike();
                int republish = blog.getResults().get(finalPosition8).getRepublish();
                String rep = blog.getResults().get(finalPosition8).getRep();
                int owner = blog.getResults().get(finalPosition8).getUser();

                db.addSaveBlog(like,republish,rep,date,blogID,username,owner,subj,text,0);
            }
        });

        int user_rep = blog.getResults().get(position).getRepublish();
        if (user_rep != 0) {
            holder.republish_user.setVisibility(View.VISIBLE);
            holder.republish_user.setText("Republished blog : " + blog.getResults().get(position).getRep());
        } else {
            holder.republish_user.setVisibility(View.GONE);
        }

        int finalPosition = position;
        holder.itemView.setOnLongClickListener(v -> {
            showDialog(blog.getResults().get(finalPosition).getSubject(),
                    Jsoup.clean(blog.getResults().get(finalPosition).getText(),new Whitelist())
                            .substring(0,150).replace("&nbsp;"," ") + " ...",
                    html,
                    finalPosition);
            return false;
        });

        int finalPosition1 = position;
        holder.like.setOnClickListener(v -> {
//
            if (!db.isLike(blog_id)) {

                db.likeBlog(blog_id, 1);
                holder.like.setImageResource(R.drawable.is_like);
                int newLikeCount = like(1, blog.getResults().get(finalPosition1).getId(), finalPosition1, holder);
                holder.like_count.setText(newLikeCount + " Liked,");
                if (newLikeCount > 1000){
                    holder.like_count.setText(newLikeCount / 1000 + "K Liked,");
                }
                isLikeOnClick = false;
            } else {//          update dislike in database
                db.desLikeBlog(blog_id, 1);
                holder.like.setImageResource(R.drawable.like);
                int newLikeCount =  like(-1, blog.getResults().get(finalPosition1).getId(), finalPosition1, holder);
                holder.like_count.setText(newLikeCount + "");
                if (newLikeCount > 1000){
                    holder.like_count.setText(newLikeCount / 1000 + "K");
                }
                isLikeOnClick = true;
            }
        });

        // double like click
        int finalPosition3 = position;
        int finalPosition4 = position;
        holder.itemView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {

                if (!db.isLike(blog_id)) {
                    holder.like.setImageResource(R.drawable.is_like);
                    holder.gifView.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        holder.gifView.setVisibility(View.GONE);
                        DataBase db = new DataBase(context.getContext());
                        db.likeBlog(blog.getResults().get(finalPosition3).getId(), db.getId());
                    }, 1200);   // After 1.2s
//
                    like(1, blog.getResults().get(finalPosition4).getId(), finalPosition4, holder);

//                    isLikeOnClick = false;
                } else {
                    db.desLikeBlog(blog_id, 1);
                    holder.like.setImageResource(R.drawable.like);
                    like(-1, blog.getResults().get(finalPosition4).getId(), finalPosition4, holder);
                    isLikeOnClick = true;
                    
                }
                // set Action like instagram
            }
        }));

        int finalPosition5 = position;
        holder.republish.setOnClickListener(v -> {

            Intent intent = new Intent(context.getContext(), write_blog.class);
            intent.putExtra("SUB", blog.getResults().get(finalPosition5).getSubject());
            intent.putExtra("FROM", 0);
            intent.putExtra("REPUBLISH_BLOG_ID",blog.getResults().get(finalPosition5).getId() );
            context.getContext().startActivity(intent);
        });

        int finalPosition6 = position;
        pos = finalPosition6;
        holder.read.setOnClickListener(v -> {

            Intent read = new Intent(context.getContext(), Read.class);
            read.putExtra("TEXT", html);
            read.putExtra("SUBJECT", blog.getResults().get(finalPosition6).getSubject());
            read.putExtra("ID", blog.getResults().get(finalPosition6).getId());
            read.putExtra("LIKE", blog.getResults().get(finalPosition6).getLike());
            context.getContext().startActivity(read);
        });

        int finalPosition7 = position;
        holder.read_more.setOnClickListener(v -> {

            Intent read = new Intent(context.getContext(), Read.class);
            read.putExtra("TEXT", html);
            read.putExtra("SUBJECT", blog.getResults().get(finalPosition7).getSubject());
            read.putExtra("ID", blog.getResults().get(finalPosition7).getId());
            read.putExtra("LIKE", blog.getResults().get(finalPosition7).getLike());
            context.getContext().startActivity(read);
        });
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

                                    blog.getResults().remove(blog.getResults().get(del_index));
                                    filterList(blog);


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

        blog = filterList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return blog.getResults().size();
    }


    private int like(int bool, int blog_id, int position, ViewHolder holder) {

//        holder.gifView.setVisibility(View.GONE);

        blog_like = blog.getResults().get(position).getLike();
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        Call<Like> call = request.likeBlog(
                Credentials.basic("mahdi", "M@hdi1380*"),
                blog_id,
                blog_like + bool
        );

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {

                if (response.code() == 201 || response.code() == 200 ) {
                    holder.like_count.setText( (blog_like+ bool) + " Liked,");
                }

            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {

            }
        });
        blog.getResults().get(position).setLike(blog.getResults().get(position).getLike() + bool);
        return blog_like + bool;
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
                            read.putExtra("SUBJECT", blog.getResults().get(position).getSubject());
                            read.putExtra("ID", blog.getResults().get(position).getId());
                            read.putExtra("LIKE", blog.getResults().get(position).getLike());

                            context.getContext().startActivity(read);
                            dialog.dismiss();
                        });

// Show the alert
        builder.show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView subject, user, text, read_more, republish_user,follow,like_count,date;
        ImageView republish, save, like, read, edit, delete;

        RelativeLayout rel_blog;
        GifImageView gifView;
        CircleImageView civ;

        public ViewHolder( View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            text = itemView.findViewById(R.id.text);
            user = itemView.findViewById(R.id.username);
            read_more = itemView.findViewById(R.id.read);
            republish_user = itemView.findViewById(R.id.republish_user);

            edit = itemView.findViewById(R.id.edit);
            date = itemView.findViewById(R.id.date_blog);
            delete = itemView.findViewById(R.id.delete);
            republish = itemView.findViewById(R.id.republish);
            save = itemView.findViewById(R.id.save);
            like = itemView.findViewById(R.id.like);
            read = itemView.findViewById(R.id.read_more);
            rel_blog = itemView.findViewById(R.id.RELblog);
            gifView = itemView.findViewById(R.id.love_blog);
            civ = itemView.findViewById(R.id.img_user);

            follow = itemView.findViewById(R.id.txt_follow);
            like_count = itemView.findViewById(R.id.txt_date);

        }
    }

}