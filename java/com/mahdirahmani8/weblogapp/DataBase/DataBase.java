package com.mahdirahmani8.weblogapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.Model.MiniBlog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    Context context;

    public DataBase(@Nullable Context context) {
        super(context, info_db.DATABASE_NAME, null, info_db.DATABASE_VERSION);
        this.context = context;
        isDataBase();
    }

    private void isDataBase() {
        File check = new File(info_db.PACKAGE);
        if (check.exists()) {

        } else {
            check.mkdirs();
        }

        check = context.getDatabasePath(info_db.DATABASE_NAME);
        if (check.exists()) {

        } else {

            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = null;
        myInput = context.getAssets().open(info_db.DATABASE_SOURCE);

        String outFileName = info_db.PACKAGE + info_db.DATABASE_NAME;

        OutputStream myOutput = null;
        myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean login() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE 'login' SET " + info_db.ISLOGIN + "=1 WHERE " + info_db.ID + "=1;";
        db.execSQL(query);

        db.close();
        return true;
    }

    public boolean logout() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE 'login' SET " + info_db.ISLOGIN + " = 0 WHERE " + info_db.ID + "=1;";
        db.execSQL(query);

        db.close();
        return false;
    }


    int status;

    public boolean isLogin() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM 'login' WHERE id=1;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {

            do {

                status = cursor.getInt(cursor.getColumnIndex(info_db.ISLOGIN));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'profile' WHERE id=1;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(info_db.USER_ID));
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return id;
    }

    public void setId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE 'profile' SET " + info_db.USER_ID + "=" + userId + " WHERE " + info_db.ID + " =1";

        db.execSQL(query);
        db.close();
    }

    public void setProfile(String email, String name, String bio) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE 'profile' SET " +
                info_db.USER_NAME + " = '" + name + "'," +
                info_db.USER_EMAIL + " = '" + email + "'," +
                info_db.USER_BIO + "='" + bio +
                "' WHERE " + info_db.ID + "=1;";

        db.execSQL(query);
        db.close();
    }

    public String getName() {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'profile' WHERE id=1;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(info_db.USER_NAME));
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return name;
    }

    public String getBio() {
        String bio = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'profile' WHERE id=1;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                bio = cursor.getString(cursor.getColumnIndex(info_db.USER_BIO));
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return bio;
    }

    public String getEmail() {
        String email = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'profile' WHERE id=1;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            do {
                email = cursor.getString(cursor.getColumnIndex(info_db.USER_EMAIL));
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return email;
    }

    public boolean likeBlog(int blogID, int userID) {

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        int bool = 0;
        String query = "";

        query = "INSERT INTO 'like_blog' (" + info_db.BLOG_ID +
                "," + info_db.IS_LIKE + "," + info_db.USER_ID_BLOG +
                ") VALUES (" + blogID + ",1," + userID + "); ";
        db.execSQL(query);


        db.close();
        db_read.close();
        return true;
    }

    public boolean desLikeBlog(int blogID, int userID) {

        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE 'like_blog' SET '" + info_db.IS_LIKE + "' = 0 WHERE ('" +
//                info_db.BLOG_ID + "','" + info_db.USER_ID_BLOG + "') = (" + blogID + "," + userID + ") ;";
//        db.execSQL(query);

        String query=" DELETE FROM 'like_blog' WHERE  " +
                info_db.BLOG_ID +"="+ blogID + ";";
        db.execSQL(query);
        db.close();
        return true;
    }

    public boolean isLike(int blogId) {

        SQLiteDatabase db = this.getReadableDatabase();
        int bool = 0;
        String query = "SELECT * FROM 'like_blog' WHERE " + info_db.BLOG_ID + " = " + blogId;
//        db.execSQL(query);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                bool = cursor.getInt(cursor.getColumnIndex(info_db.IS_LIKE));
            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        if (bool == 1) {
            return true;
        } else {
            return false;
        }
    }


    public void setTempBlog(int user, String subject, String data , String textBlog){

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        int bool = 0;
        String query = "";

        query = "INSERT INTO 'temp_blog' ('" + info_db.TEMP_SUB + "','" + info_db.TEMP_TEXT +"','"+
                info_db.TEMP_DATE + "','"+ info_db.TEMP_USER + "'" +
                " ) VALUES ('" +  subject + "','" + textBlog.replace("&nbsp;"," ")+"','"+ data + "','"
                + user + "'); ";
        db.execSQL(query);


        db.close();
        db_read.close();

    }

    public List<Blog> getTempBlog(int user){

        List<Blog> blogList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM 'temp_blog' WHERE " + info_db.TEMP_USER + " = " + user+";";
//        db.execSQL(query);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                Blog blog = new Blog();
                blog.setId(cursor.getInt(cursor.getColumnIndex("id")));
                blog.setSubject( cursor.getString(cursor.getColumnIndex(info_db.TEMP_SUB)));
                blog.setText( cursor.getString(cursor.getColumnIndex(info_db.TEMP_TEXT)));
                blog.setDate( cursor.getString(cursor.getColumnIndex(info_db.TEMP_DATE)));
//                blog.setSubject( cursor.getString(cursor.getColumnIndex(info_db.TEMP_SUB)));

                blogList.add(blog);
            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        return blogList;
    }

    public void deleteTempBlog (int id){
     SQLiteDatabase db = this.getWritableDatabase();
     String query= "DELETE FROM 'temp_blog' WHERE id = "+ id+";";

     db.execSQL(query);
     db.close();

    }

    public void updateTempBlog(int id, String text){
        SQLiteDatabase db = this.getWritableDatabase();
        String query= " UPDATE 'temp_blog' SET "+ info_db.TEMP_TEXT + "= '"+text+"' WHERE id = "+id+" ;";

        db.execSQL(query);
        db.close();

    }

    public void addSaveBlog (int like,int republish, String rep,String date,int blogID, String username, int userID, String subject, String text, int type ){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "";

        query = "INSERT INTO 'pin' ( 'like','rep','republish','date','blogId','subject','text','userName','userId','type') VALUES ('"+like+"' ,'"+rep+"' ,'"+republish+"' ,'"+date+"','"+blogID+
                "','"+subject+"','"+text+"','"+username+"','"+userID+ "','"+type+"' ); ";
        db.execSQL(query);


        db.close();
    }

    public void removeSaveBlog( int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query= "DELETE FROM 'pin' WHERE blogId = "+ id+";";

        db.execSQL(query);
        db.close();
    }

    public boolean isSavedBlog ( int id ){
        SQLiteDatabase db = this.getReadableDatabase();
        int bool = 0;
        String query = "SELECT * FROM 'pin' WHERE blogId = " + id+";" ;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                bool = cursor.getInt(cursor.getColumnIndex("blogId"));
            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();

        if (bool != 0 ) {
            return true;
        } else {
            return false;
        }
    }

    public List<Blog> getSavedBlogs(int type){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'pin' ;" ;
        List<Blog> blogList = new ArrayList<Blog>();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                Blog blog = new Blog();
                blog.setId(cursor.getInt(cursor.getColumnIndex("blogId")));
                blog.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
                blog.setText(cursor.getString(cursor.getColumnIndex("text")));
                blog.setUsername(cursor.getString(cursor.getColumnIndex("userName")));
                blog.setUser(cursor.getInt(cursor.getColumnIndex("userId")));
                blog.setDate(cursor.getString(cursor.getColumnIndex("date")));
                blog.setUser_img(cursor.getString(cursor.getColumnIndex("imgPro")));
                blog.setLike(cursor.getInt(cursor.getColumnIndex("like")));
                blog.setRep(cursor.getString(cursor.getColumnIndex("rep")));
                blog.setRepublish(cursor.getInt(cursor.getColumnIndex("republish")));

                blogList.add(blog);
            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();
        return blogList;
    }

    public List<MiniBlog> getSavedMiniBlogs(int type){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM 'pin' WHERE 'type' = "+type+" ;" ;
        List<MiniBlog> blogList = new ArrayList<MiniBlog>();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                MiniBlog blog = new MiniBlog();
                blog.setId(cursor.getInt(cursor.getColumnIndex("blogId")));
//                blog.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
                blog.setText(cursor.getString(cursor.getColumnIndex("text")));
                blog.setUsername(cursor.getString(cursor.getColumnIndex("userName")));
                blog.setUser(cursor.getInt(cursor.getColumnIndex("userId")));

                blogList.add(blog);
            } while (cursor.moveToNext());

        }

        db.close();
        cursor.close();
        Collections.reverse(blogList);
        return blogList;
    }
}
