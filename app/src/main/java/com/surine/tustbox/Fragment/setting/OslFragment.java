package com.surine.tustbox.Fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.OslAdapter;
import com.surine.tustbox.Bean.Osl_info;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/17.
 */

public class OslFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Osl_info> mOsl_infos = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_osl, container, false);
        initData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.osl_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new OslAdapter(getActivity(),mOsl_infos));
        return view;
    }

    private void initData() {
        Osl_info oi = new Osl_info("OKhttp3","http://square.github.io/okhttp");
        mOsl_infos.add(oi);
        oi = new Osl_info("Eventbus","https://github.com/greenrobot/EventBus/");
        mOsl_infos.add(oi);
        oi = new Osl_info("Jsoup","https://jsoup.org/");
        mOsl_infos.add(oi);
        oi = new Osl_info("Litepal","https://github.com/LitePalFramework/LitePal#latest-downloads/");
        mOsl_infos.add(oi);
        oi = new Osl_info("Circleimageview","https://github.com/hdodenhof/CircleImageView");
        mOsl_infos.add(oi);
        oi = new Osl_info("Glide","https://github.com/bumptech/glide");
        mOsl_infos.add(oi);
        oi = new Osl_info("ScratchView","https://github.com/sharish/ScratchView");
        mOsl_infos.add(oi);
        oi = new Osl_info("Takephoto","https://github.com/crazycodeboy/TakePhoto");
        mOsl_infos.add(oi);
        oi = new Osl_info("Gson","https://github.com/google/gson");
        mOsl_infos.add(oi);
        oi = new Osl_info("BaseRecyclerViewAdapterHelper","https://github.com/CymChad/BaseRecyclerViewAdapterHelper");
        mOsl_infos.add(oi);
        oi = new Osl_info("Butterknife","http://jakewharton.github.io/butterknife/");
        mOsl_infos.add(oi);
        oi = new Osl_info("Ninegridview","http://github.com/jeasonlzy/NineGridView");
        mOsl_infos.add(oi);
    }
}
