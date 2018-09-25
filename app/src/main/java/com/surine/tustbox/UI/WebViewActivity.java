package com.surine.tustbox.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.surine.tustbox.Data.Constants.TITLE;
import static com.surine.tustbox.Data.Constants.URL;

public class WebViewActivity extends TustBaseActivity {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        context = this;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(context, R.style.ToolbarTitle);


        Intent intent = getIntent();
        String data = intent.getStringExtra(URL);
        String title = intent.getStringExtra(TITLE);
        int flag = intent.getIntExtra("flag", 0);
        setTitle(title);

        //加载网页源码还是url，默认是url（0）
        if (flag != 0) {
            webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl(data);
        }


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

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
