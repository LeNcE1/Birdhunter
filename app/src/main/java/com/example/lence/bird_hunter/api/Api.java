package com.example.lence.bird_hunter.api;




import com.example.lence.bird_hunter.model.Birds;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("index.php?r=birds/get-bird")
    Call<Birds> get();


    @GET("/posts/{id}")
    Call<ResponseBody> post(@Path("id") String id);

}
