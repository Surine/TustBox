package com.surine.tustbox.UI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.course.EditCourseFragment;
import com.surine.tustbox.Fragment.setting.SettingFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Surine on 2018/3/8.
 */

public class EditCourseActivity extends TustBaseActivity {


    @BindView(R.id.setting_toolbar)
    Toolbar courseEditToolbar;
    @BindView(R.id.share_setting_fragment)
    FrameLayout settingFragment;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        ButterKnife.bind(this);

        setSupportActionBar(courseEditToolbar);
        setTitle("编辑课程");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        id = getIntent().getIntExtra(FormData.id, 0);
        if(getIntent().getStringExtra(FormData.CREATE) != null){
            id = -1;  //id = -1是新建的意思
        }
        EditCourseFragment prefFragment = new EditCourseFragment(id);
        replaceFragment(prefFragment);
    }

    //overload
    private void replaceFragment(PreferenceFragment prefFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.share_setting_fragment, prefFragment);
        transaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.ok:
                if(id == -1){
                    //新建
                    EventBus.getDefault().post(new SimpleEvent(11,"ok"));
                }else{
                    //更新
                    EventBus.getDefault().post(new SimpleEvent(9,"ok"));
                }
                break;
            case R.id.delete:
                 //删除
                EventBus.getDefault().post(new SimpleEvent(10,"delete"));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return true;
    }

}
