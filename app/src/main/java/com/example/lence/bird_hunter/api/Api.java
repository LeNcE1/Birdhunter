package com.example.lence.bird_hunter.api;


import com.example.lence.bird_hunter.model.Birds;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Api {
    @GET("index.php?r=birds/get-bird")
    Call<Birds> get();

    @FormUrlEncoded
    @POST("index.php?r=birds/auth")
    Call<ResponseBody> autor(@Field("username") String username,
                             @Field("password") String password);

//    @Multipart
//    @POST("index.php?r=birds/coords-from-app")
//    Call<ResponseBody> dispatch(@Query("id") int id,
//                                @Query("x") String x,
//                                @Query("y") String y,
//                                @Query("bird") String bird,
//                                @Part List<MultipartBody.Part> file);


    @Multipart
    @POST("index.php?r=birds/coords-from-app")
    Call<ResponseBody> dispatch(@PartMap() Map<String, RequestBody> info,
                                @Part List<MultipartBody.Part> file);

}
