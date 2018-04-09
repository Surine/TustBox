package com.surine.tustbox.Init;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lzy.ninegrid.NineGridView;
import com.surine.tustbox.Util.GlideImageLoader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;

import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by surine on 2017/5/3.
 */

public class TustBoxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init the litepal
        LitePalApplication.initialize(getApplicationContext());
        NineGridView.setImageLoader(new GlideImageLoader());

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

