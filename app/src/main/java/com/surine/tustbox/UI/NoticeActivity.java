package com.surine.tustbox.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.surine.tustbox.Adapter.Recycleview.NoticeAdapter;
import com.surine.tustbox.Bean.Message;
import com.surine.tustbox.Bean.Notice;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Surine on 2018/8/14.
 */

public class NoticeActivity extends TustBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private Context context;
    NoticeAdapter noticeAdapter;
    private List<Notice> list = new ArrayList<>();
    private List<Notice> noticesFromServer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        context = this;
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("通知");
        toolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);


        recycleview.setLayoutManager(new LinearLayoutManager(context));
        noticeAdapter = new NoticeAdapter(R.layout.item_notice,list);
        recycleview.setAdapter(noticeAdapter);

        initData();
    }

    private void initData() {

        HttpUtil.get(UrlData.getNoticeFromServer).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(recycleview,R.string.network_no_connect,Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getInt(FormData.JCODE) == 200){
                        String s1 = jsonObject.getString(FormData.JDATA);
                        noticesFromServer.clear();
                        noticesFromServer = GsonUtil.parseJsonWithGsonToList(s1, Notice.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadData();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(recycleview,R.string.server_error,Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadData() {
        for (Notice notice:noticesFromServer) {
            list.add(notice);
            noticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    //set the back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
