package com.surine.tustbox.Fragment.setting;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.Util.HttpUtil;
import com.tencent.bugly.beta.Beta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by surine on 2017/4/17.
 */

public class AboutFragment extends Fragment {
    private static final long CONNECT_TIMEOUT = 5;
    String update_message;
    @BindView(R.id.osltext)
    TextView osl;
    @BindView(R.id.update)
    TextView update;
    Unbinder unbinder;
    @BindView(R.id.textView16)
    TextView textView16;

    private ProgressDialog pg;

    private String version = "";
    private String log = "";
    private int is_ness = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about_app, container, false);
        getActivity().setTitle(getString(R.string.about_app));

        unbinder = ButterKnife.bind(this, view);

        textView16.setText("v-"+getAppInfo());
        return view;
    }









    private void Check_update() {
        Beta.checkUpgrade(true,false);
       // setDialog();
       // StartCheck();
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
                        Toast.makeText(getActivity(), "网络好像出现了点问题！", Toast.LENGTH_SHORT).show();
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
                        if (version.equals(getAppInfo()) || version.equals("") || version == null) {
                            //this version
                            builder.setTitle("已经是最新版本呐！");
                            builder.setMessage(log);
                            builder.setPositiveButton(R.string.ok, null);
                        } else {
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
                            builder.setNegativeButton("残忍拒绝", null);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.osltext, R.id.update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.osltext:
                startActivity(new Intent(getActivity(), SettingActivity.class).putExtra("set_", 4));
                break;
            case R.id.update:
                Check_update();
                break;
        }
    }
}
