package com.surine.tustbox.UI.Fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.SettingAdapter;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Pojo.SettingItem;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Surine on 2019/1/31.
 */

public class ProSettingFragment extends Fragment {
    private static ProSettingFragment ourInstance = new ProSettingFragment();
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;

    public static ProSettingFragment getInstance() {
        return ourInstance;
    }

    private SettingAdapter settingAdapter;
    private List<SettingItem> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_setting, container, false);

        data = SettingData.getProData(getActivity());
        unbinder = ButterKnife.bind(this, view);

        settingAdapter = new SettingAdapter(R.layout.item_setting, data);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(settingAdapter);

        settingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.show("敬请期待");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
