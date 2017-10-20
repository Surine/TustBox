package com.surine.tustbox.Util;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

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


}
