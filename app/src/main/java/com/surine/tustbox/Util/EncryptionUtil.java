package com.surine.tustbox.Util;

import android.util.Base64;

/**
 * Created by surine on 2017/9/12.
 */

public class EncryptionUtil {

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
