package com.surine.tustbox.UI.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.CheckWifi_then_login_util;
import com.surine.tustbox.Helper.Utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ShortActivity extends TustBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpUtil.post(UrlData.net_post_url, CheckWifi_then_login_util.LoginNetWork(ShortActivity.this)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShortActivity.this, R.string.fail,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(s.contains(FormData.Drcom_pc_Login_success)){
                            //成功
                            Toast.makeText(ShortActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
                        }else{
                            //账号密码错误
                            Toast.makeText(ShortActivity.this,R.string.wrong,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        finish();
    }
}
