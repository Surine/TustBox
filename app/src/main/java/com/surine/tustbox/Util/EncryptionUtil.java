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

    //Base64编码
    public static String base64_en(String a){
        try {
            return Base64.encodeToString(a.getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //Base64解码
    public static String base64_de(String a){
        try {
            return new String(Base64.decode(a.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
