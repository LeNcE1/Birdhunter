package com.example.lence.bird_hunter.api;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {
    private static Api sApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://bird.bsu.ru/basic/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sApi = retrofit.create(Api.class);
    }

    public static Api getApi() {
        return sApi;
    }
}
