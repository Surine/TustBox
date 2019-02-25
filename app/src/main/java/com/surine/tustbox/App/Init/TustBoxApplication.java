package com.surine.tustbox.App.Init;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.lzy.ninegrid.NineGridView;
import com.surine.tustbox.Helper.Utils.GlideImageLoader;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by surine on 2017/5/3.
 */

public class TustBoxApplication extends Application {

    public static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        //初始化 litepal
        LitePalApplication.initialize(getApplicationContext());
        //初始化9图
        NineGridView.setImageLoader(new GlideImageLoader());

        //初始化图片加载
        Phoenix.config().imageLoader(new ImageLoader() {
            @Override
            public void loadImage(Context context, ImageView imageView, String imagePath, int type) {
                Glide.with(context).load(imagePath).into(imageView);
            }
        });

        //初始化吐司大作战
        ToastUtil.getInstance(getApplicationContext());

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
         // 设置是否为上报进程
        UserStrategy strategy = new UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
       // CrashReport.initCrashReport(getApplicationContext(), "fe37bddc26", false);
        Bugly.init(getApplicationContext(), "fe37bddc26", false);

        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.initX5Environment(getApplicationContext(),  new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
            }
            });

    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}

