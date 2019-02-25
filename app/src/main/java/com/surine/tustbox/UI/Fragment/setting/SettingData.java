package com.surine.tustbox.UI.Fragment.setting;

import android.content.Context;
import android.widget.Switch;
import android.widget.TextView;

import com.surine.tustbox.Pojo.SettingItem;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import static com.surine.tustbox.App.Data.Constants.ABOUT;
import static com.surine.tustbox.App.Data.Constants.ABOUT_SUM;
import static com.surine.tustbox.App.Data.Constants.ACCOUNTBIND;
import static com.surine.tustbox.App.Data.Constants.ACCOUNTBIND_SUM;
import static com.surine.tustbox.App.Data.Constants.BANNERBACKOUND;
import static com.surine.tustbox.App.Data.Constants.BANNERBACKOUND_SUM;
import static com.surine.tustbox.App.Data.Constants.FEEDBACK;
import static com.surine.tustbox.App.Data.Constants.FEEDBACK_SUM;
import static com.surine.tustbox.App.Data.Constants.HIDEBOTTOM;
import static com.surine.tustbox.App.Data.Constants.HIDEBOTTOM_SUM;
import static com.surine.tustbox.App.Data.Constants.INDEXCARD;
import static com.surine.tustbox.App.Data.Constants.INDEXCARD_SUM;
import static com.surine.tustbox.App.Data.Constants.NORMALPAGE;
import static com.surine.tustbox.App.Data.Constants.NORMALPAGE_SUM;
import static com.surine.tustbox.App.Data.Constants.QQ;
import static com.surine.tustbox.App.Data.Constants.QQ_SUM;
import static com.surine.tustbox.App.Data.Constants.SUPPORT;
import static com.surine.tustbox.App.Data.Constants.SUPPORT_SUM;
import static com.surine.tustbox.App.Data.Constants.WIDGETBACKOUND;
import static com.surine.tustbox.App.Data.Constants.WIDGETBACKOUND_SUM;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Read;

/**
 * Created by Surine on 2019/1/31.
 */

public class SettingData {

    public static List<SettingItem> getBaseData(Context context){
        List<SettingItem> list = new ArrayList<>();
        SettingItem settingItem = new SettingItem(
                R.drawable.ic_action_about,
                ACCOUNTBIND,
                ACCOUNTBIND_SUM,
                null
        );
        SettingItem settingItem2 = new SettingItem(
                R.drawable.ic_action_about,
                 BANNERBACKOUND,
                BANNERBACKOUND_SUM,
                null
        );

        Switch switchItem3 = new Switch(context);
        switchItem3.setChecked(Read(context,WIDGETBACKOUND
                ,false));
        SettingItem settingItem3 = new SettingItem(
                R.drawable.ic_action_about,
                WIDGETBACKOUND,
                WIDGETBACKOUND_SUM,
                switchItem3
        );
        SettingItem settingItem4 = new SettingItem(
                R.drawable.ic_action_about,
                INDEXCARD,
                INDEXCARD_SUM,
                null
        );

        TextView textView = new TextView(context);
        SettingItem settingItem5 = new SettingItem(
                R.drawable.ic_action_about,
                NORMALPAGE,
                NORMALPAGE_SUM,
                textView
        );

        Switch switchItem6 = new Switch(context);
        switchItem6.setChecked(Read(context,HIDEBOTTOM
                ,false));
        switchItem6.setClickable(false);
        SettingItem settingItem6 = new SettingItem(
                R.drawable.ic_action_about,
                HIDEBOTTOM,
                HIDEBOTTOM_SUM,
                switchItem6
        );
        SettingItem settingItem7 = new SettingItem(
                R.drawable.ic_action_about,
                FEEDBACK,
                FEEDBACK_SUM,
                null
        );
        SettingItem settingItem8 = new SettingItem(
                R.drawable.ic_action_about,
                QQ,
                QQ_SUM,
                null
        );
        SettingItem settingItem9 = new SettingItem(
                R.drawable.ic_action_about,
                SUPPORT,
                SUPPORT_SUM,
                null
        );
        SettingItem settingItem10 = new SettingItem(
                R.drawable.ic_action_about,
                ABOUT,
                ABOUT_SUM,
                null
        );
        list.add(settingItem);
        list.add(settingItem2);
        list.add(settingItem3);
        list.add(settingItem4);
        list.add(settingItem5);
        list.add(settingItem6);
        list.add(settingItem7);
        list.add(settingItem8);
        list.add(settingItem9);
        list.add(settingItem10);

        return list;
    }

    public static List<SettingItem> getProData(Context context){
        List<SettingItem> list = new ArrayList<>();

        TextView textView2 = new TextView(context);
        textView2.setText("教程");
        SettingItem settingItem = new SettingItem(
                R.drawable.ic_action_about,
                "首页标题Pattern",
                "配置您自己的标题",
                textView2
        );
        SettingItem settingItem2 = new SettingItem(
                R.drawable.ic_action_about,
                "StatusColor",
                "请填写颜色代码,默认为#00000000",
                null
        );
        SettingItem settingItem3 = new SettingItem(
                R.drawable.ic_action_about,
                "首页StatusBarTextColor",
                "Banner不适合的时候请自行调节",
                null
        );
        SettingItem settingItem4 = new SettingItem(
                R.drawable.ic_action_about,
                "网络检测",
                "Biu！",
                null
        );

        SettingItem settingItem5 = new SettingItem(
                R.drawable.ic_action_about,
                "小天实验室",
                "Boom！",
                null
        );
        SettingItem settingItem6 = new SettingItem(
                R.drawable.ic_action_about,
                "清除全部数据",
                "小心哦！",
                null
        );
        list.add(settingItem);
        list.add(settingItem2);
        list.add(settingItem3);
        list.add(settingItem4);
        list.add(settingItem5);
        list.add(settingItem6);
        return list;
    }
}
