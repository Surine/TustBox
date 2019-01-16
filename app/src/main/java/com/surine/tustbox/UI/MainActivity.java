package com.surine.tustbox.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.Fragment.main.MainFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 2018年2月20日 重构，UI重设计
 */


public class MainActivity extends TustBaseActivity {
    final static String[] str = new String[]{
            "第一周", "第二周", "第三周", "第四周", "第五周", "第六周",
            "第七周", "第八周", "第九周", "第十周", "第十一周", "第十二周",
            "第十三周", "第十四周", "第十五周", "第十六周", "第十七周", "第十八周",
            "第十九周", "第二十周", "第二十一周", "第二十二周", "第二十三周", "第二十四周",
    };
    int yourChoice;   //周选择变量

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;


    private String update_message;
    private String version = "";
    private String log = "";
    private View view;
    private Context context;
    private Badge badge;
    private View itemView;
    private final int REQUEST_WRITE = 1;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        //设置toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);
        setTitle(TimeUtil.getNowMonthCC());
        //设置界面框架
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(R.id.content, MainFragment.getInstance("1")).commit();
        //首次登录
        SharedPreferencesUtil.save(this, "is_login", true);
        //更新桌面小部件
        updateWidget();
        getUnReadNum();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //如果没申请，那么需要申请
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求存储
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);
            }
        }
    }

    private void getUnReadNum() {
        //获取未读数量
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String buildUrl = UrlData.getMessageNum + "?" + FormData.toUser + "=" + tust_number + "&" + FormData.token + "=" + token;
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG", x);
                try {
                    final JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (badge != null) {
                                        badge.hide(true);
                                    }
                                    badge = new QBadgeView(MainActivity.this).bindTarget(mToolbar).setBadgeNumber(jsonObject.getInt(FormData.JDATA)).setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                                        @Override
                                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                                            if (dragState == 5) {
                                                //拖拽全部已读
                                                AllRead();
                                            }
                                        }
                                    });
                                    badge.isDraggable();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (badge != null) {
                                    badge.hide(true);
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void AllRead() {
        //全部已读
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String buildUrl = UrlData.readAll + "?" + FormData.toUser + "=" + tust_number + "&" + FormData.token + "=" + token;
        Log.d("TAG", buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG", x);
                try {
                    final JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //成功
                                Toast.makeText(context, R.string.MainActivityAllRead, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUnReadNum();
    }

    private void Share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.welcome) + UrlData.download_url);
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表  
        startActivity(Intent.createChooser(intent, getString(R.string.more_share)));
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.week:
               // show_Dialog(); //show the dialog(choose week)
                showListDialog();
                break;
            case R.id.message:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                break;
            case R.id.account:
                String uid = SharedPreferencesUtil.Read(MainActivity.this, FormData.tust_number_server, "");
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class).putExtra(FormData.uid, uid);
                startActivity(intent);
                break;
        }
        return true;
    }

    //显示列表对话框
    private void showListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择本周");
        yourChoice = -1;
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
                if (yourChoice != -1) {
                    //储存，通知更新UI
                    SharedPreferencesUtil.save(MainActivity.this, Constants.CHOOSE_WEEK, yourChoice + 1);
                    Toast.makeText(MainActivity.this, getString(R.string.choose_) + str[yourChoice], Toast.LENGTH_SHORT).show();
                    setTitle((SharedPreferencesUtil.Read(MainActivity.this, Constants.CHOOSE_WEEK, 1)) + "周");
                    updateWidget();
                    //发送周更新通知给fragment
                    EventBus.getDefault().post(new SimpleEvent(0, "UPDATE"));
                }
            }
        }).show();
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
                SharedPreferencesUtil.save(MainActivity.this, Constants.CHOOSE_WEEK, yourChoice + 1);
                Toast.makeText(MainActivity.this, getString(R.string.choose_) + str[yourChoice], Toast.LENGTH_SHORT).show();
                setTitle((SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
                //title.setText("第" + (SharedPreferencesUtil.Read(MainActivity.this, "choice_week", 0)) + "周");
                updateWidget();
                //发送周更新通知给fragment
                EventBus.getDefault().post(new SimpleEvent(0, "UPDATE"));
            }
            }
        });
        builder.show();
    }

    private void updateWidget() {
        //发送桌面小部件广播
        Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(updateIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功
                Toast.makeText(context, "mua~ 你真是个优秀的家伙哦！", Toast.LENGTH_SHORT).show();
            } else {
                //申请失败
                Toast.makeText(context, "不给我权限？那宝宝不给你更新APP了！哼！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
