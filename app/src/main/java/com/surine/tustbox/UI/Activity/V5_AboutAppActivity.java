package com.surine.tustbox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.tencent.bugly.beta.Beta;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.surine.tustbox.Helper.Utils.AppUtil.getVersionName;

public class V5_AboutAppActivity extends TustBaseActivity {

    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.l1)
    Button l1;
    @BindView(R.id.a2)
    Button a2;
    @BindView(R.id.t3)
    Button t3;
    @BindView(R.id.u4)
    Button u4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this, true);
        setContentView(R.layout.activity_v5_about_app);
        ButterKnife.bind(this);

        version.setText(getVersionName(this));
    }

    @OnClick({R.id.l1, R.id.a2, R.id.t3, R.id.u4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.l1:
                loadUrl(UrlData.license);
                break;
            case R.id.a2:
                loadUrl(UrlData.agreement);
                break;
            case R.id.t3:
                loadUrl(UrlData.thanks);
                break;
            case R.id.u4:
                Beta.checkUpgrade(true,false);
                break;
        }
    }

    private void loadUrl(String s) {
        Intent intent = new Intent(V5_AboutAppActivity.this,V5_WebViewActivity.class);
        intent.putExtra(Constants.URL,s);
        startActivity(intent);
    }
}
