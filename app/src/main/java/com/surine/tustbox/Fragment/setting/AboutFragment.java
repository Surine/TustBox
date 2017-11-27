package com.surine.tustbox.Fragment.setting;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by surine on 2017/4/17.
 */

public class AboutFragment extends Fragment{
    private static final long CONNECT_TIMEOUT = 5;
    String update_message;
    private ProgressDialog pg;
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    TextView share;
    TextView textView32;
    TextView textView30;
    TextView textView33;
    TextView textView35;
    TextView textView36;
    TextView textView37;
    TextView textView38;
    TextView textView40;
    TextView textView41;
    TextView textView42;
    private String version = "";
    private String log = "";
    private int is_ness = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about_app, container, false);
        getActivity().setTitle(getString(R.string.about_app));
        initOkHttp();
        share = (TextView) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //share();
            }
        });


        textView32 = (TextView) view.findViewById(R.id.textView32);
        textView32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.surine_is_me,Toast.LENGTH_SHORT).show();
            }
        });

        textView30 = (TextView) view.findViewById(R.id.textView30);
        textView30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cp_qq();  //copy the qq number
            }
        });

        textView33 = (TextView) view.findViewById(R.id.textView33);
        textView33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendbug();  //send bug to me
            }
        });

        textView35 = (TextView) view.findViewById(R.id.textView35);
        textView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Check_update();  //check update
            }
        });

        textView36 = (TextView) view.findViewById(R.id.textView36);
        textView36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SettingActivity.class).putExtra("set_",5));
            }
        });

        textView37 = (TextView) view.findViewById(R.id.textView37);
        textView37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("activity_flag",2));
            }
        });

        textView38 = (TextView) view.findViewById(R.id.textView38);
        textView38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlipay();
            }
        });

        textView40 = (TextView) view.findViewById(R.id.textView40);
        textView40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(),"卖个萌！",Toast.LENGTH_SHORT).show();
            }
        });

        textView41 = (TextView) view.findViewById(R.id.all_log);
        textView41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Toast.makeText(getActivity(),"服务器处于维护阶段，公告功能将在不久后上线！",Toast.LENGTH_SHORT).show();
            }
        });
        textView42 = (TextView) view.findViewById(R.id.join_celitea);
        textView42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = UrlData.celitea_url;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent .setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        return view;
    }

    private void startAlipay() {
        PackageManager packageManager = null;
        try {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText("2234503567@qq.com");
            Toast.makeText(getActivity(), "支付宝账号已复制",Toast.LENGTH_SHORT).show();
            packageManager = getActivity().getPackageManager();
            startActivity(packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOkHttp() {
        //init the okhttpclient and set the cookiejar for the data request
        builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//设置连接超时时间;
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }

    private void cp_qq() {
        PackageManager packageManager = null;
        try {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            cmb.setText(getString(R.string.qq_number));
            Toast.makeText(getActivity(), R.string.qq_is_copy,Toast.LENGTH_SHORT).show();
            packageManager = getActivity().getPackageManager();
            Intent intent=new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent=new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.tim");
            startActivity(intent);
        }
    }



    private void sendbug() {
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用  
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用  
        Uri uri = Uri.parse(getString(R.string.qq_email_mo));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, getString(R.string.qq_email)); // 抄送人  
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.welcome_send_bug) + android.os.Build.MODEL + "SDK:" + android.os.Build.VERSION.SDK); // 主题  
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.what_do_you_want_to_say));// 正文  
        startActivity(Intent.createChooser(intent, getString(R.string.type)));
    }

    private void Check_update() {
        setDialog();
        StartCheck();
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

    private void StartCheck() {
        HttpUtil.get(UrlData.update_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //failure
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(getActivity(),"网络好像出现了点问题！",Toast.LENGTH_SHORT).show();
                       pg.dismiss();
                   }
               });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                update_message = response.body().string().toString();
                getActivity().runOnUiThread(new Runnable() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        if(version.equals(getAppInfo())||version.equals("")||version == null){
                            //this version
                            builder.setTitle("已经是最新版本呐！");
                            builder.setMessage(log);
                            builder.setPositiveButton(R.string.ok,null);
                        }else{
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
                            builder.setNegativeButton("残忍拒绝",null);
                        }
                        builder.show();
                        pg.dismiss();
                    }
                });
            }
        });

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
}
