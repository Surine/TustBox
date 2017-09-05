package com.surine.tustbox.UI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.surine.tustbox.Fragment.setting.AboutFragment;
import com.surine.tustbox.Fragment.setting.NoticFragment;
import com.surine.tustbox.Fragment.setting.OslFragment;
import com.surine.tustbox.Fragment.setting.Set_BackgroundFragment;
import com.surine.tustbox.Fragment.setting.SettingFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.setting));

        //preventing repeated loading (fragment)
        if (savedInstanceState == null) {
           Intent intent = getIntent();
            if(intent.getIntExtra("set_",0)==1) {
                replaceFragment(set_background);
            }else if(intent.getIntExtra("set_",0)==2){
                replaceFragment(about);
            }else if(intent.getIntExtra("set_",0)==4){
                replaceFragment(osl);
            }else if(intent.getIntExtra("set_",0)==5) {
              //notic
                replaceFragment(notic);
            }else{
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


    //set the back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
