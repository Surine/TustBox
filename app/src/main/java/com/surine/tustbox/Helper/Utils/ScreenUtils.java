package com.surine.tustbox.Helper.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;

import com.surine.tustbox.App.Init.TustBoxApplication;

/**
 * Created by Surine on 2019/1/31.
 */


public class ScreenUtils {

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return 数组第一个元素宽，第二个元素高
     */
    public static int[] getScreenBounds(Context context) {

        int[] screenBounds = new int[2];

        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        screenBounds[0] = displayMetrics.widthPixels;
        screenBounds[1] = displayMetrics.heightPixels;
        return screenBounds;
    }

    /**
     * 根据dip返回当前设备上的px值
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, int dip) {
        int px = 0;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        px = (int) (dip * density);
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int sp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, TustBoxApplication.context.getResources().getDisplayMetrics());
    }

    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        return density;
    }

    public static int getTouchSlop(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        return configuration.getScaledTouchSlop();
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 这个方法是将view的左上角坐标存入数组中.此坐标是相对当前activity而言.
     * 若是普通activity,则y坐标为可见的状态栏高度+可见的标题栏高度+view左上角到标题栏底部的距离.
     * 可见的意思是:在隐藏了状态栏/标题栏的情况下,它们的高度以0计算.
     * 若是对话框式的activity,则y坐标为可见的标题栏高度+view到标题栏底部的距离. 此时是无视状态栏的有无的.
     *
     * @param view
     * @return
     */
    public static int[] getLocationInWindow(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        System.out.println("getLocationInWindow:" + position[0] + ","
                + position[1]);
        return position;
    }

    /**
     * 这个方法跟上面的差不多,也是将view的左上角坐标存入数组中.但此坐标是相对整个屏幕而言. y坐标为view左上角到屏幕顶部的距离.
     *
     * @param view
     * @return
     */
    public static int[] getLocationOnScreen(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        System.out.println("getLocationOnScreen:" + position[0] + ","
                + position[1]);
        return position;
    }

    /**
     * 这个方法是构建一个Rect用来"套"这个view.此Rect的坐标是相对当前activity而言.
     * 若是普通activity,则Rect的top为可见的状态栏高度+可见的标题栏高度+Rect左上角到标题栏底部的距离.
     * 若是对话框式的activity,则y坐标为Rect的top为可见的标题栏高度+Rect左上角到标题栏底部的距离. 此时是无视状态栏的有无的.
     *
     * @param view
     * @return
     */
    public static Rect getGlobalVisibleRect(View view) {
        Rect viewRect = new Rect();
        view.getGlobalVisibleRect(viewRect);
        System.out.println(viewRect);
        return viewRect;
    }

    /**
     * 这个方法获得的Rect的top和left都是0,也就是说,仅仅能通过这个Rect得到View的宽度和高度....
     *
     * @param view
     * @return
     */
    public static Rect getLocalVisibleRect(View view) {
        Rect globeRect = new Rect();
        view.getLocalVisibleRect(globeRect);
        return globeRect;
    }

    /**
     * 获得状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

	/*
     * 注意:以上方法在OnCreate方法中调用,都会返回0,这是因为View还未加载完毕.建议在onWindowFocusChanged方法中进行获取,
	 * 有些情况下onWindowFocusChanged不好用的时候(比如ActivityGroup),可以这样写:
	 * mTextView.post(new Runnable() {
	 *
	 * @Override public void run() { Rect viewRect = new Rect();
	 * mTextView.getGlobalVisibleRect(viewRect);
	 * mTreeScrollView.setRect(viewRect); } });
	 */
    // ///////////////////////////////////////////////////////////////////////////////////
}
