package com.surine.tustbox.Util;

import com.surine.tustbox.NetWork.JavaNetCookieJar;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static java.lang.String.valueOf;

/**
 * Created by surine on 2017/7/9.
 */

public class HttpUtil {
    //request status
    static  Request request;
    //init the  okhttp(client and cookiejar)
    public static OkHttpClient initOkhttp(long timeout){
        //init the okhttpclient and set the cookiejar for the data request
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS);//设置连接超时时间;
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        return okHttpClient;
    }

    //request callback
    //parameter and return
    // url : request url
    // formbody : post formbody
    //return request
    public static Request HttpConnect(String url,FormBody formbody) {
        if (formbody == null) {
            request = new Request.Builder().url(url).build();
        } else {
            //start the request
            request = new Request.Builder().post(formbody).url(url).build();
        }
        return request;
    }

    //get
    public static Call get(String url){
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS);//设置连接超时时间;
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        return okHttpClient.newCall(new Request.Builder().url(url).build());
    }

    //post
    public static Call post(String url,FormBody formBody){
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS);//设置连接超时时间;
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        return okHttpClient.newCall(new Request.Builder().post(formBody).url(url).build());
    }

//    //post file
    public static Call post_file(String url, final Map<String, Object> map,File file){
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS);//设置连接超时时间;
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        return okHttpClient.newCall(new Request.Builder().post(requestBody.build()).url(url).build());
    }
}
