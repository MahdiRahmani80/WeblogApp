package com.mahdirahmani8.weblogapp.Adopter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Comment;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetComments_Adopter extends RecyclerView.Adapter<GetComments_Adopter.ViewHolder> {

    private LayoutInflater context;
    List<Comment> data ;
    boolean isWriteOnClick;
    APIInterface request;

    public GetComments_Adopter(Context context, List<Comment> data) {
        this.context = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = context.inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (data.get(position).getReply() != 0){
            holder.tv_comment.setText(data.get(position).getText() );
            holder.reply_blog.setText("Reply : " + data.get(position).getReply_username());
            holder.user.setText(data.get(position).getUsername());
            holder.itemView.setPadding(65,0,0,0);
        }else{
            holder.tv_comment.setText( data.get(position).getText() );
            holder.reply_blog.setVisibility(View.GONE);
            holder.user.setText(data.get(position).getUsername());
            holder.itemView.setPadding(0,0,0,0);
        }

        holder.like_count.setText(data.get(position).getLike() +"" );

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWriteOnClick = !isWriteOnClick;
                if (isWriteOnClick) {
                    holder.like.setVisibility(View.GONE);
                    holder.like_count.setVisibility(View.GONE);
                    holder.reply.setVisibility(View.GONE);
                    holder.user.setVisibility(View.GONE);

                    holder.reply_post.setVisibility(View.VISIBLE);
                    holder.reply_txt.setVisibility(View.VISIBLE);

                    holder.reply_post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            holder.like.setVisibility(View.VISIBLE);
                            holder.like_count.setVisibility(View.VISIBLE);
                            holder.reply.setVisibility(View.VISIBLE);
                            holder.user.setVisibility(View.VISIBLE);

                            holder.reply_post.setVisibility(View.GONE);
                            holder.reply_txt.setVisibility(View.GONE);

                            sendReplyComment(

                                    data.get(position).getBlog(),
                                    data.get(position).getName(),
                                    holder.reply_txt.getText().toString(),
                                    position,
                                    0,
                                    holder.reply_txt.getText().toString().trim(),
                                    data.get(position).getSort()+1,
                                    v
                            );
                        }
                    });

                }else{
                    holder.like.setVisibility(View.VISIBLE);
                    holder.like_count.setVisibility(View.VISIBLE);
                    holder.reply.setVisibility(View.VISIBLE);

                    holder.reply_post.setVisibility(View.GONE);
                    holder.reply_txt.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendReplyComment( int blogID, int userID , String text, int position, int likeOrDislike ,String txt, int sort,View v) {
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);

        DataBase db = new DataBase(context.getContext());
        Call<Comment> call = request.createBlogComment(
                Credentials.basic("mahdi", "M@hdi1380*"),
                txt,
                db.getId(),
                data.get(position).getId(),
                data.get(position).getUsername(),
                data.get(position).getBlog(),
                data.get(position).getLike() + likeOrDislike,
                sort
        );

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
//                if (response.code() == 201 || response.code() == 200 ) {
                    notifyDataSetChanged();
                    Toast.makeText(context.getContext(), "0k", Toast.LENGTH_SHORT);
//                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void ShowSnackbar(View v) {

        final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
        View customSnackView = context.inflate(R.layout.success_bottom, null);

        // set the background of the default snackbar as transparent
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // set padding of the all corners as 0
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();
    }


    // class view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_comment ,like_count,user,reply_blog;
        ImageView like,reply,reply_post;
        EditText  reply_txt;
        RelativeLayout REL_comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_comment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like_comment);
            reply = itemView.findViewById(R.id.write_comment);
            like_count = itemView.findViewById(R.id.like_count);
            reply_txt = itemView.findViewById(R.id.et_reply);
            reply_post = itemView.findViewById(R.id.reply_comment);
            user = itemView.findViewById(R.id.username);
            reply_blog = itemView.findViewById(R.id.reply);
            REL_comment = itemView.findViewById(R.id.REL_comment);

        }

    }
}
