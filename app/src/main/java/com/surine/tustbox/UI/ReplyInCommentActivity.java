package com.surine.tustbox.UI;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.ReplyAdapter;
import com.surine.tustbox.Bean.Comment;
import com.surine.tustbox.Bean.Reply;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.DOWNTOREFRESH;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.UPTOREFRESH;

/**
 * Created by Surine on 2018/2/24.
 */

public class ReplyInCommentActivity extends TustBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_comment_head)
    CircleImageView itemCommentHead;
    @BindView(R.id.item_comment_username)
    TextView itemCommentUsername;
    @BindView(R.id.item_comment_time)
    TextView itemCommentTime;
    @BindView(R.id.comment_info)
    TextView commentInfo;
    @BindView(R.id.comment_layout)
    RelativeLayout commentLayout;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.replyComment)
    FloatingActionButton replyComment;
    private Context context;
    private int cid = 0;
    private List<Reply> replys = new ArrayList<>();
    private ReplyAdapter adapter;
    private int pageNum = 1;
    private List<Reply> replysFromServer = new ArrayList<>();
    private String uid = null;
    private CharSequence[] items;
    private int comment_position = 0;
    private int positionInReplys = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_reply_in_comment);
        ButterKnife.bind(this);
        context = this;
        EventBus.getDefault().register(this);
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("评论");
        toolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);

        cid = getIntent().getIntExtra(FormData.cid, 0);
        comment_position = getIntent().getIntExtra(FormData.POSITION, 0);
        initCommentInfo(cid);

        adapter = new ReplyAdapter(R.layout.item_reply_in_comment, replys);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(adapter);
        adapter.setPreLoadNumber(2);
        View noView = getLayoutInflater().inflate(R.layout.view_empty_action, (ViewGroup) recycleview.getParent(), false);
        adapter.setEmptyView(noView);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                // loadMore();   //加载更多数据
            }
        });

        //点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String type = SharedPreferencesUtil.Read_safe(context, FormData.USER_TYPE, "0");
                if(type.equals("3")){
                    Toast.makeText(context, R.string.SendActionActivityAccountError, Toast.LENGTH_SHORT).show();
                }else{
                    sendReplyInComment(replys.get(position).getUid());
                }
            }
        });

        //长按事件
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final String items_with_permission[] = {"复制内容", "删除回复"};
                final String items_no_permission[] = {"复制内容"};
                String tust_number = SharedPreferencesUtil.Read(context, "tust_number", "");
                String type = SharedPreferencesUtil.Read_safe(context, FormData.USER_TYPE, "0");
                if(replys.get(position).getUid().equals(tust_number)||type.equals("1")){
                   items = items_with_permission;
                }else{
                    items = items_no_permission;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(which == 0){
                          //复制内容
                            ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            cmb.setText(replys.get(position).getSay_info());
                            Toast.makeText(context, R.string.ReplyInCommentActivityClipSuccess, Toast.LENGTH_SHORT).show();
                        }else if(which == 1){
                            positionInReplys = position;
                            //删除回复
                            deleteReply(position);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view == adapter.getViewByPosition(recycleview, position, R.id.item_reply_head)) {
                    //启动个人详情页
                    startActivity(new Intent(context, UserInfoActivity.class).putExtra(FormData.uid, replys.get(position).getUid()));
                }
            }
        });

        //初始化加载最新数据
        initServerReplyData(DOWNTOREFRESH, 1);
    }

    //删除回复
    private void deleteReply(int position) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        HttpUtil.get(UrlData.deleteReplyById+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+tust_number+"&"+FormData.id+"="+replys.get(position).getId()).enqueue(new Callback() {
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
                               //删除成功
                               //通知自身列表更新
                               updateList(positionInReplys);
                               //通知更新回复列表
                               EventBus.getDefault().post(new SimpleEvent(6,String.valueOf(comment_position)));
                               positionInReplys = 0;
                           }
                       });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.ReplyInCommentActivityDeleteFail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateList(int position) {
        replys.remove(position);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
    }

    private void initServerReplyData(final boolean b, int i) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, "TOKEN", "");
        String tust_number = SharedPreferencesUtil.Read(context, "tust_number", "");
        String buildUrl = UrlData.getReply + "?" + FormData.token + "=" + token + "&" + FormData.page + "=" + i + "&" + FormData.cid + "=" + cid;
        Log.d("TAG", buildUrl);
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
                final String s = response.body().string();
                Log.d("TAG", s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        String s1 = jsonObject.getString(FormData.JDATA);
                        replysFromServer.clear();
                        replysFromServer = GsonUtil.parseJsonWithGsonToList(s1, Reply.class);
                        Log.d("TAG", "本次加载从服务器返回" + replysFromServer.size() + "条数据");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    updataMoreData();
                                } else {
                                    updataData();
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    adapter.loadMoreFail();
                                } else {
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

    //加载更多数据
    private void loadMore() {
        //只有在加载更多数据的时候，参数为true
        pageNum = pageNum + 1;
        initServerReplyData(UPTOREFRESH, pageNum);
    }

    private void updataData() {
        replys.clear();
        for (Reply reply : replysFromServer) {
            replys.add(reply);
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
        for (Reply reply : replysFromServer) {
            replys.add(reply);
        }
        replysFromServer.clear();
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
    }


    //初始化评论内容
    private void initCommentInfo(int cid) {
        String token = SharedPreferencesUtil.Read_safe(context, "TOKEN", "");
        HttpUtil.get(UrlData.getCommentById + "?" + FormData.id + "=" + cid + "&" + FormData.token + "=" + token).enqueue(new Callback() {
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
                Log.d("TAF", x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        final Comment comment = GsonUtil.parseJsonWithGson(jsonObject.getString(FormData.JDATA), Comment.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (comment.getUser().getFace() != null) {
                                    //设置头像
                                    Glide.with(context).load(comment.getUser().getFace()).into(itemCommentHead);
                                }
                                //加载昵称
                                if(comment.getUser()!=null){
                                    if(comment.getUser().getSchoolname() == null||comment.getUser().getSchoolname().equals("")){
                                        if(comment.getUser().getNick_name() == null){
                                            itemCommentUsername.setText("未设置");
                                        }else{
                                            itemCommentUsername.setText(comment.getUser().getNick_name());
                                        }
                                    }else{
                                        itemCommentUsername.setText(comment.getUser().getSchoolname());
                                    }
                                }


                                if (comment.getComment_time() != null) {
                                    //设置时间
                                    itemCommentTime.setText(comment.getComment_time());
                                }
                                if (comment.getComment_content() != null) {
                                    //设置内容
                                    commentInfo.setText(comment.getComment_content());
                                }
                                if(comment.getUid() != null){
                                    uid = comment.getUid();
                                }

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



    private void sendReplyInComment(final String at) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.ActionInfoActivitySendComment);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_view_editview, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_view);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送
                if (!editText.getText().equals("")) {
                    sendReplyInCommentToServer(editText.getText().toString(), at);
                } else {
                    Toast.makeText(context, R.string.SendActionActivityNullParam, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void sendReplyInCommentToServer(String s, String at) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, "TOKEN", "");
        String tust_number = SharedPreferencesUtil.Read(context, "tust_number", "");
        FormBody formBody = new FormBody.Builder()
                .add(FormData.token, token)
                .add(FormData.cid, String.valueOf(cid))
                .add(FormData.uid, tust_number)
                .add(FormData.say_info, s)
                .add(FormData.say_at, at)
                .build();

        HttpUtil.post(UrlData.addReplyInComment, formBody).enqueue(new Callback() {
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
                Log.d("TAG", x);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.ActionInfoActivityCommentSuccess, Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new SimpleEvent(3, "S"));
                                EventBus.getDefault().post(new SimpleEvent(2, "S"));  //更新动态详情页列表

                            }
                        });
                    } else if (jsonObject.getInt(FormData.JCODE) == 401) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.null_input, Toast.LENGTH_SHORT).show();
                                // finish();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo) {
        if (simpleBusInfo.getId() == 3) {
            Log.d("TAG", "收到信息");
            initServerReplyData(DOWNTOREFRESH, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.item_comment_head, R.id.replyComment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_comment_head:
                startActivity(new Intent(context, UserInfoActivity.class).putExtra(FormData.uid,uid));
                break;
            case R.id.replyComment:
                String type = SharedPreferencesUtil.Read_safe(context, FormData.USER_TYPE, "0");
                if(type.equals("3")){
                    Toast.makeText(context, R.string.SendActionActivityAccountError, Toast.LENGTH_SHORT).show();
                }else {
                    //发送回复
                    sendReplyInComment("");
                }
                break;
        }
    }
}
