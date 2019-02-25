package com.surine.tustbox.Helper.Utils;

import android.content.Context;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.surine.tustbox.App.Data.FormData;

import java.io.File;

/**
 * Created by Surine on 2018/2/21.
 * 七牛云服务
 */

public class UploadQiniuService {
    static Configuration config = new Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();
    static UploadManager uploadManager = new UploadManager(config);
    private static String s;

    //构建文件名
    public static String buildFileUrl(Context context, String key){
        String tustNumber = SharedPreferencesUtil.Read(context, FormData.tust_number_server,"");
        String[] blocks = key.split("/");
        key = blocks[blocks.length-1];  //获取文件名
        key = tustNumber+"/"+key;
        //获取时间戳
        key = System.currentTimeMillis()+"/"+key;
        return key;
    }

    //构建头像文件名
    public static String buildFileHeadUrl(Context context, String key){
        String tustNumber = SharedPreferencesUtil.Read(context, FormData.tust_number_server,"");
        String[] blocks = key.split("/");
        key = blocks[blocks.length-1];  //获取文件名
        key = tustNumber+"/Head/"+key;
        //获取时间戳
        key = System.currentTimeMillis()+"/"+key;
        return key;
    }

    //上传文件
    public static String uploadFile(int size, File data, String key, String token){

        return s;
    }
}
