package com.surine.tustbox.Manager;

import android.util.Log;

import com.surine.tustbox.NetWork.JavaNetCookieJar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Surine on 2018/9/2.
 * TODO:优化
 */

public class OkhttpManager {
    private static OkHttpClient mOkHttpClient;
    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    private static OkHttpClient init(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();

        return mOkHttpClient;
    }

    //get
    public static Call get(String url){
        init();
        return mOkHttpClient.newCall(new Request.Builder().url(url).build());
    }

    //post
    public static Call post(String url, FormBody formBody){
        init();
        return mOkHttpClient.newCall(new Request.Builder().post(formBody).url(url).build());
    }
}
