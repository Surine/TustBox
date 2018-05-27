package com.surine.tustbox.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

public class WebViewActivity extends TustBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String data = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        int flag = intent.getIntExtra("flag",0);
        setTitle(title);

        final WebView webView = (WebView)findViewById(R.id.webview);
        //加载网页源码还是url，默认是url（0）
        if(flag != 0){
            webView.loadDataWithBaseURL(null,data,"text/html","UTF-8",null);
        }else{
            webView.loadUrl(data);
        }


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

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
