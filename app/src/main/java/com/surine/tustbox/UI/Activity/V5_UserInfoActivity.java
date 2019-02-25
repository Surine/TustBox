package com.surine.tustbox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;
import com.surine.tustbox.Pojo.User;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Fragment.Me.MyActionFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.JCode.ERROR;
import static com.surine.tustbox.App.Data.JCode.J200;

public class V5_UserInfoActivity extends TustBaseActivity {

    @BindView(R.id.tustBoxHead)
    ImageView tustBoxHead;
    @BindView(R.id.tustBoxName)
    TextView tustBoxName;
    @BindView(R.id.userSetting)
    ImageView userSetting;
    @BindView(R.id.tustBoxSex)
    CircleImageView tustBoxSex;
    @BindView(R.id.tustBoxSign)
    TextView tustBoxSign;

    private Context mContext;
    private String uid;
    private TustBoxUtil tbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        mContext = this;
        tbUtil = new TustBoxUtil(this);
        setContentView(R.layout.activity_v5_user_info);
        ButterKnife.bind(this);
        uid = getIntent().getStringExtra(FormData.uid);
        loadFragment(R.id.content, MyActionFragment.getInstance(uid));
        if(uid != null || !uid.isEmpty())
            initViewFromServerById(uid);

    }



    //加载信息
    private void initViewFromServerById(String uid) {
        String token = tbUtil.getToken();
        HttpUtil.get(UrlData.getUserInfo+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+uid).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RunOnUiThread.updateUi(mContext, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        ToastUtil.netError();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String serveData = response.body().string();
                RunOnUiThread.updateUi(mContext, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        try {
                            int status = JsonUtil.getStatus(serveData);
                            switch (status){
                                case J200:
                                    final User user = JsonUtil.getPojo(serveData, User.class);
                                    loadView(user);
                                    break;
                                case ERROR:
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

    private void loadView(User user) {
        if(user == null)
            return;

        //设置头像
        if(user.getFace() != null)
            if(!V5_UserInfoActivity.this.isFinishing())
                Glide.with(V5_UserInfoActivity.this).load(user.getFace()).into(tustBoxHead);

        //设置用户名
        if(user.getNick_name() != null)
            tustBoxName.setText(user.getNick_name());

        if(user.getSchoolname() != null)
            tustBoxName.setText(user.getSchoolname());



        //设置签名
        if(user.getSign() != null)
            tustBoxSign.setText(user.getSign());


        //加载性别
        if(user.getSex() != null){
            if(user.getSex().equals("男")){
                tustBoxSex.setImageResource(R.color.blue);
            }else if(user.getSex().equals("女")){
                tustBoxSex.setImageResource(R.color.Tust_RED);
            }else{
                tustBoxSex.setImageResource(R.color.pure_grey);
            }
        }else{
            tustBoxSex.setImageResource(R.color.pure_grey);
        }


        //加载修改信息按钮
        if(user.getTust_number() != null){
            if(user.getTust_number().equals(tbUtil.getUid())){
                userSetting.setVisibility(View.VISIBLE);
            }else{
                userSetting.setVisibility(View.GONE);
            }
        }else{
            userSetting.setVisibility(View.GONE);
        }

    }


    @OnClick(R.id.userSetting)
    public void onViewClicked() {
        startActivity(new Intent(mContext,EditUserInfoActivity.class));
    }
}
