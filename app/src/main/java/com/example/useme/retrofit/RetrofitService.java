package com.example.useme.retrofit;

import android.util.Log;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.50.115:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        Log.d("RETROFIT", "retrofit initialized");
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
