package com.surine.tustbox.Fragment.PageFragment;

/**
 * Created by surine on 2017/9/16.
 */

import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Action_List_Adapter;
import com.surine.tustbox.Adapter.Recycleview.SubjectHeaderAdapter;
import com.surine.tustbox.Adapter.Recycleview.TagListAdapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Bean.ActionInfo;
import com.surine.tustbox.Bean.Subject;
import com.surine.tustbox.Bean.TagInfo;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ActionInfoActivity;
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

/**
 * Created by surine on 2017/6/3.
 */

public class SchoolPageFragment extends Fragment {
    public static final String ARG = "Fragment";
    public static final boolean UPTOREFRESH = true;  //上拉标识
    public static final boolean DOWNTOREFRESH = false;  //下拉标识
    View v;
    @BindView(R.id.res_list)
    RecyclerView resList;
    Unbinder unbinder;

    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private Action_List_Adapter adapter;
    List<ActionInfo> data = new ArrayList<>();
    List<TagInfo> tags = new ArrayList<>();
    List<Action> actions = new ArrayList<>();
    List<Action> actionsFromServer = new ArrayList<>();
    private FloatingActionButton fab;
    private String picIdsInAction = null;
    private int pageNum = 1;
    private String[] items;
    private int positionInActions = 0;
    private String tokenFromServer;
    private View noView;
    private View headView;
    private List<Subject> subData = new ArrayList<>();
    private SubjectHeaderAdapter subjectHeaderAdapter;

    public static SchoolPageFragment getInstance(String title) {
        SchoolPageFragment fragment = new SchoolPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_school_page, container, false);
        unbinder = ButterKnife.bind(this, v);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              initServerData(DOWNTOREFRESH,1);
            }
        });

       // initTagData();

       // initTagList();

        //设置动态列表及适配器
        adapter = new Action_List_Adapter(R.layout.item_action, actions);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        resList.setLayoutManager(new LinearLayoutManager(getActivity()));


        resList.setAdapter(adapter);

        adapter.setEmptyView(R.layout.view_empty_2,resList);

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
                if(view == adapter.getViewByPosition(resList,position,R.id.love)){
                    loveThisAction(actions.get(position).getId(),position);
                }else if(view == adapter.getViewByPosition(resList,position,R.id.more)){
                    //展示更多对话框
                    positionInActions = position;
                    showMoreDialog();
                }else if(view == adapter.getViewByPosition(resList,position,R.id.action_info_head)){
                    Intent intent = new Intent(getActivity(),UserInfoActivity.class).putExtra(FormData.uid,actions.get(position).getUid());
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,"head").toBundle());
                }
            }
        });


