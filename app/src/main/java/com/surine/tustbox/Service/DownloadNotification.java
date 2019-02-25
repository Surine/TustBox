package com.surine.tustbox.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.surine.tustbox.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class DownloadNotification {

    private NotificationManager mManager;
    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private static final int mNotifiyId = 0;
    private Notification notification;

    public DownloadNotification(Context context) {
        mContext = context;
        init();
    }

    private void init() {

        //        mManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
//        //  notification = new NotificationCompat.Builder(this, "chat")
//              //  .setContentTitle("收到一条聊天消息")
//            //    .setContentText("今天中午吃什么？")
//           //     .setWhen(System.currentTimeMillis())
//            //    .setSmallIcon(R.drawable.icon)
//            //    .setAutoCancel(true)
//            //    .build();
//        mManager.notify(1, notification);



        mManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle("小天盒子下载器")
                .setContentText("拼命下载：")
                .setProgress(100, 0, false)
                .setSmallIcon(R.mipmap.ic_launcher);
        mManager.notify(mNotifiyId, mBuilder.build());
    }

    public void upload(int progress){
        if (mBuilder != null) {
            mBuilder.setProgress(100, progress, false);
            mManager.notify(mNotifiyId, mBuilder.build());
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, int importance,Context context) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }

}