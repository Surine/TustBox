package com.surine.tustbox.UI.Activity;

import android.os.Bundle;

import com.surine.tustbox.UI.Fragment.setting.SetBackgroundFragment;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;

public class UIPictureActivity extends TustBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏UI
        SystemUI.setStatusBarUI(this,true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uipicture);
        //加载碎片
        loadFragment(R.id.content, new SetBackgroundFragment());
    }
}
