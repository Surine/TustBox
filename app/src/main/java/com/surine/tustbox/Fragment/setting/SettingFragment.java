package com.surine.tustbox.Fragment.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.surine.tustbox.Activity.SettingActivity;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.ClearCacheUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by surine on 2017/4/8.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private static final long CONNECT_TIMEOUT = 5;
    private Preference Setting_back;
    private SwitchPreference setting_dialog;
    private SwitchPreference setting_close_show_picture;
    private Preference About;
    private Preference notic;
    private Preference bug;
    private Preference orl;
    private Preference check_update;
    private Preference clear_cache;
    private ProgressDialog pg;
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    String update_message;
    String new_message = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        initOkHttp();
        findview();   //initview
        setLinsener();   //setlinstener
    }

    private void setLinsener() {
        Setting_back.setOnPreferenceClickListener(this);
        setting_dialog.setOnPreferenceClickListener(this);
        About.setOnPreferenceClickListener(this);
        notic.setOnPreferenceClickListener(this);
        bug.setOnPreferenceClickListener(this);
        check_update.setOnPreferenceClickListener(this);
        orl.setOnPreferenceClickListener(this);
        setting_close_show_picture.setOnPreferenceClickListener(this);
        clear_cache.setOnPreferenceClickListener(this);
    }


    private void initOkHttp() {
        //init the okhttpclient and set the cookiejar for the data request
        builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//设置连接超时时间;
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }

    private void findview() {
        Setting_back = findPreference("setting_back");
        About = findPreference("about");
        notic = findPreference("notic");
        bug = findPreference("bug");
        orl = findPreference("orl");
        check_update = findPreference("check_update");
        clear_cache = findPreference("clear_cache");
        Setting_back = findPreference("setting_back");
        Setting_back = findPreference("setting_back");
        setting_dialog = (SwitchPreference) findPreference("setting_dialog");
        setting_close_show_picture = (SwitchPreference) findPreference("setting_close_show_picture");
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == Setting_back){
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",1));
        }else if(preference == setting_dialog){

        }else if(preference == setting_close_show_picture){
            //发送数据
            EventBus.getDefault().post(new SimpleEvent(1,"UPDATE"));
        }else if(preference == About){
            //about
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",2));
        }else if(preference == bug){
           //bug
            sendbug();
        }else if(preference == orl){
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",4));
        }else if(preference == notic){
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",5));
        }else if(preference == clear_cache){
            clear_cache();
        }else if(preference == check_update){
            Check_update();
        }
        return false;
    }

    private void Check_update() {
        new_message = "APP日志\n\n";
        setDialog();
        StartCheck();
    }

    private void StartCheck() {
        Request request = new Request.Builder().
                url(UrlData.update_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //failure
                Message mess = new Message();
                mess.what = 1;
                myHandler.sendMessage(mess);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                update_message = response.body().string().toString();
                pg.dismiss();
                Message mess = new Message();
                mess.what = 2;
                myHandler.sendMessage(mess);
            }
        });

    }

    private void Jsoup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Document doc = Jsoup.parse(update_message);
        Elements content = doc.select("li");
        for(int i = 0;i<content.size();i++){
          new_message+=(content.get(i).text()+"\n");
        }
        String title = doc.title();
        if(title.equals(getAppInfo())){
            //this version
            builder.setTitle("已经是最新版本呐！");
            builder.setMessage(new_message);
            builder.setPositiveButton(R.string.ok,null);
        }else{
            //new version
            builder.setTitle("小天发现新版本啦！");
            builder.setMessage(new_message);
            builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String url = "http://surine.cn/download/app-release.apk";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent .setData(Uri.parse(url));
                    startActivity(intent);

                }
            });
            builder.setNegativeButton("残忍拒绝",null);
        }
        builder.show();
    }


    private void clear_cache() {
        /** * 清除本应用内部缓存 */
        String cachePath2 =  getActivity().getCacheDir().getPath();
        ClearCacheUtil.delAllFile(cachePath2,getActivity());
        Toast.makeText(getActivity(),"缓存清理成功，请重新选择背景",Toast.LENGTH_SHORT).show();
    }

    private void sendbug() {
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用  
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用  
        Uri uri = Uri.parse("mailto:2234503567@qq.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, "2234503567@qq.com"); // 抄送人  
        intent.putExtra(Intent.EXTRA_SUBJECT, "欢迎给盒子提出意见：" + android.os.Build.MODEL + "SDK:" + android.os.Build.VERSION.SDK); // 主题  
        intent.putExtra(Intent.EXTRA_TEXT, "想说点什么？");// 正文  
        startActivity(Intent.createChooser(intent, "你要使用什么方式联系盒子呢？"));
    }


    //set dialog
    private void setDialog() {
        //create the dialog
        pg = new ProgressDialog(getActivity());
        pg.setTitle("检查更新");
        pg.setMessage("小天正在漫游中");
        pg.setCancelable(false);
        pg.show();
    }
    private String getAppInfo() {
 	try {
 			String pkName = getActivity().getPackageName();
 			String versionName = getActivity().getPackageManager().getPackageInfo(
                     					pkName, 0).versionName;
 			int versionCode = getActivity().getPackageManager()
 					.getPackageInfo(pkName, 0).versionCode;
 		//	return pkName + "   " + versionName + "  " + versionCode;
 			return versionName;
 		} catch (Exception e) {
 		}
 		return null;
 	}

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getActivity(),"网络好像出现了点问题！",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Jsoup();
                    break;
            }
        }
    };

}
