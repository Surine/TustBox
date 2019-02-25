package com.surine.tustbox.Helper.Utils;

import android.app.Activity;
import android.content.Context;

import com.surine.tustbox.Helper.Interface.UpdateUIListenter;

/**
 * Created by Surine on 2019/2/21.
 */

public class RunOnUiThread{

    /**
     * 在UI线程执行动作
     * */
    public static void updateUi(Context context, final UpdateUIListenter updateUIListenter){
        if(context == null || !(context instanceof Activity)){
            return;
        }
        Activity a = (Activity) context;
        try {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUIListenter.update();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
