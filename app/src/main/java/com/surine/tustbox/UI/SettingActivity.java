package com.surine.tustbox.UI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.surine.tustbox.Fragment.setting.AboutFragment;
import com.surine.tustbox.Fragment.setting.NoticFragment;
import com.surine.tustbox.Fragment.setting.OslFragment;
import com.surine.tustbox.Fragment.setting.Set_BackgroundFragment;
import com.surine.tustbox.Fragment.setting.SettingFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by surine on 2017/4/8.
 * the settingActivity extends baseactivity
 * in order to solve the problem about toolbar does not show on the window
 */

public class SettingActivity extends TustBaseActivity {


    SettingFragment prefFragment = new SettingFragment();
    Set_BackgroundFragment set_background = new Set_BackgroundFragment();
    AboutFragment about = new AboutFragment();
    NoticFragment notic = new NoticFragment();
    OslFragment osl = new OslFragment();
    @BindView(R.id.setting_toolbar)
    Toolbar settingToolbar;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        ButterKnife.bind(this);
        setSupportActionBar(settingToolbar);
        context = this;
        setTitle(getString(R.string.setting));
        settingToolbar.setTitleTextAppearance(context, R.style.ToolbarTitle);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.getIntExtra("set_", 0) == 1) {
                replaceFragment(set_background);
            } else if (intent.getIntExtra("set_", 0) == 2) {
                replaceFragment(about);
            } else if (intent.getIntExtra("set_", 0) == 4) {
                replaceFragment(osl);
            } else if (intent.getIntExtra("set_", 0) == 5) {
                //notic
                replaceFragment(notic);
            } else {
                replaceFragment(prefFragment);
            }
        }
    }

    //overload
    private void replaceFragment(SettingFragment prefFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.share_setting_fragment, prefFragment);
        transaction.commit();
    }

    //overload
    private void replaceFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.replace(R.id.share_setting_fragment, fragment);
        tran.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    //set the back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
