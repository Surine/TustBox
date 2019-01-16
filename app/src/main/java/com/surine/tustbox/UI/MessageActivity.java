package com.surine.tustbox.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.MessageAdapter;
import com.surine.tustbox.Bean.Message;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

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

import static com.surine.tustbox.Data.Constants.*;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.DOWNTOREFRESH;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.UPTOREFRESH;

public class MessageActivity extends TustBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;
    private Context context;
    int pageNum = 1;
    private List<Message> messagesFromServer = new ArrayList<>();
    private int messagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        context = this;
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("消息");
        toolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);
        //设置recycleview
        adapter = new MessageAdapter(R.layout.item_message, messages);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(adapter);
        adapter.setPreLoadNumber(2);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();   //加载更多数据
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recycleview,position,R.id.item_message_head)){
                    //启动个人详情页
                    startActivity(new Intent(context, UserInfoActivity.class).putExtra(FormData.uid,messages.get(position).getFromuser()));
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                messagePosition = position;
                Read();
                if(messages.get(position).getType() == LOVE_ACTION){
                    //跳转动态页面
                    startActivity(new Intent(context,ActionInfoActivity.class).putExtra(FormData.did,messages.get(position).getDid()));
                }else if(messages.get(position).getType() == COMMENT_ACTION){
                    //跳转动态页面
                    startActivity(new Intent(context,ActionInfoActivity.class).putExtra(FormData.did,messages.get(position).getDid()));
                }else if(messages.get(position).getType() == REPLY_COMMENT){
                    startActivity(new Intent(context,ReplyInCommentActivity.class).putExtra(FormData.cid,messages.get(position).getCid()));
                }else if(messages.get(position).getType() == REPLY_AT_SOMEONE){
                    startActivity(new Intent(context,ReplyInCommentActivity.class).putExtra(FormData.cid,messages.get(position).getCid()));
                }
            }
        });
        initServerData(DOWNTOREFRESH,1);
        adapter.setEmptyView(R.layout.view_empty_2,recycleview);
    }

    private void Read() {
        String token = SharedPreferencesUtil.Read_safe(context,FormData.token,"");
        String buildUrl = UrlData.read+"?"+FormData.token+"="+token+"&"+FormData.id+"="+messages.get(messagePosition).getId();
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG",x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if(jsonObject.getInt(FormData.JCODE) == 400){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.get(messagePosition).setIsread(1);
                                adapter.notifyItemChanged(messagePosition);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加载更多数据
    private void loadMore() {
        //只有在加载更多数据的时候，参数为true
        pageNum = pageNum +1;
        initServerData(UPTOREFRESH,pageNum);
    }

    private void initServerData(final boolean b, int page) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String buildUrl = UrlData.getMessageList+"?"+FormData.toUser+"="+tust_number+"&"+FormData.page+"="+page;
        Log.d("TAG",buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
               String x = response.body().string();
               Log.d("TAG",x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if(jsonObject.getInt(FormData.JCODE) == 400){
                        String s1 = jsonObject.getString("jdata");
                        messagesFromServer.clear();
                        messagesFromServer = GsonUtil.parseJsonWithGsonToList(s1, Message.class);
                        Log.d("TAG","本次加载从服务器返回"+messagesFromServer.size()+"条数据");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(b){
                                    updataMoreData();
                                }else{
                                    updataData();
                                }
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(b){
                                    adapter.loadMoreFail();
                                }else{
                                    Toast.makeText(context, R.string.ThirdPageFragmentNullContent, Toast.LENGTH_SHORT).show();
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

    private void updataData() {
        messages.clear();
        for(Message message : messagesFromServer){
            messages.add(message);
        }
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        recycleview.smoothScrollToPosition(0);
        //重新加载其他页
        pageNum = 1;
        //TODO:下拉加载后重新启动上拉加载
        adapter.setEnableLoadMore(true);
    }

    private void updataMoreData() {
        for(Message message : messagesFromServer){
            messages.add(message);
        }
        messagesFromServer.clear();
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
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
