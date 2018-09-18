package com.wyson.common.net.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wyson.common.net.okhttp.OkHttpHola;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHola {
    private static volatile Retrofit sRetrofit;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            synchronized (RetrofitHola.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder()
                            .client(OkHttpHola.getInstance())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl("http://gank.io/api/")
                            .build();
                }
            }
        }
        return sRetrofit;
    }

    public static Retrofit getRxInstance() {
        if (sRetrofit == null) {
            synchronized (RetrofitHola.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder()
                            .client(OkHttpHola.getInstance())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl("http://gank.io/api/")
                            .build();
                }
            }
        }
        return sRetrofit;
    }

}
