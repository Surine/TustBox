package com.surine.tustbox.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.SystemUI;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SplashActivity extends TustBaseActivity {
    private int week_from_server;
    private String login_string;
    private int jcode;
    private String token_string;
    private JSONObject jdata;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        SystemUI.hide_statusbar(this);

        //get the server week
        getServerWeek();

        //Check Token
        Check_Token();

    }

    private void Check_Token() {
        //构建TOKEN验证表单（携带账号密码，减少操作步骤）
        FormBody formBody = new FormBody.Builder()
                .add(FormData.tust_number_server, SharedPreferencesUtil.Read(this
                        ,"tust_number","000000"))
                .add(FormData.token,SharedPreferencesUtil.Read_safe(this,"TOKEN",""))
                .add(FormData.pass_server, EncryptionUtil.base64_de(SharedPreferencesUtil.Read(this
                        ,"pswd","000000")))
                .build();

        //发起TOKEN验证请求
        HttpUtil.post(UrlData.login_server_url,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  Toast.makeText(SplashActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                token_string = response.body().string().toString();
               // Log.d("XXX",token_string);
                runOnUiThread(new Runnable() {
                    public JSONObject jsonObject;
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(token_string);
                            jcode = jsonObject.getInt("jcode");
                            if(jcode == 404){
                                //进行用户注册
                                Register_user();
                            }else if(jcode == 400){
                                Toast.makeText(SplashActivity.this, "登录小天服务器失败，账号或者密码错误", Toast.LENGTH_SHORT).show();
                            }else if(jcode == 200){
                                //创建token
                                jdata = jsonObject.getJSONObject("jdata");
                                token = jdata.getString("token");
                                SharedPreferencesUtil.Save_safe(SplashActivity.this,"TOKEN",token);
                            }else if(jcode == 201){
                                //啥都不干
                                //进入token验证成功状态，可自由访问数据
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }



    private void Register_user() {
        String tust_number = SharedPreferencesUtil.Read(SplashActivity.this,"tust_number","");
        String pswd = EncryptionUtil.base64_de(SharedPreferencesUtil.Read(SplashActivity.this,"pswd",""));
        if(tust_number.equals("")||pswd.equals("")){
            Toast.makeText(SplashActivity.this, "注册用户失败，本地数据错误，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        FormBody formBody = new FormBody.Builder()
                .add(FormData.tust_number_server,tust_number)
                .add(FormData.pass_server, pswd)
                .add(FormData.sign_server,"这个人很懒，还没有个性签名~")
                .add(FormData.nick_name_server,SharedPreferencesUtil.Read(SplashActivity.this,"stu_name","未设置"))
                .add(FormData.college_server,SharedPreferencesUtil.Read(SplashActivity.this,"part","未知学院"))
                .add(FormData.sex,SharedPreferencesUtil.Read(SplashActivity.this,"sex","男"))
                .build();
        HttpUtil.post(UrlData.register_server_url,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, "注册小天账号失败，网络未连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                login_string = response.body().string().toString();
                runOnUiThread(new Runnable() {
                    public JSONObject jsonObject;
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(login_string);
                            jcode = jsonObject.getInt("jcode");
                            if(jcode == 404){
                                //进行用户注册
                                Toast.makeText(SplashActivity.this, "用户已存在或者数据库操作失败", Toast.LENGTH_SHORT).show();
                            }else if(jcode == 400){
                                Toast.makeText(SplashActivity.this, "个人信息不完整，请重新登录教务处！", Toast.LENGTH_SHORT).show();
                            }else if(jcode == 200){
                                //用户创建成功
                                Check_Token();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }



    //intent: start intent based on login status
    private void Intent() {
        //start login
        if (!SharedPreferencesUtil.Read(SplashActivity.this, "is_login", false)) {
            //intent
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("other_user", "no");
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
                        Toast.makeText(SplashActivity.this, "自动更新周失败", Toast.LENGTH_SHORT).show();
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
                SharedPreferencesUtil.Save(SplashActivity.this, "choice_week", week_from_server);
                Intent();
            }
        });
    }

}