//        RecyclerView subjectList = getActivity().findViewById(R.id.recycleviewSubject);
//        subjectHeaderAdapter = new SubjectHeaderAdapter(R.layout.item_subject_head,subData);
//        subjectList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
//        subjectList.setAdapter(subjectHeaderAdapter);

        //adapter.addHeaderView(view);

        //初始化加载最新数据
        initServerData(DOWNTOREFRESH,1);
        return v;
    }


    public void loadSubjectData(){
        HttpUtil.get(UrlData.getSubjectList).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String x = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if(jsonObject.getInt(FormData.JCODE) == 2000){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //获取主题数据
                                String s = null;
                                try {
                                    s = new JSONObject(x).getString(FormData.JDATA);
                                } catch (JSONException e) {
                                    return;
                                }
                                List<Subject> subjects = GsonUtil.parseJsonWithGsonToList(s, Subject.class);
                                for (Subject su : subjects) {
                                    subData.add(su);
                                    subjectHeaderAdapter.notifyDataSetChanged();
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

    private void showMoreDialog() {
        final String items_with_permission[] = {"复制文本", "删除动态"};
        final String items_no_permission[] = {"复制文本"};
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        String type = SharedPreferencesUtil.Read_safe(getActivity(), FormData.USER_TYPE, "0");
        if(actions.get(positionInActions).getUid().equals(tust_number)||type.equals("1")){
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
                    cmb.setText(actions.get(positionInActions).getMessages_info());
                    Toast.makeText(getActivity(), R.string.ReplyInCommentActivityClipSuccess, Toast.LENGTH_SHORT).show();
                }else if(which == 1){
                    picIdsInAction = actions.get(positionInActions).getPic_ids();
                    deleteAction();
                }
            }
        });
        builder.create().show();
    }

    private void deleteAction() {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(), FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        HttpUtil.get(UrlData.deleteAction+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+tust_number+"&"+FormData.id+"="+actions.get(positionInActions).getId()).enqueue(new Callback() {
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
                                updateList(positionInActions);
                                positionInActions = 0;
                                //TODO：删除七牛云图片
                                if(!picIdsInAction.equals("")||picIdsInAction == null){
                                    deleteQiniuPic();
                                }
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

    private void deleteQiniuPic() {
        Log.d("TAG","删除七牛");
        HttpUtil.get(UrlData.getToken).enqueue(new Callback() {
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
                    if(getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                tokenFromServer = jsonObject.getString("token");
                                if(tokenFromServer!=null){
                                    
                                }else{
                                    Toast.makeText(getActivity(), R.string.SendActionActivityQiniuTokenError, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
    }

    private void updateList(int positionInActions) {
        actions.remove(positionInActions);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(positionInActions);
    }

    //点赞
    private void loveThisAction(int id, final int postion) {
        Log.d("TAG","点赞");
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(),FormData.token,"");
        String tust_number = SharedPreferencesUtil.Read(getActivity(),FormData.tust_number_server,"");
        String buildUrl = UrlData.loveAction+"?"+FormData.token+"="+token+"&"+FormData.did+"="+
                id+"&"+FormData.uid+"="+tust_number;
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
                String x = response.body().string();
                Log.d("TAG",x);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //点赞成功
                               actions.get(postion).setIslove(true);
                                actions.get(postion).setMessages_agreenum(String.valueOf(Integer.parseInt(actions.get(postion).getMessages_agreenum()) + 1));
                                adapter.notifyItemChanged(postion);
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 401){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            Toast.makeText(getActivity(), R.string.null_input, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 500){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //取消赞
                                actions.get(postion).setIslove(false);
                                actions.get(postion).setMessages_agreenum(String.valueOf(Integer.parseInt(actions.get(postion).getMessages_agreenum()) - 1));
                                adapter.notifyItemChanged(postion);
                            }
                        });
                    }else{
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //其他错误
                                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
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

        String token = SharedPreferencesUtil.Read_safe(getActivity(),FormData.token,"");
        String tust_number = SharedPreferencesUtil.Read(getActivity(),FormData.tust_number_server,"");
        String buildUrl = UrlData.getAction+"?"+FormData.uid+"="+tust_number+"&"+FormData.page+"="+page;
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
                        srl.setRefreshing(false);
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
                    if (jsonObject.getInt("jcode") == 400) {
                        String s1 = jsonObject.getString("jdata");
                        actionsFromServer.clear();
                        actionsFromServer = GsonUtil.parseJsonWithGsonToList(s1, Action.class);
                        //Log.d("TAG","本次加载从服务器返回"+actionsFromServer.size()+"条数据");
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //请求到数据停止刷新
                              srl.setRefreshing(false);
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
                                    Toast.makeText(getActivity(), R.string.ThirdPageFragmentNullContent, Toast.LENGTH_SHORT).show();
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
        resList.smoothScrollToPosition(0);
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



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo){
       if(simpleBusInfo.getId() == 1){
           //tag == 1为更新，tag == 0 为初始加载
           Log.d("TAG","收到信息");
           srl.post(new Runnable() {
               @Override
               public void run() {
                   srl.setRefreshing(true);
               }
           });
           initServerData(DOWNTOREFRESH,1);
       }else if(simpleBusInfo.getId() == 4){
           //刷新
           initServerData(DOWNTOREFRESH,1);
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}