package com.mahdirahmani8.API;

import com.mahdirahmani8.Model.AllMiniBlogs;
import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.Model.Category;
import com.mahdirahmani8.Model.Comment;
import com.mahdirahmani8.Model.Feed;
import com.mahdirahmani8.Model.ForgotPass;
import com.mahdirahmani8.Model.GetCode;
import com.mahdirahmani8.Model.HomeAllBlog;
import com.mahdirahmani8.Model.Like;
import com.mahdirahmani8.Model.MiniBlogFeed;
import com.mahdirahmani8.Model.NameBioImg_User;
import com.mahdirahmani8.Model.Register;
import com.mahdirahmani8.Model.User;
import com.mahdirahmani8.Model.Verify;
import com.mahdirahmani8.Model.followers;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    public static String auth = Credentials.basic("mahdi", "M@hdi1380*");

    @GET("/users/")
    Call<List<User>> getprofile(
            @Header("Authorization") String authkey,
            @Query("id") int id
    );

    @POST("/register/")
    @FormUrlEncoded
    Call<Register> createUser(
            @Header("Authorization") String authkey,
            @Field("email") String email,
            @Field("password") String password
    );

    @PUT("/resend/{email}/")
    @FormUrlEncoded
    Call<Register> resend_email(
            @Header("Authorization") String authkey,
            @Path("email") String email_path,
            @Field("email") String email,
            @Field("password") String password
    );

    //    @FormUrlEncoded
    @GET("/get/code")
    Call<List<GetCode>> getCode(
            @Query("email") String email,
            @Header("Authorization") String authkey
    );

    @PUT("/verify/{email}/")
    @FormUrlEncoded
    Call<Verify> verify(
            @Path("email") String email,
            @Header("Authorization") String authkey,
            @Field("isVerified") boolean verify
    );

    @PUT("/{email}/edit/name/")
    @Multipart
    Call<NameBioImg_User> sendBio(
            @Path("email") String email,
            @Header("Authorization") String authkey,
            @Part("name") String name,
            @Part("bio") String bio,
            @Part MultipartBody.Part file
    );

    @PUT("/{email}/edit/name/")
    @Multipart
    Call<NameBioImg_User> sendBio(
            @Path("email") String email,
            @Header("Authorization") String authkey,
            @Part("name") String name,
            @Part("bio") String bio
    );

    @GET("/verify/")
    Call<List<Verify>> login(
            @Header("Authorization") String authkey,
            @Query("email") String email,
            @Query("password") String password
    );

    @PUT("{email}/forgot/password/")
    @FormUrlEncoded
    Call<ForgotPass> forgot(
            @Header("Authorization") String authkey,
            @Path("email") String email,
            @Field("isForgotPass") boolean isForgotPass
    );

    @GET("/show/blogs/")
    Call<HomeAllBlog> getMyBlogs(
            @Header("Authorization") String authkey,
            @Query("user") int user
    );

    @GET("/show/blogs/")
    Call<HomeAllBlog> getBlogs(
            @Header("Authorization") String authkey,
            @Query("ordering") String ordering,
            @Query("page") int page
    );

    @PUT("/{id}/blog/")
    @FormUrlEncoded
    Call<Blog> like(
            @Header("Authorization") String authkey,
            @Path("id") int id,
            @Field("like") int like,
            @Field("Subject") String subject,
            @Field("text") String text,
            @Field("user") int user
    );

    @GET("/show/comments/")
    Call<List<Comment>> getcomments(
            @Header("Authorization") String authkey,
            @Query("blog") int blogID,
            @Query("ordering") String ordering
    );

    @POST("/create/comment/")
    @FormUrlEncoded
    Call<Comment> createBlogComment(
            @Header("Authorization") String authkey,
            @Field("text") String text,
            @Field("name") int user,
            @Field("reply") int reply,
            @Field("reply_username") String reply_username,
            @Field("blog") int blog,
            @Field("like") int like,
            @Field("sort") int sort
    );

    @POST("/create/comment/")
    @FormUrlEncoded
    Call<Comment> createBlogCommentInRead(
            @Header("Authorization") String authkey,
            @Field("text") String text,
            @Field("name") int user,
            @Field("blog") int blog,
            @Field("like") int like,
            @Field("sort") int sort
    );

    @PUT("/{id}/like/blog/")
    @FormUrlEncoded
    Call<Like> likeBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id,
            @Field("like") int like
    );

    @GET("/categories/")
    Call<List<Category>> getCategories(
            @Header("Authorization") String authkey
    );

    @POST("/create/blog/")
    @FormUrlEncoded
    Call<Blog> sendBlog(
            @Header("Authorization") String authkey,
            @Field("subject") String subject,
            @Field("text") String text,
            @Field("user") int user,
            @Field("follow_category[]") List<Category> follow_category
    );

    @FormUrlEncoded
    @PUT("/{id}/blog/")
    Call<Blog> editBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id,
            @Field("subject") String subject,
            @Field("text") String text,
            @Field("user") int user,
            @Field("follow_category[]") ArrayList<Category> follow_category,
            @Field("date") String date,
            @Field("like") int like
    );

    @POST("/create/blog/")
    @FormUrlEncoded
    Call<Blog> sendBlog(
            @Header("Authorization") String authkey,
            @Field("subject") String subject,
            @Field("text") String text,
            @Field("user") int user,
            @Field("republished") int republish,
            @Field("follow_category[]") ArrayList<Category> follow_category
    );

    @GET("followers")
    Call<followers> getMyFollowers(
            @Header("Authorization") String authkey,
            @Query("user") int user
    );

    @GET("/feed/")
    Call<List<Feed>> getBlog(
            @Header("Authorization") String authkey,
            @Query("user") int user,
            @Query("format") String format
    );

    @GET("/users/")
    Call<List<User>> getMyINFO(
            @Header("Authorization") String auth,
            @Query("email") String email
    );

    @DELETE("/{id}/blog/")
    Call<ResponseBody> deleteBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id
    );

    @GET("/mini/feed/")
    Call<List<MiniBlogFeed>> getMiniBlogs(
            @Header("Authorization") String authkey,
            @Query("user") int user
    );

    @GET("/show/blogs/")
    Call<HomeAllBlog> getSearch(
            @Header("Authorization") String authkey,
            @Query("search") String search,
            @Query("page") int page
    );


    @GET("/miniblog/")
    Call<AllMiniBlogs> getAllMiniBlog(
        @Header("Authorization") String authkey,
        @Query("ordering") String ordering
    );

    @GET("/miniblog/")
    Call<AllMiniBlogs> getMyMiniBlog(
            @Header("Authorization") String authkey,
            @Query("user") int user,
            @Query("ordering") String ordering
    );

    @FormUrlEncoded
    @PUT("/{id}/miniblog/")
    Call<ResponseBody> editMyMiniBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id,
            @Field("username") String username,
            @Field("text") String text
    );

    @FormUrlEncoded
    @POST("/create/miniblog/")
    Call<ResponseBody> postMyMiniBlog(
            @Header("Authorization") String authkey,
            @Field("text") String text,
            @Field("user") int user
    );

    @FormUrlEncoded
    @POST("/create/miniblog/")
    Call<ResponseBody> postMyMiniBlog(
            @Header("Authorization") String authkey,
            @Field("text") String text,
            @Field("user") int user,
            @Field("republished") int republish
    );

    @FormUrlEncoded
    @PUT("/{id}/miniblog/")
    Call<ResponseBody> LikeMiniBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id,
            @Field("like") int like
    );

    @FormUrlEncoded
    @DELETE("/{id}/miniblog/")
    Call<ResponseBody> deleteMyMiniBlog(
            @Header("Authorization") String authkey,
            @Path("id") int id
    );
}