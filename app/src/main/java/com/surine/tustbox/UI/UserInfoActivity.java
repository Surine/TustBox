package com.surine.tustbox.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Bean.User;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Fragment.Me.Me_info_fragment;
import com.surine.tustbox.Fragment.Me.MyActionFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoActivity extends TustBaseActivity {
    @BindView(R.id.art_back)
    ImageView mArtBack;

    @BindView(R.id.head_from_server)
    CircleImageView mHeadFromServer;
    @BindView(R.id.name_from_server)
    TextView mNameFromServer;
    @BindView(R.id.sign_from_server)
    TextView mSignFromServer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private String uid = null;
    private Context context;
    private User userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        context = this;
        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("个人");
        mToolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);

        uid = getIntent().getStringExtra(FormData.uid);
        initViewFromServerById(uid);
    }

    //加载信息
    private void initViewFromServerById(String uid) {
        String token = SharedPreferencesUtil.Read_safe(context,FormData.token,"");
        HttpUtil.get(UrlData.getUserInfo+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+uid).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAF", x);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 200) {
                        final User user = GsonUtil.parseJsonWithGson(jsonObject.getString(FormData.JDATA), User.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(user.getFace() != null){
                                    try {
                                        Glide.with(context).load(user.getFace()).into(mHeadFromServer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                //加载昵称
                                if(user!=null){
                                    if(user.getSchoolname() == null||user.getSchoolname().equals("")){
                                        if(user.getNick_name() == null){
                                            mNameFromServer.setText("未设置");
                                        }else{
                                            mNameFromServer.setText(user.getNick_name());
                                        }
                                    }else{
                                        mNameFromServer.setText(user.getSchoolname());
                                    }
                                }
                                if(user.getSign() != null){
                                    mSignFromServer.setText(user.getSign());
                                }
                                userInfo = user;
                                initViewPager(user);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void initViewFromServer() {
        //免流量加载

        String head_url = SharedPreferencesUtil.Read_safe(this, "face", "");
        String nick_name = SharedPreferencesUtil.Read_safe(this, "nick_name", "");
        String sign = SharedPreferencesUtil.Read_safe(this, "sign", "");
        if (!head_url.equals("")) {
            Glide.with(this).load(head_url).into(mHeadFromServer);
        }
        if (!nick_name.equals("")) {
            mNameFromServer.setText(nick_name);
        }
        if (!sign.equals("")) {
            mSignFromServer.setText(sign);
        }
    }

    private void initViewPager(User user) {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mTabs = (TabLayout) findViewById(R.id.tabs);
        fragments.add(MyActionFragment.getInstance(uid));
        fragments.add(Me_info_fragment.getInstance(user));
        titles.add("动态");
        titles.add("更多");
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(3);
        mTabs.setupWithViewPager(mViewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finishAfterTransition();
                break;
            case R.id.edit:
                String tust_number = SharedPreferencesUtil.Read(UserInfoActivity.this, FormData.tust_number_server,"");
                if(uid.equals(tust_number)){
                    if(userInfo == null || userInfo.getFace() == null){
                        break;
                    }
                    try {
                        Glide.clear(mHeadFromServer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(UserInfoActivity.this,EditUserInfoActivity.class).putExtra(FormData.face_server,userInfo.getFace()));
                }else{
                    Toast.makeText(this, R.string.UserInfoActivityDontEdit, Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.setting:
                startActivity(new Intent(UserInfoActivity.this,SettingActivity.class));
                break;
        }
        return true;
    }

}
