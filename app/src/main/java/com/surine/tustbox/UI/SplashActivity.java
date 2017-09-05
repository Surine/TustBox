package com.surine.tustbox.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends TustBaseActivity {
    ImageView view;
    private int week_from_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        view = (ImageView) findViewById(R.id.imageView);

        //full screen setting
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //delayed intent
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //get the server week
                getServerWeek();
            }
        }, 300);

        //TODO:我们期望在这里，请求服务器获取当前的周，然后将数据保存在本地
    }

    //intent: start intent based on login status
    private void Intent() {
        //start login
        if (!SharedPreferencesUtil.Read(SplashActivity.this,"is_login", false)) {
            //intent
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("other_user","no");
            startActivity(intent);
            //start mainActivity
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }


    /*
       * get the server data（week）
        * */
    private void getServerWeek() {
        HttpUtil.get(UrlData.getServerWeek).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this,"自动更新周失败",Toast.LENGTH_SHORT).show();
                    }
                });
                Intent();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    week_from_server = Integer.parseInt(jsonObject.getString("week"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferencesUtil.Save(SplashActivity.this,"choice_week",week_from_server);
                Intent();
            }
        });
    }

}
