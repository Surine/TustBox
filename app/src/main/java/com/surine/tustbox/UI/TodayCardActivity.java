package com.surine.tustbox.UI;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.SharedPreferencesUtil;

public class TodayCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ImageView imageView = (ImageView) findViewById(R.id.art_back);
        setSupportActionBar(toolbar);
        //设置toolbar颜色
        collapsingToolbarLayout.setContentScrimColor(SharedPreferencesUtil.Read(this,"TOOLBAR_C", R.color.colorPrimary));
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("ART_TITLE"));
        collapsingToolbarLayout.setStatusBarScrimColor(SharedPreferencesUtil.Read(this,"TOOLBAR_C", R.color.colorPrimary));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Glide.with(this).load(getIntent().getStringExtra("ART_IMAGE")).into(imageView);
        final WebView webView = (WebView) findViewById(R.id.web_art);
        //WebView :load url at assets
        webView.loadDataWithBaseURL(null,getIntent().getStringExtra("ART"),"text/html", "utf-8",null);
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
