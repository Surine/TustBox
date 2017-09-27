package com.surine.tustbox.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.surine.tustbox.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/4/9.
 * 资源来自：http://www.cnblogs.com/skywang12345/p/3264991.html
 * 使用请注明
 */

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "provider_ME";
    public static String MY_UPDATA_CHANGE = "11111";
    int i;
    int time = 1;
    public static final String BT_REFRESH_ACTION = "com.surine.tustbox.BT_REFRESH_ACTION";
    public static final String COLLECTION_VIEW_ACTION = "com.surine.tustbox.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "com.surine.tustbox.COLLECTION_VIEW_EXTRA";
    // 启动ExampleAppWidgetService服务对应的action
    private final Intent EXAMPLE_SERVICE_INTENT =
            new Intent("android.appwidget.action.EXAMPLE_APP_WIDGET_SERVICE");
    // 更新 widget 的广播对应的action
    private final String ACTION_UPDATE_ALL = "com.surine.tust_box.UPDATE_ALL";
    int appid;

    // 接收广播的回调函数
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("MyProvider", "onReceive:" + intent.getAction());

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
        //设置数据
        Intent serviceIntent = new Intent(context, WidgetService.class);
        intent.setData(Uri.fromParts("content", "" + i++, null));
        remoteView.setRemoteAdapter(R.id.gridview, serviceIntent);
        remoteView.setOnClickPendingIntent(R.id.bt_refresh, getPendingIntent(context));
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        int choose_week =  pref.getInt("choice_week",0);
        remoteView.setTextViewText(R.id.widget_week, "第"+choose_week+"周");

        //更新
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        am.updateAppWidget(appWidgetIds, remoteView);
        am.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gridview);

    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WidgetProvider.class);
        intent.setAction("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }


    // onUpdate() 在更新 widget 时，被执行，
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e("MyProvider", "onUpdate");
        appid = appWidgetIds[0];
    }


    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.e("MyProvider", "onAppWidgetOptionsChanged");

    }

    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted(): appWidgetIds.length="+appWidgetIds.length);
        super.onDeleted(context, appWidgetIds);
    }

    // 第一个widget被创建时调用
    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
    }

    // 最后一个widget被删除时调用
    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
    }
}
