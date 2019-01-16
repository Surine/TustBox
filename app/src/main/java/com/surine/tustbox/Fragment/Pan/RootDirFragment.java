package com.surine.tustbox.Fragment.Pan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.RootDirAdapter;
import com.surine.tustbox.Bean.EventBusBean.TustPanBus;
import com.surine.tustbox.Bean.RootDir;
import com.surine.tustbox.Data.FunctionTag;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Surine on 2018/12/31.
 */

public class RootDirFragment extends Fragment {

    private static final String ARG = "RootDirFragment";
    @BindView(R.id.group)
    RecyclerView group;
    private View v;
    private List<RootDir> list = new ArrayList<>();
    private RootDirAdapter groupAdapter;

    public static RootDirFragment getInstance(String title) {
        RootDirFragment fragment = new RootDirFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String s = (String) bundle.get(ARG);
        LogUtil.d(s);
        try {
            list = GsonUtil.parseJsonWithGsonToList((String)bundle.get(ARG),RootDir.class);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "该文件/目录无法访问", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_folder, container, false);
        ButterKnife.bind(this, v);
        group.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupAdapter = new RootDirAdapter(R.layout.item_root_dir,list);
        group.setAdapter(groupAdapter);
        groupAdapter.setEmptyView(R.layout.view_empty_2,group);

        groupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new TustPanBus(FunctionTag.PAN_CODE,list.get(position).getRoot_id(),"/"));
            }
        });

        return v;
    }


}
