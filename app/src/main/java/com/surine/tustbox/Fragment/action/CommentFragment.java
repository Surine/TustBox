package com.surine.tustbox.Fragment.action;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.CommentAdapter;
import com.surine.tustbox.Bean.Comment;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ReplyInCommentActivity;
import com.surine.tustbox.UI.UserInfoActivity;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.surine.tustbox.Fragment.PageFragment.ThirdPageFragment.DOWNTOREFRESH;
import static com.surine.tustbox.Fragment.PageFragment.ThirdPageFragment.UPTOREFRESH;

/**
 * Created by Surine on 2018/2/23.
 */

public class CommentFragment extends Fragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private CommentAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private String did = null;
    private List<Comment> commentsFromServer = new ArrayList<>();
    private int pageNum = 1;
    private String[] items;
    private int positionInComments = 0;

    public static CommentFragment getInstance(String title) {
        CommentFragment fra = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FormData.did, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
        did = bundle.getString(FormData.did);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycleview, container, false);
        unbinder = ButterKnife.bind(this, v);


        adapter = new CommentAdapter(R.layout.item_comment, comments);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(adapter);
        View noView = getActivity().getLayoutInflater().inflate(R.layout.view_empty_action, (ViewGroup) recycleview.getParent(), false);
        adapter.setEmptyView(noView);
        adapter.setPreLoadNumber(2);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();   //加载更多数据
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
              startActivity(new Intent(getActivity(), ReplyInCommentActivity.class).putExtra(FormData.cid,comments.get(position).getId())
                      .putExtra(FormData.POSITION,position));
            }
        });
        //初始化加载最新数据
        initServerData(DOWNTOREFRESH,1);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recycleview,position,R.id.item_comment_head)){
                    //启动个人详情页
                    startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra(FormData.uid,comments.get(position).getUid()));
                }
            }
        });

        //长按事件
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final String items_with_permission[] = {"复制内容", "删除回复"};
                final String items_no_permission[] = {"复制内容"};
                String tust_number = SharedPreferencesUtil.Read(getActivity(), "tust_number", "");
                String type = SharedPreferencesUtil.Read_safe(getActivity(), FormData.USER_TYPE, "0");
                if(comments.get(position).getUid().equals(tust_number)||type.equals("1")){
                    items = items_with_permission;
                }else{
                    items = items_no_permission;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(which == 0){
                            //复制内容
                            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                            cmb.setText(comments.get(position).getComment_content());
                            Toast.makeText(getActivity(), R.string.ReplyInCommentActivityClipSuccess, Toast.LENGTH_SHORT).show();
                        }else if(which == 1){
                            positionInComments = position;
                            //删除回复
                            deleteComment(position);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        return v;
    }

    private void deleteComment(int position) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(), FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        HttpUtil.get(UrlData.deleteCommentById+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+tust_number+"&"+FormData.id+"="+comments.get(position).getId()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.network_no_connect, Toast.LENGTH_SHORT).show();
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
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //删除成功
                                //通知自身列表更新
                                updateList(positionInComments);
                                //通知详情页面更新评论数
                                EventBus.getDefault().post(new SimpleEvent(7,"s"));
                                positionInComments = 0;
                            }
                        });

                    }else{
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), R.string.ReplyInCommentActivityDeleteFail, Toast.LENGTH_SHORT).show();
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
        comments.remove(position);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
    }

    //加载更多数据
    private void loadMore() {
        //只有在加载更多数据的时候，参数为true
        pageNum = pageNum +1;
        initServerData(UPTOREFRESH,pageNum);
    }

    private void initServerData(final boolean b, int i) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(),"TOKEN","");
        String tust_number = SharedPreferencesUtil.Read(getActivity(),"tust_number","");
        String buildUrl = UrlData.getComment+"?"+ FormData.token+"="+token+"&"+FormData.page+"="+i+"&"+FormData.did+"="+did;
        Log.d("TAG",buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("TAG",s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        String s1 = jsonObject.getString(FormData.JDATA);
                        commentsFromServer.clear();
                        commentsFromServer = GsonUtil.parseJsonWithGsonToList(s1, Comment.class);
                        Log.d("TAG","本次加载从服务器返回"+commentsFromServer.size()+"条数据");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(b){
                                    updataMoreData();
                                }else{
                                    updataData();
                                }
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(b){
                                    adapter.loadMoreFail();
                                }else{
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
        comments.clear();
        for(Comment comment : commentsFromServer){
            comments.add(comment);
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
        for(Comment comment : commentsFromServer){
            comments.add(comment);
        }
        commentsFromServer.clear();
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo){
        if(simpleBusInfo.getId() == 2){
            //tag == 1为更新，tag == 0 为初始加载
            Log.d("TAG","收到信息");
            initServerData(DOWNTOREFRESH,1);
        }else if(simpleBusInfo.getId() == 6){
            //更新回复数量
            updateReplyNum(Integer.parseInt(simpleBusInfo.getMessage()));
        }
    }

    private void updateReplyNum(int i) {
        comments.get(i).setComment_say_num(comments.get(i).getComment_say_num()-1);
        adapter.notifyItemChanged(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
