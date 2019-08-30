package com.surine.tustbox.UI.Fragment.PageFragment;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Box_new_adapter;
import com.surine.tustbox.Helper.Utils.AppUtil;
import com.surine.tustbox.Pojo.Box;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.EmptyClassRoomActivity;
import com.surine.tustbox.UI.Activity.NetWorkActivity;
import com.surine.tustbox.UI.Activity.PanLoginActivity;
import com.surine.tustbox.UI.Activity.ScoreActiviy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FunctionPageFragment extends Fragment {
    public static final String ARG = "Fragment";
    View v;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private List<Box> mboxs = new ArrayList<>();
    private Box_new_adapter adapter;


    public static FunctionPageFragment getInstance(String title) {
        FunctionPageFragment fragment = new FunctionPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        // EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_function_page, container, false);
        ButterKnife.bind(this, v);
        initBlockTools();
        return v;
    }

    private void initBlockTools() {
        //初始化数据
        initToolData();
        //初始化视图
        initToolView();
    }

    private void initToolData() {
        //成绩
        Box score = new Box(R.drawable.star_v5, "成绩", null, R.color.Tust_Green);
        //网络
        Box net = new Box(R.drawable.card_v5, "网络", null, R.color.colorPrimaryB);
        //空教室
        Box emptyClassRoom = new Box(R.drawable.home_v5, "空教室", null, R.color.Tust_more_color_1);
        Box pan = new Box(R.drawable.video_v5, "云盘(Beta)", null, R.color.Tust_more_color_1);

        Box hey = new Box(R.drawable.heyu,"HeyU",null,R.color.trans_white);
        mboxs.add(score);
        mboxs.add(net);
        mboxs.add(emptyClassRoom);
        mboxs.add(pan);
        mboxs.add(hey);
    }


    private void initToolView() {
        recycleview.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new Box_new_adapter(R.layout.item_box, mboxs);
        recycleview.setAdapter(adapter);
        adapter.addHeaderView(getTopbarView());
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        //成绩页面
                        intent.setClass(getActivity(), ScoreActiviy.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //网络页面
                        intent.setClass(getActivity(), NetWorkActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //空教室页面
                        intent.setClass(getActivity(), EmptyClassRoomActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(getActivity(), PanLoginActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        AppUtil.launchMiniProgramByWxAppId(getActivity(),"wx5f22b78c34909007","gh_7153347c2d45","/pages/index/index",0);
                        break;
                }


            }
        });
    }

    private View getTopbarView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.vg_box_topbar,(ViewGroup) recycleview.getParent(),false);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
