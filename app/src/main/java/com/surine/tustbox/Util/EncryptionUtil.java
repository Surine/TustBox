package com.surine.tustbox.Util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by surine on 2017/9/12.
 */

public class EncryptionUtil {

    //URL编码
    public static String url_en(String a){
        try {
            return URLEncoder.encode(a,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "error";
    }

    //Base64加密
    public static String base64_en(String a){
        return Base64.encodeToString(a.getBytes(), Base64.DEFAULT);
    }

    //Base64解密
    public static String base64_de(String a){
        return new String
                (Base64.decode(a.getBytes(), Base64.DEFAULT));
    }
}
