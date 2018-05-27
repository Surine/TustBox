package com.surine.tustbox.Init;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jph.takephoto.model.InvokeParam;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/3/28.
 * BaseActivity:管理活动
 */

public class TustBaseActivity extends AppCompatActivity {


    //固定字体大小（禁止缩放）
    @Override
    public Resources getResources() {
        // TODO Auto-generated method stub
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印活动名
        Log.d("BaseActivity", getClass().getSimpleName());
        //活动管理器添加活动
        ActivityCollector.addActivity(this);

      //  if(!getClass().getSimpleName().equals("MainActivity")){
            SystemUI.StatusUISetting(this,"#30000000");
      //  }

      //  setTheme(R.style.BaseAPPDark);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCollector.removeActivity(this);
    }

    public static void KillAPP(){
        ActivityCollector.finishAll();
    }


    static class ActivityCollector {
        public static List<Activity> activities = new ArrayList<>();

        public static void addActivity(Activity activity) {
            activities.add(activity);
        }

        public static void removeActivity(Activity activity) {
            activities.remove(activity);
        }

        public static void finishAll() {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

}
