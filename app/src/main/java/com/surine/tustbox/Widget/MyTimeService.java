package com.surine.tustbox.Widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by surine on 2017/4/12.
 */

public class MyTimeService extends Service {
    private static final String TAG="MyTimeService";

    // 更新 widget 的广播对应的action
    private final String ACTION_UPDATE_ALL = "com.surine.tust_box.UPDATE_ALL";
    private Context mContext;
    // 更新周期的计数
    private int count=0;

    @Override
    public void onCreate() {
        mContext = this.getApplicationContext();

        Intent updateIntent = new Intent("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
        mContext.sendBroadcast(updateIntent);

        super.onCreate();
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * 服务开始时，即调用startService()时，onStartCommand()被执行。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

}
