package com.surine.tustbox.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.surine.tustbox.NetWork.JavaNetCookieJar;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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


    public static String getIPAddress(Context context) {
        //获取系统链接服务信息
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        //如果已经链接
        if (info != null && info.isConnected()) {
            //如果使用2/3/4G网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //获取移动网络IP
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                //如果使用无线
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                //获取无线管理器
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //通过管理器换取无线信息
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //通过无线信息获取无线IP
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
