package com.surine.tustbox.Util;

import android.content.Context;

/**
 * Created by surine on 2017/11/17.
 * APP管理工具
 */

public class AppUtil {

    /**
     * 获取APP VersionName
     * */

    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

}
