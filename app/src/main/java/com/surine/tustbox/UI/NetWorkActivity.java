package com.surine.tustbox.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.Net_EventBus;
import com.surine.tustbox.Fragment.network.Fragment_charge;
import com.surine.tustbox.Fragment.network.Fragment_login;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.CheckWifi_then_login_util;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NetWorkActivity extends TustBaseActivity {
    int network_status = 0;
    public static final String TAG = "LOG";
    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private int Position_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_net);
        setSupportActionBar(toolbar);
        setTitle(R.string.school_network);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(!SharedPreferencesUtil.Read(this,"IS_LOGIN_NETWORK",false)) {
            //登陆
            ShowLoginDialog();
        }else{
            Log_net();
        }
    }

    private void ShowLoginDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_view_login_work_view,null);
        final EditText number = (EditText) view.findViewById(R.id.tust_number);
        final EditText pswd = (EditText) view.findViewById(R.id.network_passwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(number.getText().toString().equals("")||pswd.getText().toString().equals("")){
                    Toast.makeText(NetWorkActivity.this, R.string.null_input, Toast.LENGTH_SHORT).show();
                    ShowLoginDialog();
                }else{
                    //储存已经登陆的数据
                    SharedPreferencesUtil.Save(NetWorkActivity.this,"TUST_PSWD_NETWORK", EncryptionUtil.base64_en(pswd.getText().toString()));
                    SharedPreferencesUtil.Save(NetWorkActivity.this,"TUST_NUMBER_NETWORK",number.getText().toString());

                    Log_net();
                }
            }
        });
        builder.show();

    }

    //登录校园网
    private void Log_net() {
        setTitle(getString(R.string.is_log_network));
        HttpUtil.post(UrlData.net_post_url, CheckWifi_then_login_util.LoginNetWork(NetWorkActivity.this)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NetWorkActivity.this,R.string.fail,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(s.contains(FormData.Drcom_pc_Login_success)){
                            //成功
                            Toast.makeText(NetWorkActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
                            SharedPreferencesUtil.Save(NetWorkActivity.this,"IS_LOGIN_NETWORK",true);
                            setTitle(R.string.school_network);
                            LoadInfo();
                        }else{
                            //账号密码错误
                            Toast.makeText(NetWorkActivity.this,R.string.wrong,Toast.LENGTH_SHORT).show();
                            ShowLoginDialog();
                        }
                    }
                });
            }
        });
    }

    private void LoadInfo() {
       HttpUtil.get(UrlData.net_get_url).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(NetWorkActivity.this, R.string.get_data_use_fail,Toast.LENGTH_SHORT).show();
                   }
               });
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               final String s = response.body().string();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if(s.contains(FormData.Drcom_pc_get_success)){
                           //成功
                           Toast.makeText(NetWorkActivity.this,R.string.success,Toast.LENGTH_SHORT).show();
                           //解析展示
                           Jsoup_and_show(s);
                       }else{
                           //获取数据失败
                           Toast.makeText(NetWorkActivity.this,R.string.get_data_use_fail,Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
       });
    }

    private void Jsoup_and_show(String s) {
        Document document = Jsoup.parse(s);
         /*取得script下面的JS变量*/
        Element e = document.getElementsByTag("script").get(1);
        List<String> numbers = PatternUtil.getNumber(e.data().toString().substring(e.data().toString().indexOf("time"),e.data().toString().indexOf("xsele")));
        String time = numbers.get(0);
        String flow = numbers.get(1);
        String fee = numbers.get(3);

        //官方换算公式
        int flow0=Integer.parseInt(flow)%1024;
        int flow1=Integer.parseInt(flow)-flow0;
        flow0=flow0*1000;
        flow0=flow0-flow0%1024;
        int fee1=Integer.parseInt(fee)-Integer.parseInt(fee)%100;
        String flow3=".";

        String use_time = time;
        String use_data = flow1/1024+flow3+flow0/1024;
        String use_fee =  ""+fee1/10000;

        Net_EventBus net_eventbus = new Net_EventBus(1,use_time,use_data,use_fee);
        initViewPager(net_eventbus);
    }

    private void initViewPager(Net_EventBus net_eventbus) {
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tab = (TabLayout) findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(Fragment_login.getInstance(net_eventbus));
        fragments.add(Fragment_charge.getInstance("2"));

        //3.使用string的list集合来添加标题
        titles.add("账号");
        titles.add("充值");


        //4.初始化适配器（传入参数：FragmentManager，碎片集合，标题）
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        //5.设置viewpager适配器
        viewpager.setAdapter(pagerAdapter);

        //6.设置缓存
        /*
        *  * 注意：设置缓存的原因
        * 在加载Tab-A时会实例化Tab-B中fragment，依次调用：onAttach、
        * onCreate、onCreateView、onActivityCreated、onStart和onResume。
        * 同样切换到Tab-B时也会初始化Tab-C中的fragment。（Viewpager预加载）
        * 但是fragment中的数据(如读取的服务器数据)没有相应清除，导致重复加载数据。
        *
        *
        * 注意：ps:我们在使用viewpager时会定义一个适配器adapter，其中实例化了一个fragment列表，
        * 所以在tab切换时fragment都是已经实例化好的，所以在切换标签页时是不会重新实例化fragment
        * 对象的，因而在fragment中定义的成员变量是不会被重置的。所以为列表初始化数据需要注意这个问题。
        *
        * 参考网址：https://my.oschina.net/buobao/blog/644699
*/
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tab.setupWithViewPager(viewpager);


        //8.viewpager的监听器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动监听器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页卡选中监听器
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    Position_page = 0;
                } else if (position == 1) {
                    Position_page = 1;
                }
            }

            //滚动状态变化监听器
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
