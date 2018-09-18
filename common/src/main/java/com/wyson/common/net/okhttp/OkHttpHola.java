package com.wyson.common.net.okhttp;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wyson.common.base.BaseApplication;
import com.wyson.common.util.NetWorkUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author : Wuyson
 * @date : 2018/7/5-11:58
 */
public class OkHttpHola {
    private static final int READ_TIME_OUT = 7676;
    private static final int CONNECT_TIME_OUT = 7676;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    private static volatile OkHttpClient sOkHttpClient;

    private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private OkHttpClient setupOkHttp() {
        //缓存
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        //100Mb
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        Interceptor headerIntercept = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor appInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();
                if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);

                if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached , max-stale=" + CACHE_STALE_SEC)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };

        Interceptor netInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {

                }
                return null;
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(appInterceptor)
                .addNetworkInterceptor(appInterceptor)
                .addInterceptor(headerIntercept)
                .addInterceptor(logInterceptor)
                .cache(cache)
/*                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })*/
                .build();

        return okHttpClient;
    }

    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }

    public static OkHttpClient getInstance() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpHola.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpHola().setupOkHttp();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    public static String getCacheControl() {
        return NetWorkUtils.isNetConnected(BaseApplication.getAppContext()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }
}
