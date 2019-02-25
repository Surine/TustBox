package com.surine.tustbox.UI.Fragment.PageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.surine.tustbox.Adapter.Recycleview.ActionListAdapter;
import com.surine.tustbox.Pojo.Action;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.V5_SettingActivity;
import com.surine.tustbox.UI.View.Header.HeaderBox;
import com.surine.tustbox.UI.View.Header.HeaderBtmLine;
import com.surine.tustbox.UI.View.Header.HeaderCourse;
import com.surine.tustbox.UI.View.Header.HeaderShare;
import com.surine.tustbox.UI.View.Header.HeaderTask;
import com.surine.tustbox.UI.View.Header.HeaderTopBar;
import com.surine.tustbox.UI.View.VgTopbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.surine.tustbox.App.Data.Constants.INDEXCARD;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Read;

/**
 * Created by Surine on 2019/1/25.
 * v5首页
 * 用于展示首页的，碎片，包含内容是信息卡流，暂时用的方式是RecyclerView的头尾布局
 */

public class FirstPageFragment extends Fragment {
    private static final String NAME = "FirstPageFragment";
    Unbinder unbinder;

    @BindView(R.id.msgRecyclerView)
    RecyclerView msgRecyclerView;
    @BindView(R.id.vgTopbar)
    VgTopbar vgTopbar;

    private ActionListAdapter adapter;   //辅助用adapter
    private List<Action> data = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    public static FirstPageFragment getInstance(String msg) {
        FirstPageFragment firstPageFragment = new FirstPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, msg);
        firstPageFragment.setArguments(bundle);
        return firstPageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.fragment_main_page_v5, container, false);
        unbinder = ButterKnife.bind(this, view);
        vgTopbar.setTitle("首页")
                .setLeftIconGone(true)
                .setRightIcon(R.drawable.ic_setting_black_24dp)
                .setListener(new VgTopbar.OnClickListener() {
                    @Override
                    public void leftButton() {

                    }

                    @Override
                    public void rightButton() {
                        Intent intent = new Intent(getActivity(), V5_SettingActivity.class);
                        startActivity(intent);
                    }
                });
        //配置列表
        linearLayoutManager = new LinearLayoutManager(getActivity());
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ActionListAdapter(R.layout.item_action, data);
        msgRecyclerView.setAdapter(adapter);

        //滑动监听
        msgRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //获取RecyclerView当前顶部显示的第一个条目对应的索引
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                //根据索引来获取对应的itemView
                View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
                //获取当前显示条目的高度
                int itemHeight = firstVisiableChildView.getHeight();
                //获取当前Recyclerview 偏移量
                int flag = (position) * itemHeight - firstVisiableChildView.getTop();

                if (flag >= 410) {
                    //显示topbar
                    if(vgTopbar.getVisibility() == View.GONE)
                        showBar();
                } else {
                    if(vgTopbar.getVisibility() == View.VISIBLE)
                        dontShowBar();
                }

            }
        });
        initCard();
        return view;
    }

    private void dontShowBar() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(600);
        vgTopbar.startAnimation(mHiddenAction);
        vgTopbar.setVisibility(View.GONE);
    }

    //显示顶部栏
    private void showBar() {
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(300);
        vgTopbar.startAnimation(mShowAction);
        mShowAction.setDuration(800);
        vgTopbar.setRightIconGone(true);
        vgTopbar.getView(3).startAnimation(mShowAction);
        vgTopbar.getView(3).setVisibility(View.VISIBLE);
        vgTopbar.setVisibility(View.VISIBLE);
    }

    //加载首页信息卡
    private void initCard() {
        adapter.addFooterView(HeaderTopBar.getInstance(getActivity(), msgRecyclerView));
        adapter.addFooterView(HeaderBox.getInstance(getActivity(), msgRecyclerView));

        if(Read(getActivity(),INDEXCARD+0,true)){
            adapter.addFooterView(HeaderCourse.getInstance(getActivity(), msgRecyclerView));
        }
        if(Read(getActivity(),INDEXCARD+1,true)){
            adapter.addFooterView(HeaderTask.getInstance(getActivity(), msgRecyclerView));
        }
        if(Read(getActivity(),INDEXCARD+2,true)){
            adapter.addFooterView(HeaderShare.getInstance(getActivity(), msgRecyclerView));
        }
        if(Read(getActivity(),INDEXCARD+3,true)){
            adapter.addFooterView(HeaderBtmLine.getInstance(getActivity(), msgRecyclerView));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
