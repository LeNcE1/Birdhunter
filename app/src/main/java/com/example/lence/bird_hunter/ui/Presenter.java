package com.example.lence.bird_hunter.ui;


import android.util.Log;

import com.example.lence.bird_hunter.R;
import com.example.lence.bird_hunter.api.App;
import com.example.lence.bird_hunter.model.Birds;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter {
    MVP mMVP;

    public Presenter(MVP MVP) {
        mMVP = MVP;
    }

    public void loadBirds() {
        App.getApi().get().enqueue(new Callback<Birds>() {
            @Override
            public void onResponse(Call<Birds> call, Response<Birds> response) {
                //Log.e("Response",response.body().getBirds().toString());
                mMVP.addBirds(response.body().getBirds());
            }

            @Override
            public void onFailure(Call<Birds> call, Throwable t) {
                Log.e("Response", t.toString());
                mMVP.showError();
            }
        });

    }

    public void sendBirds(HashMap<String, RequestBody> map, List<MultipartBody.Part> file) {
        App.getApi().dispatch(map, file).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("sendBirdsResponse", response.code() + " " + response.message());
                if (response.code() == 200) {
                    mMVP.show("Данные успешно отправлены");
                    mMVP.clear();
                } else
                    mMVP.show("Ошибка отравки данных");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mMVP.show("onFailure");
            }
        });
//        App.getApi().dispatch(id,x,y,bird,file).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.e("sendBirdsResponse",response.code()+" "+response.message());
//                if (response.code()==200){
//                    mMVP.show("Данные успешно отправлены");
//                    mMVP.clear();
//                }
//                else
//                    mMVP.show("Ошибка отравки данных");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                mMVP.show("onFailure");
//            }
//        });
    }

}
