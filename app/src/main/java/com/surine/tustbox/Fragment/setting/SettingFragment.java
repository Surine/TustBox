package com.surine.tustbox.Fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.surine.tustbox.R;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.Util.IOUtil;

/**
 * Created by surine on 2017/4/8.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference Setting_back;
    private Preference About;
    private Preference clear_cache;

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
    }


    private void findview() {
        Setting_back = findPreference("setting_back");
        About = findPreference("about");
        clear_cache = findPreference("clear_cache");
        Setting_back = findPreference("setting_back");
        Setting_back = findPreference("setting_back");
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
        }
        return false;
    }

    private void clear_cache() {
        /** * 清除本应用内部缓存 */
        String cachePath2 =  getActivity().getCacheDir().getPath();
        IOUtil.delAllFile(cachePath2,getActivity());
        Toast.makeText(getActivity(),"缓存清理成功，请重新选择背景",Toast.LENGTH_SHORT).show();
    }

}
