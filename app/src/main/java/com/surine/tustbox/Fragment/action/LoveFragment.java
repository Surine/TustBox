package com.surine.tustbox.Fragment.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.LoveAdapter;
import com.surine.tustbox.Bean.Love;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.R;
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

import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.DOWNTOREFRESH;
import static com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment.UPTOREFRESH;

/**
 * Created by Surine on 2018/2/23.
 */

public class LoveFragment extends Fragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private LoveAdapter adapter;
    private List<Love> loves = new ArrayList<>();
    private String did = null;
    private int pageNum = 1;
    private List<Love> lovesFromServer = new ArrayList<>();

    public static LoveFragment getInstance(String title) {
        LoveFragment fra = new LoveFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FormData.did , title);
        fra.setArguments(bundle);
        return fra;
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

        adapter = new LoveAdapter(R.layout.item_love, loves);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(adapter);
        View noView = getActivity().getLayoutInflater().inflate(R.layout.view_empty_action, (ViewGroup) recycleview.getParent(), false);
        adapter.setEmptyView(noView);
        //初始化加载最新数据
        initServerData(DOWNTOREFRESH,1);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recycleview,position,R.id.item_love_head)){
                    //启动个人详情页
                    startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra(FormData.uid,loves.get(position).getUid()));
                }
            }
        });
        return v;
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
        String buildUrl = UrlData.getLove+"?"+ FormData.token+"="+token+"&"+FormData.page+"="+i+"&"+FormData.did+"="+did;
        Log.d("TAG",buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
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
                final String s = response.body().string();
                Log.d("TAG",s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        String s1 = jsonObject.getString(FormData.JDATA);
                        lovesFromServer.clear();
                        lovesFromServer = GsonUtil.parseJsonWithGsonToList(s1, Love.class);
                        //Log.d("TAG","本次加载从服务器返回"+lovesFromServer.size()+"条数据");
                        if(getActivity() == null){
                            return;
                        }
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
                        if(getActivity() == null){
                            return;
                        }
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
        loves.clear();
        for(Love love : lovesFromServer){
            loves.add(love);
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
        for(Love love : lovesFromServer){
            loves.add(love);
        }
        lovesFromServer.clear();
        //通知适配器更新数据
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo){
        if(simpleBusInfo.getId() == 5){
            Log.d("TAG","更新点赞列表");
            initServerData(DOWNTOREFRESH,1);
        }
        if(simpleBusInfo.getId() == 8){
            //取消赞（删除列表中某一项）
            for (int i = 0;i<loves.size();i++){
                if(loves.get(i).getUid().equals(simpleBusInfo.getMessage())){
                    loves.remove(i);
                    adapter.notifyDataSetChanged();
                    adapter.remove(i);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
