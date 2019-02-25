package com.surine.tustbox.UI.Widget.Task;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.AddTaskActivity;

/**
 * Created by Surine on 2018/8/16.
 */

public class TaskWidgetProvider extends AppWidgetProvider {
    int i;
    int appid;

    // 接收广播的回调函数
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_task);
        //设置数据
        Intent serviceIntent = new Intent(context, TaskWidgetService.class);
        intent.setData(Uri.fromParts("content", "" + i++, null));
        remoteView.setRemoteAdapter(R.id.listview, serviceIntent);
        remoteView.setOnClickPendingIntent(R.id.add, getPendingIntent(context));

        //更新
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, TaskWidgetProvider.class));
        am.updateAppWidget(appWidgetIds, remoteView);
        am.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview);

    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddTaskActivity.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }


    // onUpdate() 在更新 widget 时，被执行，
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appid = appWidgetIds[0];
    }


    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    // 第一个widget被创建时调用
    @Override
    public void onEnabled(Context context) {
    }

    // 最后一个widget被删除时调用
    @Override
    public void onDisabled(Context context) {
    }
}
