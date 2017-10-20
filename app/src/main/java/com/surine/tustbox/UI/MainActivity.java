package com.surine.tustbox.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.main.Box_Fragment;
import com.surine.tustbox.Fragment.main.Me_Fragment;
import com.surine.tustbox.Fragment.main.Schedule_Fragment;
import com.surine.tustbox.Fragment.main.SchoolFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends TustBaseActivity {
    int yourChoice;
    private int Flag = 1;
    Fragment schedule_fragment = null;
    Fragment box_fragment = Box_Fragment.getInstance("2");
    Fragment me_fragment = Me_Fragment.getInstance("3");
    Fragment mSchoolFragment = SchoolFragment.getInstance("4");
    final String[] str={"第一周","第二周",
            "第三周","第四周",
            "第五周","第六周",
            "第七周","第八周",
            "第九周","第十周",
            "第十一周","第十二周",
            "第十三周","第十四周",
            "第十五周","第十六周",
            "第十七周","第十八周",
            "第十九周","第二十周",
            "第二十一周","第二十二周",
            "第二十三周","第二十四周",
    };
    private String update_message;
    private String version = "";
    private String log = "";
    private int is_ness = 0;
    private BottomNavigationView navigation;
    private String login_string;
    private int jcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //getNew Version
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取新版本
                getNewVersion();
            }
        }).start();

        //init the first fragment
        initFragmentOne("1");

        //iadd all of the fragment to fragmenttransaction
        setFragment();

        //set the normal toolbar title
        setTitle("第"+(SharedPreferencesUtil.Read(MainActivity.this,"choice_week",0))+"周");


        //init the BottomNavigationView and set linstener
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //set the toolbar title
                        setTitle("第"+(SharedPreferencesUtil.Read(MainActivity.this,"choice_week",0))+"周");
                            //show the fragment
                        Flag = 1;
                        setFragment_Show(Flag);
                        //update the menu
                        supportInvalidateOptionsMenu();
                        return true;
                    case R.id.navigation_dashboard:
                        setTitle(R.string.box);
                        Flag = 2;
                        setFragment_Show(Flag);
                        supportInvalidateOptionsMenu();
                        return true;
                    case R.id.navigation_school:
                        setTitle(R.string.school);
                        Flag = 4;
                        setFragment_Show(Flag);
                        supportInvalidateOptionsMenu();
                        return true;
                    case R.id.navigation_notifications:
                        setTitle(R.string.me);
                        Flag = 3;
                        setFragment_Show(Flag);
                        supportInvalidateOptionsMenu();
                        return true;
                }

                return false;
            }
        });


        if(!SharedPreferencesUtil.Read(MainActivity.this,"is_login",false)) {
            show_Dialog(); //show the dialog(choose week)
            //save the login status
            SharedPreferencesUtil.Save(MainActivity.this,"is_login",true);
        }
        //set first_fragment
        setFragment_Show(Flag);
    }



    //iadd all of the fragment to fragmenttransaction
    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.add(R.id.content, schedule_fragment);
        tran.add(R.id.content, box_fragment);
        tran.add(R.id.content, me_fragment);
        tran.add(R.id.content, mSchoolFragment);
        tran.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initFragmentOne(String flag) {
        /*if fragment is null ,create it directly
        * Otherwise,we have to remove the old one and create a new one
        * */
        if (schedule_fragment == null) {
            schedule_fragment = Schedule_Fragment.getInstance(flag);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
            tran.remove(schedule_fragment);
            schedule_fragment = Schedule_Fragment.getInstance(flag);
            tran.add(R.id.content, schedule_fragment);
            tran.commit();
        }
    }

    //the method of show Fragment
    private void setFragment_Show(int fragment) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        if(fragment == 1){
            tran.show(schedule_fragment);
            tran.hide(box_fragment);
            tran.hide(me_fragment);
            tran.hide(mSchoolFragment);
        }else if(fragment == 2){
            tran.show(box_fragment);
            tran.hide(schedule_fragment);
            tran.hide(me_fragment);
            tran.hide(mSchoolFragment);
        }else if(fragment == 3){
            tran.show(me_fragment);
            tran.hide(schedule_fragment);
            tran.hide(box_fragment);
            tran.hide(mSchoolFragment);
        }else{
            tran.show(mSchoolFragment);
            tran.hide(schedule_fragment);
            tran.hide(box_fragment);
            tran.hide(me_fragment);
        }
        tran.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.week:
                show_Dialog(); //show the dialog(choose week)
                break;
            case R.id.other_user:
                if(!SharedPreferencesUtil.Read(MainActivity.this,"other_user_is_login",false)){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("other_user","other_user");
                    startActivity(intent);
                }else{
                    initFragmentOne("other_user_signal");
                }
                break;
        }
        return true;
    }

    private void show_Dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_week);
        yourChoice = -1;
        //default selection
        builder.setSingleChoiceItems(str, SharedPreferencesUtil.Read(MainActivity.this,"choice_week",0)-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yourChoice = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (yourChoice != -1) {

                    //1.choose the week and make a SharedPreferences
                    //2.send a broadcase to the widget(update the widget)
                    //3. load fragment

                    SharedPreferencesUtil.Save(MainActivity.this,"choice_week",yourChoice+1);
                    Toast.makeText(MainActivity.this, getString(R.string.choose_) + str[yourChoice], Toast.LENGTH_SHORT).show();
                    setTitle("第"+(SharedPreferencesUtil.Read(MainActivity.this,"choice_week",0))+"周");
                    Intent updateIntent = new Intent("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
                    sendBroadcast(updateIntent);

                    EventBus.getDefault().post(new SimpleEvent(5,"UPDATE"));
                }
            }
        });
        builder.show();
    }

    //the method is a control menu update
    //we can use the supportInvalidateOptionsMenu() to update our menu
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem week = menu.findItem(R.id.week);
        MenuItem other_user = menu.findItem(R.id.other_user);
        if(Flag == 1) {
            week.setVisible(true);
            other_user.setVisible(true);
        }else if(Flag == 2){
            week.setVisible(false);
            other_user.setVisible(false);
        }else{
            week.setVisible(false);
            other_user.setVisible(false);
        }
        return true;
    }

    private void getNewVersion() {
        HttpUtil.get(UrlData.update_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //failure
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"网络好像出现了点问题！",Toast.LENGTH_SHORT).show();
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
                            is_ness = Integer.parseInt(jsonObject.getString("is_ness"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(version.equals(getAppInfo())||version.equals("")||version == null){
                            //
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //new version
                            builder.setTitle("小天发现新版本啦！");
                            builder.setMessage(log);
                            builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String url = UrlData.download_url;
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent .setData(Uri.parse(url));
                                    startActivity(intent);

                                }
                            });
                            builder.setCancelable(false);
                            builder.setNegativeButton("残忍拒绝",null);
                            builder.show();
                        }

                    }
                });
            }
        });

    }
    private String getAppInfo() {
        try {
            String pkName = getPackageName();
            String versionName = getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            //	return pkName + "   " + versionName + "  " + versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==6) {
            //总线事件处理
            //设置toolbar颜色
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Integer.parseInt
                    (String.valueOf(SharedPreferencesUtil.Read(this,"TOOLBAR_C", R.color.colorPrimary)))));
            //设置底部栏颜色
            //（选中及未选中）
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked}
            };

            int[] colors = new int[]{getResources().getColor(R.color.Tust_Grey),Integer.parseInt
                    (event.getMessage())
            };
            ColorStateList csl = new ColorStateList(states, colors);
            navigation.setItemTextColor(csl);
            navigation.setItemIconTintList(csl);
        }
    }
}
