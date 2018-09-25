package com.surine.tustbox.Util;

import android.content.Context;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.UI.SendActionActivity;

/**
 * Created by Surine on 2018/7/27.
 */

public class UserUtil {
    public static String getTustNumber(Context context){
        String tustNumber = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "000000");
        return  tustNumber;
    }
}
