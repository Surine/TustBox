package com.surine.tustbox.UI.Fragment.message;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiniu.android.utils.Json;
import com.surine.tustbox.Adapter.Recycleview.NoticeAdapter;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Pojo.Notice;
import com.surine.tustbox.R;
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

import static com.surine.tustbox.App.Data.JCode.ERROR;
import static com.surine.tustbox.App.Data.JCode.J200;
import static com.surine.tustbox.Helper.Utils.JsonUtil.getPojoList;

/**
 * Created by Surine on 2019/2/20.
 */

public class AppMessageFragment extends BaseFragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;


    private NoticeAdapter noticeAdapter;
    private List<Notice> list = new ArrayList<>();
    private List<Notice> noticesFromServer = new ArrayList<>();

    @Override
    public void initData(View view) {
        unbinder = ButterKnife.bind(this, view);
        //加载视图
        recycleview.setLayoutManager(new LinearLayoutManager(mActivity));
        noticeAdapter = new NoticeAdapter(R.layout.item_notice,list);
        recycleview.setAdapter(noticeAdapter);
        //加载数据
        initData();
    }

    private void initData() {

        HttpUtil.get(UrlData.getNoticeFromServer).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RunOnUiThread.updateUi(mActivity, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        ToastUtil.netError();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String serverData = response.body().string();
                RunOnUiThread.updateUi(mActivity, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        int status = JsonUtil.getStatus(serverData);
                        try {
                            switch (status){
                                case J200:
                                    noticesFromServer.clear();
                                    noticesFromServer = getPojoList(serverData, Notice.class);
                                    loadData();
                                    break;
                                case ERROR:
                                    ToastUtil.getError();
                                    break;
                            }
                        } catch (JSONException e) {
                            ToastUtil.jsonError();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void loadData() {
        for (Notice notice:noticesFromServer) {
            list.add(notice);
        }
        noticeAdapter.notifyDataSetChanged();
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
