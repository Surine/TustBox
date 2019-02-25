package com.surine.tustbox.Helper.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Surine on 2018/3/19.
 */

public class ClearDataUtil {

    private Context context;
    public ClearDataUtil(Context context){
        this.context = context;
    }

    /**
     * 清除应用所有的数据
     */
    public void clearAllDataOfApplication( String... customfilepath) {
        clearInternalCache();
        clearExternalCache();
       // clearDatabases();
        clearSharedPreference();
        clearFiles();
        if (customfilepath == null) {
            return;
        }
        for (String filePath : customfilepath) {
            clearCustomCache(filePath);
        }
    }
    /*
     *清除外部和内部缓存
     */
    public void clearAllCache() {
        clearExternalCache();
        clearInternalCache();
    }

    /**
     * 清除内部缓存(/data/data/包名/cache)
     */
    public void clearInternalCache() {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除应用SharedPreference(/data/data/包名/shared_prefs)
     */
    public void clearSharedPreference() {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * *清除所有数据库(/data/data/包名/databases)
     */
    public void clearDatabases() {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * 按名字清除数据库
     */
    public void clearDatabaseBydbName(String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/包名/files下的内容
     */
    public void clearFiles() {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除自定义路径下的文件(只支持目录下的文件删除,不支持目录)
     */
    public void clearCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }


    /**
     * 清除外部SDcard下的cache内容(/mnt/sdcard/android/data/包名/cache)
     */
    public void clearExternalCache() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 只删除文件夹下的文件
     *
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /*
     **获取文件
     **Context.getExternalFilesDir() --> SDCard/Android/data/包名/files/ 目录，一般放一些长时间保存的数据
     **Context.getExternalCacheDir() --> SDCard/Android/data/包名/cache/目录，一般存放临时缓存数据
     */

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取应用文件缓存大小
     */
    public String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 获取外部和内部缓存大小
     */
    public String getTotalCacheSize() throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 格式化缓存单位
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
       /* if (kiloByte < 1) {
            return size + "Byte";
        }*/

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
