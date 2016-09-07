package com.mmednet.common.retrofit2;


import com.mmednet.common.retrofit2.interf.GankApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alpha on 2016/8/31.
 */
public class NetWork {

    private static GankApi ganApi;
    private static OkHttpClient client = new OkHttpClient();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    public static GankApi getGanApi(){

        Retrofit retrofit= new Retrofit.Builder()
                .client(client)
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();

        return retrofit.create(GankApi.class);
    }
}
