package com.example.lence.bird_hunter.api;


import com.example.lence.bird_hunter.model.Birds;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @GET("index.php?r=birds/get-bird")
    Call<Birds> get();

    @FormUrlEncoded
    @POST("index.php?r=birds/auth")
    Call<ResponseBody> autor(@Field("username") String username,
                             @Field("password") String password);

}
