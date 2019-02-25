package com.surine.tustbox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.Helper.Interface.AddFunctionListenter;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.UI.View.LittleProgramToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.FormData.CT;
import static com.surine.tustbox.App.Data.FormData.LOCALE_DATA;
import static com.surine.tustbox.App.Data.FormData.NORMAL_CT;
import static com.surine.tustbox.App.Data.FormData.NORMAL_UA;
import static com.surine.tustbox.App.Data.FormData.SET_COOKIE;
import static com.surine.tustbox.App.Data.FormData.TOKEN;
import static com.surine.tustbox.App.Data.FormData.UA;
import static com.surine.tustbox.App.Data.FormData.USERID;
import static com.surine.tustbox.App.Data.UrlData.loginUrl;
import static com.surine.tustbox.App.Data.UrlData.picUrl;
import static com.surine.tustbox.R.layout.activity_pan_login;
import static com.surine.tustbox.Helper.Utils.PatternUtil.Wow;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Read;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.save;

/**
 * Created by Surine on 2019/1/2.
 */

public class PanLoginActivity extends TustBaseActivity {
    @BindView(R.id.topbar)
    LittleProgramToolbar topbar;
    @BindView(R.id.pan_number)
    EditText panNumber;
    @BindView(R.id.pan_pswd)
    EditText panPswd;
    @BindView(R.id.pan_vcode)
    EditText panVcode;
    @BindView(R.id.pan_vcode_image)
    ImageView panVcodeImage;
    @BindView(R.id.pan_login_btn)
    Button panLoginBtn;
    private OkHttpClient mOkHttpClient;
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    private String cookie;
    private String myloginUrl;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(activity_pan_login);
        ButterKnife.bind(this);
        mContext = this;

        topbar.setTitle(getString(R.string.tustPanString)).setAddFunctionVisible(true);
        topbar.setOnClickAddFunctionListener(new AddFunctionListenter() {
            @Override
            public void onClick() {
                Intent intent = new Intent(mContext, DownloadPageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
            }
        });


        long time = System.currentTimeMillis() / 1000;   //单位s
        int authTime = Read(mContext, FormData.AUTHTIME, 0);  //获取存储值，单位为秒
        if(time - authTime < (40 * 60)){  //40分钟免验证
            //免验证
            intentToPanActivity();
        }

        TustBoxUtil tustBoxUtil = new TustBoxUtil(PanLoginActivity.this);
        //加载学号
        panNumber.setText(tustBoxUtil.getUid());

        //初始化OKhttp（带cookiejar）
        initOkhttp();
        //首次加载验证码
        loadVerCodeByOkHttp();

    }

    /**
     * 初始化okhttp
     * */
    private void initOkhttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
    }


    /**
     * 加载验证码
     * */
    private void loadVerCodeByOkHttp() {
        mOkHttpClient.newCall(new Request.Builder().url(picUrl).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.netError();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取验证码图片比特流
                final byte[] picBt = response.body().bytes();
                //获取验证码所携带的cookie
                cookie = response.headers().get(SET_COOKIE);
                //加载验证码
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picBt,0,picBt.length);
                        panVcodeImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @OnClick({R.id.pan_vcode_image, R.id.pan_login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pan_vcode_image:
                //切换验证码
                loadVerCodeByOkHttp();
                break;
            case R.id.pan_login_btn:
                String etNumber = panNumber.getText().toString();
                String etPswd = panPswd.getText().toString();
                String etVcode = panVcode.getText().toString();
                if(Wow(etNumber,etPswd,etVcode)){
                    ToastUtil.paramError();
                    return;
                }
                login(etNumber,etPswd,etVcode);
                break;
        }
    }

    private void login(String u, String p, String v) {
        myloginUrl = loginUrl+"u="+u+"&p="+p+"&v="+v+"&locale="+LOCALE_DATA;
        FormBody formBody = new FormBody.Builder()
                .add("u", u)
                .add("p", p)
                .add("v", v)
                .add("locale", LOCALE_DATA)
                .build();
        mOkHttpClient.newCall(new Request.Builder().addHeader(SET_COOKIE,cookie)
                .addHeader(UA, NORMAL_UA)
                .addHeader(CT, NORMAL_CT)
                .post(formBody).url(loginUrl).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       ToastUtil.netError();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                LogUtil.d(r);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //身份认证
                        if(r.contains("账号或密码不正确")||r.contains("401")){
                            ToastUtil.authError();
                            return;
                        }

                        //数据解析
                        try {
                            JSONObject jsonObject = new JSONObject(r);
                            String userid = jsonObject.getString("user_id");
                            String token = jsonObject.getString("token");
                            save(mContext,USERID,userid);
                            save(mContext,TOKEN,token);
                            //身份状态存储
                            save(mContext, FormData.AUTHTIME, (int)(System.currentTimeMillis()/1000));
                            ToastUtil.getSuccess();
                            intentToPanActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void intentToPanActivity() {
        startActivity(new Intent(mContext,PanActivity.class));
        finish();
    }

}
