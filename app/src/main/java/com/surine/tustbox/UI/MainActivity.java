package com.surine.tustbox.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.main.MainFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.ClearDataUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 2018年2月20日 重构，UI重设计
 */


public class MainActivity extends TustBaseActivity {
    final static String[] str = new String[]{
            "第一周", "第二周", "第三周", "第四周", "第五周", "第六周",
            "第七周", "第八周", "第九周", "第十周", "第十一周", "第十二周",
            "第十三周", "第十四周", "第十五周", "第十六周", "第十七周", "第十八周",
            "第十九周", "第二十周", "第二十一周", "第二十二周", "第二十三周", "第二十四周",
    };
    int yourChoice;   //周选择变量

    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_message)
    TextView toolbarMessage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;


    private String update_message;
    private String version = "";
    private String log = "";
    private View view;
    private Context context;
    private Badge badge;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        context = this;
        //设置toolbar
        setSupportActionBar(mToolbar);
        //设置标题
        //  setTitle("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
        title.setText("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
        //设置界面框架
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(R.id.content, MainFragment.getInstance("1")).commit();

        //首次登录
        SharedPreferencesUtil.Save(this, "is_login", true);

        //更新桌面小部件
        updateWidget();


        /**
         * 2017年11月14日21:48:03 加入侧滑菜单
         * 创建侧滑事件
         * */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        //取得未读消息数
        getUnReadNum();

        //Toolbar上面最左边显示三杠图标监听DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        View nav_view = mNavView.inflateHeaderView(R.layout.nav_header_drawer_layout);
        //侧滑顶部点击事件
        ImageView head = (ImageView) nav_view.findViewById(R.id.nav_head);
        TextView name = (TextView) nav_view.findViewById(R.id.nav_name);
        //初始化头像和昵称
        //免流量加载
        String head_url = SharedPreferencesUtil.Read_safe(this, "face", "");
        String nick_name = SharedPreferencesUtil.Read_safe(this, "nick_name", "");
        if (!head_url.equals("")) {
            Glide.with(this).load(head_url).into(head);
        }
        if (!nick_name.equals("")) {
            name.setText(nick_name);
        }
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = SharedPreferencesUtil.Read(MainActivity.this, FormData.tust_number_server, "");
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class).putExtra(FormData.uid, uid);
                startActivity(intent);
            }
        });


        //侧滑菜单点击项
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mDrawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        switch (item.getItemId()) {
                            case R.id.theme:
                                //主题
                                Toast.makeText(context, "暂无主题功能", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.setting:
                                //设置
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                break;
                            case R.id.about:
                                //关于
                                startActivity(new Intent(MainActivity.this, SettingActivity.class).putExtra("set_", 2));
                                break;
                            case R.id.share:
                                //分享
                                Share();
                                break;
                            case R.id.exit:
                                //退出
                                Exit();
                                break;
                        }
                    }
                }, 200);

                return true;
            }
        });


    }

    private void getUnReadNum() {
        //获取未读数量
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String buildUrl = UrlData.getMessageNum + "?" + FormData.toUser + "=" + tust_number + "&" + FormData.token + "=" + token;
        Log.d("TAG", buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG", x);
                try {
                    final JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (badge != null) {
                                        badge.hide(true);
                                    }
                                    badge = new QBadgeView(MainActivity.this).bindTarget(toolbarMessage).setBadgeNumber(jsonObject.getInt(FormData.JDATA)).setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                                        @Override
                                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                                            if (dragState == 5) {
                                                //拖拽全部已读
                                                AllRead();
                                            }
                                        }
                                    });
                                    badge.isDraggable();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (badge != null) {
                                    badge.hide(true);
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void AllRead() {
        //全部已读
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String buildUrl = UrlData.readAll + "?" + FormData.toUser + "=" + tust_number + "&" + FormData.token + "=" + token;
        Log.d("TAG", buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG", x);
                try {
                    final JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //成功
                                Toast.makeText(context, R.string.MainActivityAllRead, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUnReadNum();
    }

    private void Share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.welcome) + UrlData.download_url);
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表  
        startActivity(Intent.createChooser(intent, getString(R.string.more_share)));
    }

    //exit()
    private void Exit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.exit_info);
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //1:delete all of the database
                //2:delete SharedPreferences
                //3.start the login activty
                //4. make a toast

                //TODO：清除APP全部数据
                ClearDataUtil clearDataUtil = new ClearDataUtil(MainActivity.this);
                clearDataUtil.clearAllDataOfApplication();


                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(MainActivity.this,
                        R.string.clear_success,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.week:
                show_Dialog(); //show the dialog(choose week)
                break;

        }
        return true;
    }


    private void show_Dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_week);
        yourChoice = -1;
        //default selection
        builder.setSingleChoiceItems(str, SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0) - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yourChoice = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (yourChoice != -1) {

                    //储存，通知更新UI
                    SharedPreferencesUtil.Save(MainActivity.this, "choice_week", yourChoice + 1);
                    Toast.makeText(MainActivity.this, getString(R.string.choose_) + str[yourChoice], Toast.LENGTH_SHORT).show();
                    // setTitle("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
                    title.setText("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
                    updateWidget();
                    //发送周更新通知
                    EventBus.getDefault().post(new SimpleEvent(0, "UPDATE"));
                }
            }
        });
        builder.show();
    }

    private void updateWidget() {
        //发送桌面小部件广播
        Intent updateIntent = new Intent("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
        sendBroadcast(updateIntent);
    }

    @OnClick({R.id.toolbar_message, R.id.floatingActionButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_message:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                break;
            case R.id.floatingActionButton:
                break;
        }
    }




}
