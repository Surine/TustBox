package com.surine.tustbox.Mvp.splash;

import android.content.Context;
import android.content.res.Resources;

import com.surine.tustbox.Pojo.JwcUserInfo;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Mvp.base.BaseCallBack;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.Constants.CHOOSE_WEEK;
import static com.surine.tustbox.App.Data.Constants.ERROR_PSWD;
import static com.surine.tustbox.App.Data.Constants.INFO_ERROR;
import static com.surine.tustbox.App.Data.Constants.LOGIN_SAVE;
import static com.surine.tustbox.App.Data.Constants.LOGIN_SAVE_TOKEN;
import static com.surine.tustbox.App.Data.Constants.NON_REGISTER;
import static com.surine.tustbox.App.Data.Constants.USER_EXIST;
import static com.surine.tustbox.App.Data.FormData.JCODE;
import static com.surine.tustbox.App.Data.FormData.USER_TYPE;
import static com.surine.tustbox.App.Data.FormData.WEEK;
import static com.surine.tustbox.App.Data.FormData.college_server;
import static com.surine.tustbox.App.Data.FormData.face_server;
import static com.surine.tustbox.App.Data.FormData.nick_name_server;
import static com.surine.tustbox.App.Data.FormData.sign_server;

/**
 * Created by Surine on 2018/9/14.
 * model实现
 */

public class SplashImpl implements SplashModel {

    private Context context;
    private Resources r;
    private String tustNumberFromXml; //UID
    private String pswdFromXml; //pswd
    private String tokenFromXml; //token
    private TustBoxUtil tustBoxUtil;

    public SplashImpl(Context context) {
        this.context = context;
        r = context.getResources();
    }



    @Override
    public void register(final BaseCallBack<String> baseCallBack) {
       tustBoxUtil = new TustBoxUtil(context);
       tustNumberFromXml = tustBoxUtil.getUid();
       pswdFromXml = tustBoxUtil.getPswd();

       if(tustNumberFromXml.equals("")||pswdFromXml.equals("")){
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
            baseCallBack.onError();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String loginString = response.body().string();
            int jcode;
            try {
                JSONObject jsonObject = new JSONObject(loginString);
                jcode = jsonObject.getInt(JCODE);

                switch (jcode) {
                    case 404:
                        //注册失败，用户已存在
                        baseCallBack.onFail(USER_EXIST);
                        break;
                    case 400:
                        //注册失败，个人信息有错误
                        baseCallBack.onFail(INFO_ERROR);
                        break;
                    case 200:
                        //成功之后准备登录
                        baseCallBack.onSuccess(LOGIN_SAVE);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
          }
        });

    }

    @Override
    public void getWeek(final BaseCallBack<String> baseCallBack) {
        HttpUtil.get(UrlData.getServerWeek).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseCallBack.onError();
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
                    SharedPreferencesUtil.save(context, CHOOSE_WEEK, week_from_server);
                }
                baseCallBack.onSuccess("");
            }
        });
    }


    //检查登录状态
    @Override
    public void checkLoginStatus(BaseCallBack<String> baseCallBack) {
        tustBoxUtil = new TustBoxUtil(context);
        tustNumberFromXml = tustBoxUtil.getUid();
        pswdFromXml = tustBoxUtil.getPswd();

        if(tustNumberFromXml.equals("")){
            //成功,跳转登录页面
            baseCallBack.onSuccess("Success");
        }else{
            //进行主页准备
            baseCallBack.onFail("Fail");
        }
    }

    //检查Token
    @Override
    public void checkToken(final BaseCallBack<String> baseCallBack) {
        //获取token
        tokenFromXml = tustBoxUtil.getToken();
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
                baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseFromServer = response.body().string();
                LogUtil.d(responseFromServer);
                JSONObject jsonObject;
                JSONObject jdata = null;
                String token = null;
                int jcode;

                try {

                    jsonObject = new JSONObject(responseFromServer);

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
                            baseCallBack.onFail(NON_REGISTER);
                            break;
                        case 400:
                            //帐号密码错误
                            baseCallBack.onFail(ERROR_PSWD);
                            break;
                        case 200:
                            //储存token
                            SharedPreferencesUtil.Save_safe(context,FormData.TOKEN,token);
                            saveUserInfo(jdata);
                            baseCallBack.onSuccess(LOGIN_SAVE_TOKEN);
                            break;
                        case 201:
                            saveUserInfo(jdata);
                            baseCallBack.onSuccess(LOGIN_SAVE);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //储存信息
    private void saveUserInfo(JSONObject jdata) {
        //储存用户数据防止二次加载
        String nick_name = null;
        String sign = null;
        String face = null;
        String college = null;
        String type = null;
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
        SharedPreferencesUtil.Save_safe(context,nick_name_server,nick_name);
        SharedPreferencesUtil.Save_safe(context,sign_server,sign);
        SharedPreferencesUtil.Save_safe(context,face_server,face);
        SharedPreferencesUtil.Save_safe(context,college_server,college);
        SharedPreferencesUtil.Save_safe(context, USER_TYPE,type);
    }

}
