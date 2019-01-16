package com.surine.tustbox.Fragment.setting;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Switch;
import android.widget.Toast;

import com.surine.tustbox.R;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.Util.IOUtil;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by surine on 2017/4/8.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference Setting_back;
    private Preference About;
    private Preference clear_cache;
    private SwitchPreference picCrop;
    private SwitchPreference widget_image_button;
    private Preference qq;
    private Preference bug;
    private Preference alipay;
    private Preference notice;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        getActivity().setTitle(getString(R.string.setting));
        findview();   //initview
        setLinsener();   //setlinstener
    }

    private void setLinsener() {
        Setting_back.setOnPreferenceClickListener(this);
        About.setOnPreferenceClickListener(this);
        clear_cache.setOnPreferenceClickListener(this);
        picCrop.setOnPreferenceClickListener(this);
        widget_image_button.setOnPreferenceClickListener(this);
        qq.setOnPreferenceClickListener(this);
        bug.setOnPreferenceClickListener(this);
        alipay.setOnPreferenceClickListener(this);
        notice.setOnPreferenceClickListener(this);
    }


    private void findview() {
        Setting_back = findPreference("setting_back");
        About = findPreference("about");
        clear_cache = findPreference("clear_cache");
        Setting_back = findPreference("setting_back");
        Setting_back = findPreference("setting_back");
        picCrop = (SwitchPreference) findPreference("pic_crop");
        widget_image_button = (SwitchPreference) findPreference("widget_image_button");
        qq = findPreference("qq");
        bug = findPreference("bug");
        alipay = findPreference("alipay");
        notice = findPreference("notice");
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == Setting_back){
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",1));
        }else if(preference == About){
            //about
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",2));
        }
        else if(preference == clear_cache){
            clear_cache();
        }else if(preference == qq){
            cp_qq();
        }else if(preference == bug){
            sendbug();
        }else if(preference == alipay){
            startAlipay();
        }else if(preference == notice){
            startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",5));
        }
        return false;
    }

    private void startAlipay() {
        PackageManager packageManager = null;
        try {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText("2234503567@qq.com");
            Toast.makeText(getActivity(), "支付宝账号已复制", Toast.LENGTH_SHORT).show();
            packageManager = getActivity().getPackageManager();
            startActivity(packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendbug() {
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用  
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用  
        Uri uri = Uri.parse(getString(R.string.qq_email_mo));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, getString(R.string.qq_email)); // 抄送人  
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.welcome_send_bug) + Build.MODEL + "SDK:" + Build.VERSION.SDK); // 主题  
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.what_do_you_want_to_say));// 正文  
        startActivity(Intent.createChooser(intent, getString(R.string.type)));
    }

    private void cp_qq() {
        PackageManager packageManager = null;
        try {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(getString(R.string.qq_number));
            Toast.makeText(getActivity(), R.string.qq_is_copy, Toast.LENGTH_SHORT).show();
            packageManager = getActivity().getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.tim");
            startActivity(intent);
        }
    }

    private void clear_cache() {
        /** * 清除本应用内部缓存 */
        String cachePath2 =  getActivity().getCacheDir().getPath();
        IOUtil.delAllFile(cachePath2,getActivity());
        Toast.makeText(getActivity(),"缓存清理成功，请重新选择背景",Toast.LENGTH_SHORT).show();
    }

}
