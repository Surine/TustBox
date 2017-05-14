package com.surine.tustbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.surine.tustbox.Fragment.Me.Me_info_fragment;
import com.surine.tustbox.Fragment.course.Course_info_Fragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

/**
 * Created by surine on 2017/4/5.
 * the coure_infoActivty maked for the course information
 */

public class ToolbarActivity extends TustBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar);

        Intent intent = getIntent();
        int flag = intent.getIntExtra("activity_flag", 0);


        Toolbar toolbar = (Toolbar) findViewById(R.id.course_info_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (flag == 0) {
            setTitle(getString(R.string.course_info));
            replaceFragment(Course_info_Fragment.getInstance(getString(R.string.course_info)));
        }else{
            setTitle(getString(R.string.my_info));
            replaceFragment(Me_info_fragment.getInstance(getString(R.string.my_info)));
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.replace(R.id.course_info_container, fragment);
        tran.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
