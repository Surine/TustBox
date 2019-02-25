package com.surine.tustbox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class V5_WebViewActivity extends TustBaseActivity {


    @BindView(R.id.webView)
    com.tencent.smtt.sdk.WebView webView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this,false);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        context = this;

        Intent intent = getIntent();
        String url = intent.getStringExtra(Constants.URL);
        int flag = intent.getIntExtra("flag",-1);

        if(url != null){
            if(flag == -1){
                webView.loadUrl(url);
            }else{
                webView.loadDataWithBaseURL(null,url,"text/html","UTF-8",null);
            }
        }

        webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                return true;
            }
        });

    }



}
