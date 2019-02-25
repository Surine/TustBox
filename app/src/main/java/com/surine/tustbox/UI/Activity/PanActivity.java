package com.surine.tustbox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.surine.tustbox.Pojo.EventBusBean.TustPanBus;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.FunctionTag;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.UI.Fragment.Pan.NonRootDirFragment;
import com.surine.tustbox.UI.Fragment.Pan.RootDirFragment;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.Helper.Interface.AddFunctionListenter;
import com.surine.tustbox.R;
import com.surine.tustbox.Service.DownloadService;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.UI.View.LittleProgramToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.FormData.LOCALE_DATA;
import static com.surine.tustbox.App.Data.UrlData.nonRootDir;

public class PanActivity extends TustBaseActivity {

    @BindView(R.id.toolbar_pan)
    LittleProgramToolbar toolbarPan;
    @BindView(R.id.search)
    EditText search;

    private String myGroupUrl;
    private String groupInfo;
    private String userId;
    private String token;
    private String rootIdForSearch;  //为查询准备的rootId
    private String pathForSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_pan);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        //加载首页,标题，添加额外功能，隐藏校园网连接提示，增加功能监听器
        toolbarPan.setTitle(getString(R.string.tustPanString))
                .setAddFunctionVisible(true)
                .setNoteGone(true)
                .setOnClickAddFunctionListener(new AddFunctionListenter() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(PanActivity.this, DownloadPageActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.main_enter_anim,
                                R.anim.main_exit_anim);
                    }
                });

        userId = SharedPreferencesUtil.Read(this, FormData.USERID, null);
        token = SharedPreferencesUtil.Read(this, FormData.TOKEN, null);
        //获取根目录加载url
        myGroupUrl = getRootUrl();
        //加载根目录
        loadGroupFolderGet(myGroupUrl);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(rootIdForSearch == null || rootIdForSearch.isEmpty() || pathForSearch == null || pathForSearch.isEmpty()){
                    ToastUtil.show(getString(R.string.search_error));
                }else{
                    String input = search.getText().toString();
                    if(input == null || input.isEmpty()){
                        return true;
                    }
                    String searchUrl = UrlData.searchUrl + "?query="+
                            input+"&token="+token+"&root_id="+rootIdForSearch+
                            "&path="+pathForSearch+"&locale=zh_CN&_="+System.currentTimeMillis();
                    search(searchUrl);
                }
            search.setText("");
            return true;
            }
        });

    }

    private void search(String searchUrl) {
        HttpUtil.get(searchUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.netError();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                try {
                    final String fileInfo = new JSONObject(r).getString("entries");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                            tran.addToBackStack("");
                            tran.add(R.id.content, NonRootDirFragment.getInstance(fileInfo)).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 1) {
                fragmentManager.popBackStackImmediate();
            } else {
                finish();
            }
        }
        return true;
    }

    private String getRootUrl() {
        return UrlData.groupUrl + userId + "/groups?token=" + token + "&is_activated=true&is_blocked=false&locale=zh_CN";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadGroupFolderGet(String url) {
        HttpUtil.get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                try {
                    groupInfo = new JSONObject(r).getString("entries");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //加载云盘根
                            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                            tran.addToBackStack("");
                            tran.add(R.id.content, RootDirFragment.getInstance(groupInfo)).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //消息接收器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(TustPanBus event) {
        if (event.getCode() == FunctionTag.PAN_CODE) {
            //第一个参数是文件下载url，第二个是文件名
            rootIdForSearch = event.getRoot_id();
            pathForSearch = event.getDir_name();
            loadNonRoot(rootIdForSearch, pathForSearch);
        } else if (event.getCode() == FunctionTag.PAN_DOWNLOAD) {
            readyToDownload(event.getRoot_id(), event.getDir_name());
        }
    }

    private void readyToDownload(String url, String fileName) {
        Intent intent = new Intent(PanActivity.this, DownloadService.class);
        intent.putExtra("DOWN_URL", url);
        intent.putExtra("DOWN_NAME", fileName);
        startService(intent);
    }

    private void loadNonRoot(String rootId, String dirName) {
        String url = getNonRootUrl(rootId, dirName);
        loadGroupFolderPost(url, rootId, dirName);  //加载数据
    }

    //获取非根目录url
    private String getNonRootUrl(String rootId, String message) {
        return nonRootDir + "root_id=" + rootId + "&path=" + message + "&token=" + token + "&locale=zh_CN";
    }


    private void loadGroupFolderPost(String url, String rootId, String dirName) {

        LogUtil.d(url);
        LogUtil.d(rootId);
        LogUtil.d(dirName);

        FormBody formBody = new FormBody.Builder()
                .add("root_id", rootId)
                .add("path", dirName)
                .add("token", token)
                .add("locale", LOCALE_DATA)
                .build();

        HttpUtil.post(url, formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                LogUtil.d("r" + r);
                groupInfo = r;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                        tran.addToBackStack("");
                        tran.add(R.id.content, NonRootDirFragment.getInstance(groupInfo)).commit();
                    }
                });

            }
        });
    }

}
