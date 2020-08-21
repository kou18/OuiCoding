package com.example.ouicoding.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL="http://192.168.1.6/OuiCoding/";
    private static Retrofit retrofit= null;


    public static Api getClient(){
        if(retrofit== null)
        {
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        Api api=retrofit.create(Api.class);
        return api;
    }
}
