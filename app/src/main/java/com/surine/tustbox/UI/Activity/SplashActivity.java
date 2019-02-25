package com.surine.tustbox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.surine.tustbox.Pojo.JwcUserInfo;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.Mvp.login.LoginMvpActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.Constants.CHOOSE_WEEK;
import static com.surine.tustbox.App.Data.Constants.IS_LOGIN;
import static com.surine.tustbox.App.Data.FormData.JCODE;
import static com.surine.tustbox.App.Data.FormData.USER_TYPE;
import static com.surine.tustbox.App.Data.FormData.WEEK;
import static com.surine.tustbox.App.Data.FormData.college_server;
import static com.surine.tustbox.App.Data.FormData.face_server;
import static com.surine.tustbox.App.Data.FormData.nick_name_server;
import static com.surine.tustbox.App.Data.FormData.sign_server;

public class SplashActivity extends TustBaseActivity {
    private String login_string;
    private String token;
    String nick_name;
    String sign;
    String face;
    String college;
    private String type;

    private String empty = "";   //空字符串

    private String tustNumberFromXml = null;   //持久化的科大学号
    private String tokenFromXml = null;   //持久化的登录token
    private String pswdFromXml = null; //持久化的密码

    private String responseFromServer = null;  //token检查返回的字符串
    private JSONObject jsonObject; //jsonobject
    private int jcode;   //jcode
    private JSONObject jdata;  //jdata
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化
        ButterKnife.bind(this);
        SystemUI.hideStatusbar(this);
        tustNumberFromXml = SharedPreferencesUtil.Read(this,FormData.tust_number_server,empty);
        tokenFromXml = SharedPreferencesUtil.Read_safe(this,FormData.TOKEN,empty);
        pswdFromXml = SharedPreferencesUtil.Read(this,FormData.pswd,empty);

        //检查登陆状态
        //如果第一次使用，首先不自动注册
        if(tustNumberFromXml.equals(empty)){
            //直接进入登录页面
            Intent();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CheckLogin();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getServerWeek();
                }
            }).start();
        }


    }

    private void CheckLogin() {
        //构建TOKEN验证表单（携带账号及加密密码，减少操作步骤）
        FormBody formBody = new FormBody.Builder()
            .add(FormData.tust_number_server,tustNumberFromXml)
            .add(FormData.token,tokenFromXml)
            .add(FormData.pass_server,pswdFromXml)
            .build();

        //发起TOKEN验证请求
        HttpUtil.post(UrlData.login_server_url,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseFromServer = response.body().string();
                Log.d("STRING",responseFromServer);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(responseFromServer);

                            //为了防止jdata没有数据的时候跳转到最外层catch
                            try {
                                jdata = jsonObject.getJSONObject(FormData.JDATA);
                                token = jdata.getString(FormData.token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            jcode = jsonObject.getInt(JCODE);
                            switch (jcode){
                                case 404:
                                    //进行用户注册
                                    Register_user();
                                    break;
                                case 400:
                                    Toast.makeText(SplashActivity.this, R.string.failtologinpswderror, Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                    //储存token
                                    SharedPreferencesUtil.Save_safe(SplashActivity.this,FormData.TOKEN,token);
                                    SaveInfo(jdata);
                                    break;
                                case 201:
                                    //进入token验证成功状态，可自由访问数据
                                    SaveInfo(jdata);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private void SaveInfo(JSONObject jdata) {
      //储存用户数据防止二次加载
        try {
            nick_name = jdata.getString(nick_name_server);
            sign = jdata.getString(sign_server);
            face = jdata.getString(face_server);
            college = jdata.getString(college_server);
            type = jdata.getString(USER_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TODO:编码
        SharedPreferencesUtil.Save_safe(SplashActivity.this,nick_name_server,nick_name);
        SharedPreferencesUtil.Save_safe(SplashActivity.this,sign_server,sign);
        SharedPreferencesUtil.Save_safe(SplashActivity.this,face_server,face);
        SharedPreferencesUtil.Save_safe(SplashActivity.this,college_server,college);
        SharedPreferencesUtil.Save_safe(SplashActivity.this, USER_TYPE,type);
    }


    private void Register_user() {
        if(tustNumberFromXml.equals(empty)||pswdFromXml.equals(empty)){
            return;
        }

        String nickNameDb = "未命名";
        String collegeDb = "保密";
        String sexDb = "保密";
        try {
            List<JwcUserInfo> jwcUserInfoL1 = DataSupport.where("jwcName = ?","姓名").find(JwcUserInfo.class);
            if(jwcUserInfoL1 != null){
                nickNameDb = jwcUserInfoL1.get(0).getJwcValue();
            }
            List<JwcUserInfo> jwcUserInfoL2 =  DataSupport.where("jwcName = ?","院系").find(JwcUserInfo.class);
            if(jwcUserInfoL2 != null){
                collegeDb = jwcUserInfoL2.get(0).getJwcValue();
            }

            List<JwcUserInfo> jwcUserInfoL3 = DataSupport.where("jwcName = ?","性别").find(JwcUserInfo.class);
            if(jwcUserInfoL3 != null){
                sexDb = jwcUserInfoL3.get(0).getJwcValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FormBody formBody = new FormBody.Builder()
                .add(FormData.tust_number_server,tustNumberFromXml)
                .add(FormData.pass_server, pswdFromXml)
                .add(sign_server,"这个人很懒，还没有个性签名~")
                .add(nick_name_server,nickNameDb)
                .add(FormData.college_server,collegeDb)
                .add(FormData.sex,sexDb)
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
                login_string = response.body().string();
                runOnUiThread(new Runnable() {
                    JSONObject jsonObject;
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(login_string);
                            jcode = jsonObject.getInt(JCODE);
                            if(jcode == 404){
                                //进行用户注册
                                Toast.makeText(SplashActivity.this, "用户已存在或者数据库操作失败", Toast.LENGTH_SHORT).show();
                            }else if(jcode == 400){
                                Toast.makeText(SplashActivity.this, "个人信息不完整，请重新登录教务处！", Toast.LENGTH_SHORT).show();
                            }else if(jcode == 200){
                                //用户创建成功
                                CheckLogin();
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
        if (!SharedPreferencesUtil.Read(SplashActivity.this, IS_LOGIN, false)) {
            //启动登录页面
            //Intent intent = new Intent(this, LoginActivity.class);
            Intent intent = new Intent(this, LoginMvpActivity.class);
            startActivity(intent);
        } else {
            //启动首页
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        //结束当前活动
        finish();
    }


    /***
     * 获取当前周，这里需要连接服务器更新
     */
    private void getServerWeek() {
        HttpUtil.get(UrlData.getServerWeek).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //更新失败之后直接跳转，当前本地持久化CHOOSE_WEEK字段是默认值0
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, R.string.failtoupdatetheweek, Toast.LENGTH_SHORT).show();
                    }
                });
                Intent();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int week_from_server = 0;
                //下面的代码用于获取当前周数值
                String data = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    week_from_server = Integer.parseInt(jsonObject.getString(WEEK));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //如果返回的值不是0，那么代表从服务器获取到当前周的值了，储存之后并跳转
                if(week_from_server != 0){
                    SharedPreferencesUtil.save(SplashActivity.this, CHOOSE_WEEK, week_from_server);
                }
                Intent();
            }
        });
    }

}
