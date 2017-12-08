package com.example.lence.bird_hunter.ui;


import android.util.Log;

import com.example.lence.bird_hunter.api.App;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    LoginMVP mLoginMVP;

    public LoginPresenter(LoginMVP loginMVP) {
        mLoginMVP = loginMVP;
    }

    public void autor(String username, String password) {
        App.getApi().autor(username, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Error", response.message() + " " + response.code());
                if (response.body() != null) {
                    try {
                        Log.e("autor", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    mLoginMVP.show(response.message() + " " + response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mLoginMVP.show("t");
            }
        });
    }
}
