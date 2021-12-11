package com.mahdirahmani8.weblogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.BlogCategory;
import com.mahdirahmani8.Model.followers;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.pchmn.materialchips.ChipsInput;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class write_blog extends AppCompatActivity {

    private RichEditor mEditor;
    private TextView subject, post;
    String sub = "", date, text;
    boolean isGetTmp;
    String tmp_subject, tmp_text;
    int tmp_id, like;
    int from_Activity = 0;
    int republish_id, id;
    ChipsInput chip;
    private RelativeLayout rel_tag;
    private LinearLayout lin_tag;
    List<followers> followersList = new ArrayList<>();
    APIInterface request;
//    private TextView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_blog);

        cast();

        String t;
        if (savedInstanceState != null) {
            t = savedInstanceState.getString("TEXT", "");
            mEditor.setHtml(t);
        }


        post.setOnClickListener(v -> {
            String txt = Jsoup.clean(mEditor.getHtml(), new Whitelist());
            if (txt.length() > 200) {

                if (from_Activity == 8) {
                    BlogCategory.send_edit_blog(id, subject.getText().toString().trim(), mEditor.getHtml(), date, like, write_blog.this);
                    Intent intent = new Intent(write_blog.this, MianActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    BlogCategory.sendBlog(write_blog.this, from_Activity, subject.getText().toString().trim(), republish_id, mEditor.getHtml(), isGetTmp, tmp_id);
                    Intent intent = new Intent(write_blog.this, MianActivity.class);
                    startActivity(intent);
                    finish();
                }


            } else {
                Toast.makeText(getApplicationContext(), "your blog is too short", Toast.LENGTH_LONG).show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void cast() {

        subject = findViewById(R.id.blog_sub);
        chip = findViewById(R.id.chips_input);
        rel_tag = findViewById(R.id.REL_tag);
        lin_tag = findViewById(R.id.LIN_tag);
        post = findViewById(R.id.txt_post);
        mEditor = findViewById(R.id.editor);
        mEditor.setHtml("");

        post.setText("POST");
        Intent intent = getIntent();

        sub = intent.getStringExtra("SUB");
        from_Activity = intent.getIntExtra("FROM", 1);
        republish_id = intent.getIntExtra("REPUBLISH_BLOG_ID", 0);
        like = intent.getIntExtra("LIKE", 0);
        date = intent.getStringExtra("DATE");
        id = intent.getIntExtra("ID", 0);
        text = intent.getStringExtra("TEXT");


        isGetTmp = intent.getBooleanExtra("FROM_TMP", false);
        if (isGetTmp) {
            tmp_subject = intent.getStringExtra("TMP_SUBJECT");
            tmp_text = intent.getStringExtra("TMP_TEXT");
            tmp_id = intent.getIntExtra("TMP_ID", 0);
            mEditor.setHtml(tmp_text);
            subject.setText(tmp_subject);
        }

        if (from_Activity == 0 && !isGetTmp) {
            subject.setText("Republish : " + sub);
            showSubjectAlert();

            subject.setOnClickListener(v -> showSubjectAlert());

        } else if (from_Activity == 1 && !isGetTmp) {
            subject.setText(sub);
            subject.setOnClickListener(v -> showSubjectAlert());
        } else if (isGetTmp) {
            subject.setOnClickListener(v -> showSubjectAlert());
        } else {
            subject.setText(sub);
            mEditor.setHtml(text);
            subject.setOnClickListener(v -> showSubjectAlert());

        }


        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPlaceholder("Insert text here...");


        mEditor.setOnTextChangeListener(text -> {
//                mPreview.setText(text);
        });

        findViewById(R.id.action_undo).setOnClickListener(v -> mEditor.undo());

        findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());

        findViewById(R.id.action_bold).setOnClickListener(v -> mEditor.setBold());

        findViewById(R.id.action_italic).setOnClickListener(v -> mEditor.setItalic());

        findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());

        findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());

        findViewById(R.id.action_strikethrough).setOnClickListener(v -> mEditor.setStrikeThrough());

        findViewById(R.id.action_underline).setOnClickListener(v -> mEditor.setUnderline());

        findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));

        findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));

        findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));

        findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));

        findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));

        findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(v -> mEditor.setIndent());

        findViewById(R.id.action_outdent).setOnClickListener(v -> mEditor.setOutdent());

        findViewById(R.id.action_align_left).setOnClickListener(v -> mEditor.setAlignLeft());

        findViewById(R.id.action_align_center).setOnClickListener(v -> mEditor.setAlignCenter());

        findViewById(R.id.action_align_right).setOnClickListener(v -> mEditor.setAlignRight());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> mEditor.setBlockquote());

        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> mEditor.setBullets());

        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> mEditor.setNumbers());

        findViewById(R.id.action_insert_image).setOnClickListener(v -> alert_img());

        findViewById(R.id.action_insert_youtube).setOnClickListener(v -> alert_youtobe());

        findViewById(R.id.action_insert_audio).setOnClickListener(v -> alert_music());

        findViewById(R.id.action_insert_video).setOnClickListener(v -> alert_video());

        findViewById(R.id.action_insert_link).setOnClickListener(v -> alert_link());

    }

    private void showSubjectAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        editText.setHint("write your subject ... ");
        builder.setView(view);
        builder.setTitle("Enter your subject ");
        builder.setPositiveButton("ok", (dialog, which) -> {
            String blogSubject = editText.getText().toString().trim();
            if (TextUtils.isEmpty(blogSubject)) {
                return;
            }

            subject.setText(blogSubject);

        });

        builder.setNegativeButton("cancel", (dialog, which) ->
                Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }

    private void alert_img() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle("Enter your URL ");
        builder.setPositiveButton("ok", (dialog, which) -> {
            String link = editText.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                return;
            }

//                Toast.makeText(write_blog.this, link ,Toast.LENGTH_LONG).show();
            mEditor.insertImage(link, link, 300, 220);

        });

        builder.setNegativeButton("cancel", (dialog, which) ->
                Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }

    private void alert_music() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        editText.setHint("https://exaple.com/music.mp3/");
        builder.setView(view);
        builder.setTitle("Enter your music URL ");

        builder.setPositiveButton("ok", (dialog, which) -> {
            String link = editText.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                return;
            }

            Toast.makeText(write_blog.this, link, Toast.LENGTH_LONG).show();
            mEditor.insertAudio(link);

        });

        builder.setNegativeButton("cancel", (dialog, which) ->
                Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }

    private void alert_video() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        editText.setHint("https://example.com/video.mp4/");
        builder.setView(view);
        builder.setTitle("Enter your video URL ");

        builder.setPositiveButton("ok", (dialog, which) -> {
            String link = editText.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                return;
            }

            Toast.makeText(write_blog.this, link, Toast.LENGTH_LONG).show();
            mEditor.insertVideo(link);

        });

        builder.setNegativeButton("cancel", (dialog, which) ->
                Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }

    private void alert_youtobe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        editText.setHint("https://www.youtube.com/...");
        builder.setView(view);
        builder.setTitle("Enter your youtube URL ");

        builder.setPositiveButton("ok", (dialog, which) -> {
            String link = editText.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                return;
            }

            Toast.makeText(write_blog.this, link, Toast.LENGTH_LONG).show();
            mEditor.insertYoutubeVideo(link);

        });

        builder.setNegativeButton("cancel", (dialog, which) ->
                Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }

    private void alert_link() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle("Enter your URL ");

        builder.setPositiveButton("ok", (dialog, which) -> {
            String link = editText.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                return;
            }

            Toast.makeText(write_blog.this, link, Toast.LENGTH_LONG).show();
            mEditor.insertLink(link, link);

        });

        builder.setNegativeButton("cancel", (dialog, which) -> Toast.makeText(write_blog.this, "cancel", Toast.LENGTH_LONG).show());

        builder.create().show();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("TEXT", mEditor.getHtml());
    }


    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        builder.setTitle("Save and quite ? ");
        builder.setIcon(R.drawable.ic_yellow);
        builder.setMessage("If you like working to this blog you can save and find it in archive.");

        builder.setPositiveButton("save", (dialog, which) -> {

            Toast.makeText(write_blog.this, "I saved this blog!", Toast.LENGTH_LONG).show();

            DataBase db = new DataBase(getApplicationContext());
            Date dNow = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft =
                    new SimpleDateFormat("yyyy/MM/dd");
            db.setTempBlog(db.getId(), sub, ft.format(dNow), mEditor.getHtml());

            super.onBackPressed();

        });

        builder.setNegativeButton("cancel", (dialog, which) -> {
            Toast.makeText(write_blog.this, "I don't saved this blog!", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        });

        if (isGetTmp) {

            DataBase db = new DataBase(getApplicationContext());
            db.updateTempBlog(tmp_id, mEditor.getHtml());
            Toast.makeText(getApplicationContext(), "Updated this blog!", Toast.LENGTH_LONG).show();
            super.onBackPressed();

        } else if (mEditor.getHtml().hashCode() != 0) {
            builder.create().show();

        } else {
            super.onBackPressed();
        }

        /// todo : if from archive you must update in db , and don't ask dialog ; and shouldn't empty
    }
}