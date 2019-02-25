package com.surine.tustbox.UI.Fragment.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.MessageAdapter;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;
import com.surine.tustbox.Pojo.Message;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.ActionInfoActivity;
import com.surine.tustbox.UI.Activity.ReplyInCommentActivity;
import com.surine.tustbox.UI.Activity.V5_UserInfoActivity;
import com.surine.tustbox.UI.Fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.Constants.COMMENT_ACTION;
import static com.surine.tustbox.App.Data.Constants.LOVE_ACTION;
import static com.surine.tustbox.App.Data.Constants.REPLY_AT_SOMEONE;
import static com.surine.tustbox.App.Data.Constants.REPLY_COMMENT;
import static com.surine.tustbox.App.Data.JCode.ERROR;
import static com.surine.tustbox.App.Data.JCode.J400;
import static com.surine.tustbox.Helper.Utils.ToastUtil.show;
import static com.surine.tustbox.UI.Fragment.PageFragment.SchoolPageFragment.DOWNTOREFRESH;

/**
 * Created by Surine on 2019/2/20.
 */

public class MomentMessageFragment extends BaseFragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;

    private TustBoxUtil tbUtil;
    private List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;
    private Context context;
    int pageNum = 1;
    private List<Message> messagesFromServer = new ArrayList<>();
    private int messagePosition = 0;


    @Override
    public void initData(View v) {
        unbinder = ButterKnife.bind(this, v);

        //初始化
        tbUtil = new TustBoxUtil(getActivity());
        context = mActivity;

        /*初始化适配器*/
        adapter = new MessageAdapter(R.layout.item_message, messages);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(adapter);
        adapter.setPreLoadNumber(2);
        adapter.setEmptyView(R.layout.view_empty_2,recycleview);

        /*加载更多监听器*/
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();   //加载更多数据
            }
        });

        /*点击头像监听器*/
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recycleview,position,R.id.item_message_head)){
                    //启动个人详情页
                    startActivity(new Intent(context, V5_UserInfoActivity.class).putExtra(FormData.uid,messages.get(position).getFromuser()));
                }
            }
        });

        /*点击消息项目监听器*/
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                messagePosition = position;
                //已读消息
                readMessage();
                //获取消息类型
                int msgType = messages.get(position).getType();
                if(msgType == LOVE_ACTION || msgType == COMMENT_ACTION) {
                    //跳转动态页面
                    startActivity(new Intent(context,ActionInfoActivity.class).putExtra(FormData.did,messages.get(position).getDid()));
                }else if(msgType == REPLY_COMMENT || msgType == REPLY_AT_SOMEONE){
                    //跳转道平路页面
                    startActivity(new Intent(context,ReplyInCommentActivity.class).putExtra(FormData.cid,messages.get(position).getCid()));
                }
            }
        });

        //加载第一页
        initServerData(DOWNTOREFRESH,1);

    }


    /**
     * 已读消息
     * */
    private void readMessage() {
        String token = tbUtil.getToken();
        String buildUrl = UrlData.read+"?"+FormData.token+
                "="+token+"&"+FormData.id+"="+messages.get(messagePosition).getId();
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RunOnUiThread.updateUi(getActivity(), new UpdateUIListenter() {
                    @Override
                    public void update() {
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
                        getActivity().runOnUiThread(new Runnable() {
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
        initServerData(Constants.UPTOREFRESH,pageNum);
    }


    /**
     * 从服务器加载数据，
     * */
    private void initServerData(final boolean b, int page) {
        //取得token和学号
        String token = tbUtil.getToken();
        String tustNumber = tbUtil.getUid();
        //构建url
        String buildUrl = UrlData.getMessageList+"?"+FormData.toUser
                +"="+tustNumber+"&"+FormData.page+"="+page;
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RunOnUiThread.updateUi(context, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String serverData = response.body().string();
                //直接UI更新
                RunOnUiThread.updateUi(context, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        try {
                            switch (JsonUtil.getStatus(serverData)){
                                case J400:
                                    messagesFromServer.clear();
                                    messagesFromServer = JsonUtil.getPojoList(serverData, Message.class);
                                    if(b)
                                        updataMoreData();  //加载更多
                                    else
                                        updataData();   //更新数据

                                    break;
                                case ERROR:
                                    if(b)
                                       adapter.loadMoreEnd();  //加载完成
                                    else {
                                        show(R.string.ThirdPageFragmentNullContent);
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            ToastUtil.jsonError();
                        }
                    }
                });
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
    public int getViewLayout() {
        return R.layout.recycleview;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
