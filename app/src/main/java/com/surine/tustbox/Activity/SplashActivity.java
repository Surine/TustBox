package com.surine.tustbox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

public class SplashActivity extends TustBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //delayed intent
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent();
            }
        }, 300);
    }

    //intent: start intent based on login status
    private void Intent() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        if (!pref.getBoolean("is_login", false)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }


}
