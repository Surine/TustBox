package com.surine.tustbox.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.surine.tustbox.Fragment.course.Course_info_Fragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

/**
 * Created by surine on 2017/4/5.
 * the coure_infoActivty maked for the course information
 */

public class Course_InfoActivity extends TustBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.course_info_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.course_info));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
          actionBar.setDisplayHomeAsUpEnabled(true);
        }
        replaceFragment(Course_info_Fragment.getInstance(getString(R.string.course_info)));
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
