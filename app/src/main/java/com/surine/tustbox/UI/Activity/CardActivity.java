package com.surine.tustbox.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.EncryptionUtil;
import com.surine.tustbox.Helper.Utils.HttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CardActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.button11)
    Button button11;

    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.webview)
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        String code = "http://210.31.130.20:8088/Phone/GetValidateCode?time=" + System.currentTimeMillis();
        Log.d("YAG", code);
       // Glide.with(this).load(code).into(imageView4);
        webview.loadUrl("http://210.31.130.20:8088/Phone/Login");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
    }

    @OnClick(R.id.button11)
    public void onViewClicked() {
         login();
       // getLogin();
    }

    private void getLogin() {
        HttpUtil.get("http://210.31.130.20:8088/Phone/Login").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    private void login() {
        String sno = editText.getText().toString();
        Log.d("YAG", sno);
        String pwd = EncryptionUtil.base64_en(editText2.getText().toString());
        Log.d("YAG", pwd);
        String yzm = editText3.getText().toString();
        Log.d("YAG", yzm);
        FormBody formBody = new FormBody.Builder()
                .add("sno", sno)
                .add("pwd", pwd)
                .add("remember", "1")
                .add("uclass", "1")
                .add("yzm", yzm)
                .add("zqcode", "")
                .add("json", "ture")
                .build();


        HttpUtil.post("http://210.31.130.20:8088/Phone/Login", formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("YAG", s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
}
