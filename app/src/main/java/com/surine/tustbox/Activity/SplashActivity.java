package com.surine.tustbox.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

public class SplashActivity extends TustBaseActivity {
    ImageView view;
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
                Intent();
            }
        }, 300);

        //TODO:我们期望在这里，请求服务器获取当前的周，然后将数据保存在本地
    }

    //intent: start intent based on login status
    private void Intent() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

        //start login
        if (!pref.getBoolean("is_login", false)) {
            //intent
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("other_user","no");
            if (android.os.Build.VERSION.SDK_INT > 21) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, view, "transitionImg").toBundle());
            } else {
                startActivity(intent);
            }
            //start mainActivity
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }


}
