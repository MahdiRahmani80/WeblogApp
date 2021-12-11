package com.mahdirahmani8.weblogapp;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Comment;
import com.mahdirahmani8.Model.Like;
import com.mahdirahmani8.weblogapp.Adopter.GetComments_Adopter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Read extends AppCompatActivity implements View.OnClickListener {

    TextView subject;
    ImageView like, save, post, write, share, show_comment;
    EditText comment,et_reply;
    WebView view;
    FrameLayout rel_comment;
    RecyclerView  rec_comment;

    String html, str_subject;
    int id, blog_like;
    boolean isCommentOnClick =false, isLikeOnClick;

    APIInterface request;
    boolean show_UI;

    GetComments_Adopter adopter;
    List<Comment> commentList;
    public static int last_sort =0 ;
    boolean firstSend = false;

    // menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        cast();
        save.setVisibility(GONE);
        getComment();


        // set font in web view
        Typeface font = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");
        WebSettings webSettings = view.getSettings();
        webSettings.setFixedFontFamily(String.valueOf(font));
        webSettings.setDefaultFontSize(18);

        DataBase db = new DataBase(getApplicationContext());
        if(db.isLike(id)){
            like.setImageResource(R.drawable.is_like);
        }else{
            like.setImageResource(R.drawable.like);
        }

    }


    @SuppressLint({"CutPasteId", "SetJavaScriptEnabled"})
    private void cast() {

        subject = findViewById(R.id.subject);
        like = findViewById(R.id.like);
        save = findViewById(R.id.save);
        post = findViewById(R.id.post);
        comment = findViewById(R.id.et_comment);
        view = findViewById(R.id.html_view);
        write = findViewById(R.id.write);
        show_comment = findViewById(R.id.message);
        share = findViewById(R.id.share);
        rel_comment = findViewById(R.id.REL_comment);
        rec_comment = findViewById(R.id.REC_comment);
        et_reply = findViewById(R.id.et_comment);


        // for recycler view
        rec_comment.setHasFixedSize(true);
        rec_comment.setLayoutManager(new LinearLayoutManager(Read.this));

        // set On Click
        like.setOnClickListener(this);
        save.setOnClickListener(this);
        post.setOnClickListener(this);
        comment.setOnClickListener(this);
        write.setOnClickListener(this);
        show_comment.setOnClickListener(this);
        share.setOnClickListener(this);

        comment.setVisibility(GONE);
        post.setVisibility(GONE);

        Intent intent = getIntent();
        html = intent.getStringExtra("TEXT");
        str_subject = intent.getStringExtra("SUBJECT");
        id = intent.getIntExtra("ID", 0);
        blog_like = intent.getIntExtra("LIKE", 0);

        // set
        subject.setText(str_subject);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int view_id = v.getId();
        switch (view_id) {
            case R.id.like:
                like();
                break;
            case R.id.save:

                break;
            case R.id.share:
                share();
                break;
           case R.id.write:
                write();
                break;
            case R.id.message:
                comment();
                break;
            case R.id.et_comment:

                break;
            case R.id.post:
                onClickPost(v);
                break;

        }
    }

    private void share() {

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, str_subject );
        intent.putExtra(
                android.content.Intent.EXTRA_TEXT,
                Jsoup.clean(html, new Whitelist()).replace("&nbsp;"," ") + "\n We Blog"
        );
        startActivity(Intent.createChooser(intent,"We Blog"));
    }


    @SuppressLint("SetTextI18n")
    private void comment() {
        isCommentOnClick = !isCommentOnClick;
        getComment(); // ToDo : add refresh and delete this for beauty and low server traffic

        if(isCommentOnClick) {
            subject.setText("Comments");
            view.setVisibility(GONE);
            rel_comment.setVisibility(View.VISIBLE);
            show_comment.setImageResource(R.drawable.blog);

        }else {
            subject.setText(str_subject);
            view.setVisibility(View.VISIBLE);
            rel_comment.setVisibility(GONE);
            show_comment.setImageResource(R.drawable.message);

        }

    }

    private void getComment() {
        Call<List<Comment>> call = request.getcomments(
                Credentials.basic("mahdi", "M@hdi1380*"),
                id,
                "sort"
        );

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.code() == 200){

                    commentList = response.body();
                    adopter = new GetComments_Adopter(Read.this, commentList);
                    rec_comment.setAdapter(adopter);

                }else {
                    Toast.makeText(Read.this,"We have some Error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(Read.this,"We have some Error",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void write() {
        show_UI = !show_UI;
        if (show_UI) {
            like.setVisibility(GONE);
            save.setVisibility(GONE);
            share.setVisibility(GONE);
            write.setVisibility(GONE);
            show_comment.setVisibility(GONE);

            post.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);

        } else {

            like.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            write.setVisibility(View.VISIBLE);
            show_comment.setVisibility(View.VISIBLE);

            post.setVisibility(GONE);
        }
    }

    private void onClickPost( View v ) {

        if (comment.getText().toString().hashCode() != 0){
            sendComment(id /*1 todo get from db */, comment.getText().toString().trim(), v);
        }

        like.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        share.setVisibility(View.VISIBLE);
        write.setVisibility(View.VISIBLE);
        show_comment.setVisibility(View.VISIBLE);

        post.setVisibility(GONE);
        comment.setVisibility(GONE);
    }

    private void like() {

        DataBase db =new DataBase(getApplicationContext());
        if ( db.isLike(id) ) {
            like.setImageResource(R.drawable.like);
            blog_like =  sendRequest(-1);
            db.desLikeBlog(id,db.getId());
        } else {
            like.setImageResource(R.drawable.is_like);
            blog_like = sendRequest(1);
            db.likeBlog(id,db.getId());
        }

    }

    private int sendRequest(int bool) {

        Call<Like> call = request.likeBlog(
                Credentials.basic("mahdi", "M@hdi1380*"),
                id,
                blog_like + bool
        );

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {

//                Toast.makeText(Read.this, response.message(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {

            }
        });

        return blog_like+ bool;
    }


    private void sendComment( int blogID,String txt, View v) {

        Call<Comment> call = request.createBlogCommentInRead(
                Credentials.basic("mahdi", "M@hdi1380*"),
                txt,
                1, // my user id  todo : get from data base
                blogID,
                0,
                sort_int(!firstSend)
        );
        firstSend = true;

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.code() == 201){
                    // todo refresh page
//                    ShowSnackbar(v);
                    comment.setText("");
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    getComment();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });

    }

    public void ShowSnackbar(View v) {

        final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
        @SuppressLint("InflateParams")
        View customSnackView = getLayoutInflater().inflate(R.layout.success_bottom, null);

        // set the background of the default snack bar as transparent
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snack bar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // set padding of the all corners as 0
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();
    }

    private int sort_int( boolean isFirst) {

        if (isFirst) {
            for (int i = 0; i < commentList.size(); i++) {
                int check = commentList.get(i).getReply();
                if (check == 0) {
                    last_sort += 100;
                }
            }
        }
        last_sort += 100;
        return last_sort;
    }

}