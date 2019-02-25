package com.surine.tustbox.Helper.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by surine on 2017/4/22.
 */

public class IOUtil {
    /**
     * 删除文件夹里面的所有文件
     */
    public static void delAllFile(String path, Context context) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            Toast.makeText(context,"无缓存",Toast.LENGTH_SHORT).show();
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }
            else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path+"/"+ tempList[i],context);//先删除文件夹里面的文件
                delFolder(path+"/"+ tempList[i],context);//再删除空文件夹
            }
        }
    }

    /**
     * 删除空文件夹
     */
    public static void delFolder(String folderPath,Context context) {
        try {
            delAllFile(folderPath,context); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹

        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void openFile(String filePath, String fileType, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        Uri data = Uri.fromFile(new File(filePath));
        intent.setDataAndType(data, getMap(fileType));
        //Toast.makeText(context, "uri:" + data.toString(), Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    private static String getMap(String key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("rar", "application/x-rar-compressed");
        map.put("jpg", "image/jpeg");
        map.put("zip", "application/zip");
        map.put("pdf", "application/pdf");
        map.put("doc", "application/msword");
        map.put("docx", "application/msword");
        map.put("wps", "application/msword");
        map.put("xls", "application/vnd.ms-excel");
        map.put("et", "application/vnd.ms-excel");
        map.put("xlsx", "application/vnd.ms-excel");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("txt", "text/html");
        map.put("mp3", "audio/mpeg");
        map.put("mp4", "video/mp4");
        map.put("mp4","video/rmvb");
        map.put("3gp", "video/3gpp");
        map.put("wav", "audio/x-wav");
        map.put("avi", "video/x-msvideo");
        map.put("flv", "flv-application/octet-stream");
        map.put("", "*/*");

        return map.get(key.toLowerCase());
    }


    /**判断是否是Video
    * @param fileName 文件名
    * */
    public static boolean isVideo(String fileName) {
        fileName = fileName.toLowerCase();
        if(fileName.endsWith(".mp4")
                || fileName.endsWith(".avi")
                || fileName.endsWith(".mov")
                || fileName.endsWith(".rmvb")
                || fileName.endsWith("mpeg")
                || fileName.endsWith(".mkv")){
            return true;
        }
        return false;
    }
}
