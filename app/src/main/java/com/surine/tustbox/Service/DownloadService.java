package com.surine.tustbox.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadTask;
import com.surine.tustbox.Helper.Utils.LogUtil;

import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadService extends Service {

    private DownloadNotification mNotify;
    private Context context;


    @Nullable
    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("DOWN_URL");
        String name = intent.getStringExtra("DOWN_NAME");

        LogUtil.d("服务下载"+url);
        LogUtil.d("服务下载"+name);

        if(url == null || name == null){
            Toast.makeText(context, "文件参数不正确！", Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }

        //任务数量判断
        List<DownloadEntity> list = Aria.download(this).getAllNotCompletTask();
        if(list != null && list.size() >= 5){
            Toast.makeText(context, "最大支持5个任务！", Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }

        Aria.download(this)
                .load(url)
                .setFilePath(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath() + "/" + name)
                .start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "download";
            String channelName = "小天盒子下载器";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mNotify.createNotificationChannel(channelId, channelName, importance,context);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("服务"+this);
        context = this;
        //初始化下载器
        Aria.download(this).register();
        Aria.init(this).getDownloadConfig()
                .setThreadNum(5).setMaxTaskNum(5).setConvertSpeed(true);
        Aria.init(this).getDownloadConfig().setReTryNum(3);
       // mNotify = new DownloadNotification(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Aria.download(this).unRegister();
        LogUtil.d("终止"+this);
    }



    @Download.onTaskStart
    public void onTaskStart(DownloadTask task) {
        Toast.makeText(this, "开始下载！", Toast.LENGTH_SHORT).show();
    }

    @Download.onTaskStop
    public void onTaskStop(DownloadTask task) {
        Toast.makeText(this, "下载暂停！", Toast.LENGTH_SHORT).show();
    }

    @Download.onTaskCancel
    public void onTaskCancel(DownloadTask task) {
        Toast.makeText(this, "下载取消！", Toast.LENGTH_SHORT).show();
    }

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task) {
        Toast.makeText(this, "下载失败！", Toast.LENGTH_SHORT).show();
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task) {

    }

}