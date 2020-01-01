package com.surine.tustbox.Helper.Utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

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

    /**
     * 32位MD5加密
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode32(String content) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (Exception e){
            e.printStackTrace();
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
