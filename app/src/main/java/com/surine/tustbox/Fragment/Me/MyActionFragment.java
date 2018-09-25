package com.surine.tustbox.Fragment.Me;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.ActionInMyPageAdapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ActionInfoActivity;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.DOWNTOREFRESH;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.UPTOREFRESH;

/**
 * Created by Surine on 2018/2/28.
 */

public class MyActionFragment extends Fragment {

    private static final String ARG_ = "MyActionFragment";
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private List<Action> actions = new ArrayList<>();
    private ActionInMyPageAdapter adapter;
    private int positionInActions = 1;
    private int pageNum = 1;
    private List<Action> actionsFromServer = new ArrayList<>();
    private String uid;
    private FragmentActivity mContext;

    public static MyActionFragment getInstance(String title) {
        MyActionFragment fra = new MyActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        uid = bundle.getString(ARG_);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycleview, container, false);
        unbinder = ButterKnife.bind(this, v);

        adapter = new ActionInMyPageAdapter(R.layout.item_my_action, actions);
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
                startActivity(new Intent(getActivity(), ActionInfoActivity.class).putExtra(FormData.did,actions.get(position).getId()));
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recycleview,position,R.id.more)){
                    //展示更多对话框
                    positionInActions = position;
                    showMoreDialog();
                }
            }
        });


        //初始化加载最新数据
        initServerData(DOWNTOREFRESH,1);
        return v;
    }

    private void showMoreDialog() {
        final String items_with_permission[] = {"复制文本", "删除动态"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items_with_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(which == 0){
                    //复制内容
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    cmb.setText(actions.get(positionInActions).getMessages_info());
                    Toast.makeText(getActivity(), R.string.ReplyInCommentActivityClipSuccess, Toast.LENGTH_SHORT).show();
                }else if(which == 1){
                    try {
                        deleteAction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void deleteAction() throws Exception{
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(), FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        HttpUtil.get(UrlData.deleteAction+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+tust_number+"&"+FormData.id+"="+actions.get(positionInActions).getId()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //删除成功
                                //通知自身列表更新
                                updateList(positionInActions);
                                positionInActions = 0;
                            }
                        });

                    }else{
                        mContext.runOnUiThread(new Runnable() {
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

    private void updateList(int positionInActions) {
        actions.remove(positionInActions);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(positionInActions);
    }


    //加载更多数据
    private void loadMore() {
        //只有在加载更多数据的时候，参数为true
        pageNum = pageNum +1;
        initServerData(UPTOREFRESH,pageNum);
    }

    private void initServerData(final boolean b, int page) {

        String token = SharedPreferencesUtil.Read_safe(getActivity(),FormData.token,"");
        String tust_number = uid;
        String buildUrl = UrlData.getActionByUid+"?"+FormData.uid+"="+tust_number+"&"+FormData.page+"="+page;
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
                    if (jsonObject.getInt("jcode") == 400) {
                        String s1 = jsonObject.getString("jdata");
                        actionsFromServer.clear();
                        actionsFromServer = GsonUtil.parseJsonWithGsonToList(s1, Action.class);
                        if(mContext == null){
                            return;
                        }
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //请求到数据停止刷新
                                if(b){
                                    updataMoreData();
                                }else{
                                    updataData();
                                }
                            }
                        });

                    } else {
                        if(mContext == null){
                            return;
                        }
                        mContext.runOnUiThread(new Runnable() {
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

    //上拉刷新
    private void updataMoreData() {
        for(Action action : actionsFromServer){
            actions.add(action);
        }
        actionsFromServer.clear();
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
    }

    //下拉刷新
    private void updataData() {
        actions.clear();
        for(Action action : actionsFromServer){
            actions.add(action);
        }
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        if(recycleview != null){
            recycleview.smoothScrollToPosition(0);
        }
        //重新加载其他页
        pageNum = 1;
        //TODO:下拉加载后重新启动上拉加载
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
