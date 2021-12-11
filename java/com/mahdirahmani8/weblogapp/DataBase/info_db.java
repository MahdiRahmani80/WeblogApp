package com.mahdirahmani8.weblogapp.DataBase;

public class info_db {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";
    public static final String PACKAGE = "data/data/com.mahdirahmani8.weblogapp/databases/";
    public static final String DATABASE_SOURCE = "db.sqlite3";

    // sign in & out
    public static final String ISLOGIN = "isLogin";
    public static final String ID = "id";

    // Profile
    public static final String USER_ID = "userID";
    public static final String USER_NAME = "name";
    public static final String USER_BIO = "bio";
    public static final String USER_EMAIL = "email";
    // like blog
    public static final String BLOG_ID = "blogID";
    public static final String IS_LIKE = "isLike";
    public static final String USER_ID_BLOG = "userID";
    // Temp blog
    public static final String TEMP_USER = "user";
    public static final String TEMP_SUB = "subject";
    public static final String TEMP_TEXT = "text";
    public static final String TEMP_DATE = "date";
    // Archive Blogs
    public static final String ARCHIVE_USER = "user";
    public static final String ARCHIVE_SUB = "subject";
    public static final String ARCHIVE_TEXT = "text";
    public static final String ARCHIVE_DATE = "date";
    public static final String ARCHIVE_IS_POST = "isPost";

}
