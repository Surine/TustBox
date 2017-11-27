package com.surine.tustbox.UI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Bean.Book_Info;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.main.Schedule_Fragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.AppUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends TustBaseActivity {
    int yourChoice;   //周选择变量
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    final String[] str = {"第一周", "第二周",
            "第三周", "第四周",
            "第五周", "第六周",
            "第七周", "第八周",
            "第九周", "第十周",
            "第十一周", "第十二周",
            "第十三周", "第十四周",
            "第十五周", "第十六周",
            "第十七周", "第十八周",
            "第十九周", "第二十周",
            "第二十一周", "第二十二周",
            "第二十三周", "第二十四周",
    };                     //周数据
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String update_message;
    private String version = "";
    private String log = "";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        //设置toolbar
        setSupportActionBar(mToolbar);
        //设置标题
        setTitle("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
        //设置界面框架
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(R.id.content, Schedule_Fragment.getInstance("1")).commit();

        //首次登录
        SharedPreferencesUtil.Save(this,"is_login",true);

        //子线程更新
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取新版本
                getNewVersion();
            }
        }).start();


        /**
         * 2017年11月14日21:48:03 加入侧滑菜单
         * 创建侧滑事件
         * */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        //Toolbar上面最左边显示三杠图标监听DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        View nav_view = mNavView.inflateHeaderView(R.layout.nav_header_drawer_layout);
        //侧滑顶部点击事件
        ImageView head = (ImageView) nav_view.findViewById(R.id.nav_head);
        TextView time = (TextView) nav_view.findViewById(R.id.nav_time);
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
        time.setText(TimeUtil.GetNavTime());
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });



        //侧滑菜单点击项
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mDrawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        switch (item.getItemId()){
                            case R.id.theme:
                                //主题
                                break;
                            case R.id.night:
//                                //夜间
//                                boolean isNight = SharedPreferencesUtil.Read(MainActivity.this,"night",false);
//                                if (isNight) {
//                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                                    SharedPreferencesUtil.Save(MainActivity.this,"night",false);
//                                } else {
//                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                                    SharedPreferencesUtil.Save(MainActivity.this,"night",true);
//                                }
//                                recreate();
                                break;
                            case R.id.setting:
                                //设置
                                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                                break;
                            case R.id.about:
                                //关于
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

    private void Share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.welcome)+UrlData.download_url);
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

                DataSupport.deleteAll(Course_Info.class);
                DataSupport.deleteAll(Student_info.class);
                DataSupport.deleteAll(Score_Info.class);
                DataSupport.deleteAll(Book_Info.class);
                File file = new File(String.valueOf(MainActivity.this.getFilesDir() + "/head.jpg"));
                deletefile(file);
                file = new File("/data/data/com.surine.tustbox/shared_prefs/data.xml");
                deletefile(file);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(MainActivity.this,
                        R.string.clear_success,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }

    private void deletefile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();   //delete the head or SharedPreferences
            }
        }
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
        EventBus.getDefault().unregister(this);
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
                    setTitle("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
                    //发送桌面小部件广播
                    Intent updateIntent = new Intent("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
                    sendBroadcast(updateIntent);
                    //发送周更新通知
                    EventBus.getDefault().post(new SimpleEvent(5, "UPDATE"));
                }
            }
        });
        builder.show();
    }

    //the method is a control menu update
    //we can use the supportInvalidateOptionsMenu() to update our menu
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem week = menu.findItem(R.id.week);
//        MenuItem other_user = menu.findItem(R.id.other_user);
//        if (Flag == 1) {
//            week.setVisible(true);
//            other_user.setVisible(true);
//        } else if (Flag == 2) {
//            week.setVisible(false);
//            other_user.setVisible(false);
//        } else {
//            week.setVisible(false);
//            other_user.setVisible(false);
//        }
//        return true;
//    }

    private void getNewVersion() {
        HttpUtil.get(UrlData.update_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //failure
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "网络好像出现了点问题！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                update_message = response.body().string().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            //获取相关信息
                            jsonObject = new JSONObject(update_message);
                            version = jsonObject.getString("version");
                            log = jsonObject.getString("log");
                            //is_ness = Integer.parseInt(jsonObject.getString("is_ness"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (version.equals(AppUtil.getVersionName(MainActivity.this)) || version.equals("") || version == null) {
                            //
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //new version
                            builder.setTitle("小天发现新版本啦！");
                            builder.setMessage(log);
                            builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String url = UrlData.download_url;
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);

                                }
                            });
                            builder.setCancelable(false);
                            builder.setNegativeButton("残忍拒绝", null);
                            builder.show();
                        }

                    }
                });
            }
        });

    }


    @Subscribe
    public void GetMessage(SimpleEvent event) {
        if (event.getId() == 6) {
//            //总线事件处理
//            SystemUI.color_toolbar(MainActivity.this, getSupportActionBar());
//            //设置底部栏颜色
//            //（选中及未选中）
//            int[][] states = new int[][]{
//                    new int[]{-android.R.attr.state_checked},
//                    new int[]{android.R.attr.state_checked}
//            };
//
//            int[] colors = new int[]{getResources().getColor(R.color.Tust_Grey), Integer.parseInt
//                    (event.getMessage())
//            };
//            ColorStateList csl = new ColorStateList(states, colors);
//            navigation.setItemTextColor(csl);
//            navigation.setItemIconTintList(csl);
        }
    }
}
