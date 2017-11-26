package com.example.lence.bird_hunter.api;




import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("/users")
    Call<ResponseBody> get();


    @GET("/posts/{id}")
    Call<ResponseBody> post(@Path("id") String id);

}
