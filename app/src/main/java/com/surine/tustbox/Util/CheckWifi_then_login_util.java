package com.surine.tustbox.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.surine.tustbox.Data.FormData;

import okhttp3.FormBody;

/**
 * Created by surine on 17-7-6.
 */

public class CheckWifi_then_login_util {
    //3种网络状态（wifi链接，gprs链接，断网）
    private static final int NETWORK_WIFI_CONNECTION = 1;
    private static final int NETWORK_MOBILE_CONNECTION = 2;
    private static final int NETWORK_DISCONNECTION = 0;
    static int status = 0;
    //获取网络状态
    public static int getNetWorkStatus(Context context) {
        //连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果连接管理器不为null
        if (connectivityManager != null) {
            //通过连接管理器获取网络连接状态
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
             //如果网络状态不为null并且网络已经连接
            if (networkInfo != null && networkInfo.isConnected()) {
                //根据网络信息状态获取连接种类
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_WIFI_CONNECTION;
                }
                return NETWORK_MOBILE_CONNECTION;
            }
        }
        //返回未连接
        return NETWORK_DISCONNECTION;
    }

    public static FormBody LoginNetWork(final Context mContext) {
        String id = SharedPreferencesUtil.Read(mContext,"TUST_NUMBER_NETWORK","111111");
        String pwd = EncryptionUtil.base64_de(SharedPreferencesUtil.Read(mContext,"TUST_PSWD_NETWORK","111111"));
        FormBody formBody = new FormBody.Builder()
                .add("DDDDD", id)
                .add("R1", FormData.R1)
                .add("R2", FormData.R2)
                .add("R7", FormData.R7)
                .add("upass", pwd)
                .add("para", FormData.para)
                .add("0MKKey", FormData.MKKey)
                .add("R6", FormData.R6)
                .build();
        return formBody;
    }
}
