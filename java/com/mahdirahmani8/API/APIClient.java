package com.mahdirahmani8.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static String url = "http://weblogapp0.pythonanywhere.com/";

    public static Retrofit retrofit =null;

    public static Retrofit getAPIListClient( String URL ){

        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
