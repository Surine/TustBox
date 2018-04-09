package com.surine.tustbox.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchZoneActivity extends TustBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.zone1)
    ImageView zone1;
    @BindView(R.id.zone2)
    ImageView zone2;
    @BindView(R.id.zone3)
    ImageView zone3;
    @BindView(R.id.zone4)
    ImageView zone4;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.zone1_r)
    RelativeLayout zone1R;
    @BindView(R.id.zone2_r)
    RelativeLayout zone2R;
    @BindView(R.id.zone3_r)
    RelativeLayout zone3R;
    @BindView(R.id.zone4_r)
    RelativeLayout zone4R;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sch_zone);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.sch_zone));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Glide.with(this).load(R.drawable.day).into(zone1);
        Glide.with(this).load(R.drawable.night).into(zone2);
        Glide.with(this).load(R.drawable.bkg_03_mar).into(zone3);
        Glide.with(this).load(R.drawable.groove_organize).into(zone4);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @OnClick({R.id.zone1_r, R.id.zone2_r, R.id.zone3_r, R.id.zone4_r})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zone1_r:
                startActivity(new Intent(this,EditCourseActivity.class).putExtra(FormData.CREATE,FormData.CREATE));
                break;
            case R.id.zone2_r:
                Toast.makeText(this, "功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.zone3_r:
                Toast.makeText(this, "功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.zone4_r:
                startActivity(new Intent(this,SendActionActivity.class).putExtra(FormData.messages_topic,"#我的神课表"));
                break;
        }
    }
}
