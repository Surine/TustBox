package com.surine.tustbox.Helper.Utils;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.R;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by surine on 2017/11/17.
 * APP管理工具
 */

public class AppUtil {

    /**
     * 获取APP VersionName
     * */

    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将小天盒子分享给好友
     * */
    public static void getShare(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.welcome) + UrlData.download_url);
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表  
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.more_share)));
    }


    /**
     * 启动QQ
     * */
    public static void copyQQ(Context context){
        String qq = "com.tencent.mobileqq";
        String tim = "com.tencent.tim";
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        try {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(context.getString(R.string.qq_number));
            ToastUtil.show(context.getString(R.string.qq_is_copy));
            intent = packageManager.getLaunchIntentForPackage(qq);
        } catch (Exception e) {
            intent = packageManager.getLaunchIntentForPackage(tim);
        }
        context.startActivity(intent);
    }


    /**
     * 反馈
     * */
    public static void feedBack(Context context) {
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用  
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用  
        Uri uri = Uri.parse(context.getString(R.string.qq_email_mo));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, context.getString(R.string.qq_email)); // 抄送人  
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.welcome_send_bug) + Build.MODEL + "SDK:" + Build.VERSION.SDK); // 主题  
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.what_do_you_want_to_say));// 正文  
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.type)));
    }

    /**
     * 支付
     * */
    public static void pay(Context context) {
        PackageManager packageManager = null;
        try {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cmb.setText("2234503567@qq.com");
            Toast.makeText(context, "支付宝账号已复制", Toast.LENGTH_SHORT).show();
            packageManager = context.getPackageManager();
            context.startActivity(packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开微信小程序
     *
     * @param wxAppId       小程序绑定的APP的微信平台APPID
     * @param miniProgramId 小程序的原始ID
     * @param page          要打开的小程序页面
     * @param miniType  正式版:0，测试版:1，体验版:2
     */
    public static void launchMiniProgramByWxAppId(Context context,String wxAppId, String miniProgramId, String page,int miniType) {
        ContentResolver resolver =context.getContentResolver();
        Uri uri = Uri.parse("content://com.tencent.mm.sdk.comm.provider/launchWXMiniprogram");
        String[] selection = new String[]{wxAppId, miniProgramId, page, String.valueOf(miniType)};
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, selection, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
