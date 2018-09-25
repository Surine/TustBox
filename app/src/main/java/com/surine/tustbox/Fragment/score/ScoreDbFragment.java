package com.surine.tustbox.Fragment.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.surine.tustbox.Adapter.Recycleview.ScoreBaseAdapter;
import com.surine.tustbox.Bean.ScoreInfo;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.Bean.ScoreInfoHelper;
import com.surine.tustbox.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by surine on 2017/4/22.
 */

public class ScoreDbFragment extends Fragment {
    @BindView(R.id.listScore)
    RecyclerView listScore;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.loading_text)
    Button loadingText;
    Unbinder unbinder;
    private RecyclerView mRecyclerView;
    private List<ScoreInfoHelper> mScore_infos = new ArrayList<>();
    private List<ScoreInfoHelper> mLastScore_infos = new ArrayList<>();
    private static final String ARG_ = "ScoreDbFragment";
    View v;
    private List<ScoreInfoHelper> scores = new ArrayList<>();
    private ScoreBaseAdapter adapter;
    private DoubleBounce doubleBounce;
    private List<ScoreInfoHelper> mScoreFromDB;

    public static ScoreDbFragment getInstance(String title) {
        ScoreDbFragment fra = new ScoreDbFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_now_term, container, false);
        unbinder = ButterKnife.bind(this, v);
        adapter = new ScoreBaseAdapter(R.layout.item_score, scores);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        listScore.setLayoutManager(new LinearLayoutManager(getActivity()));
        listScore.setAdapter(adapter);

        //首先加载动画
        doubleBounce = new DoubleBounce();
        spinKit.setIndeterminateDrawable(doubleBounce);

        updateList();
        return v;

    }

    private void updateList() {
        scores.clear();
        mScoreFromDB = DataSupport.findAll(ScoreInfoHelper.class);
        if(mScoreFromDB.size() > 0){
            for(ScoreInfoHelper score:mScoreFromDB){
                scores.add(score);
            }
            adapter.notifyDataSetChanged();
            loadingText.setVisibility(View.GONE);
            spinKit.setVisibility(View.GONE);
        }else{
            loadingText.setVisibility(View.VISIBLE);
            spinKit.setVisibility(View.VISIBLE);
            loadingText.setText("本地无数据，正在等待网络数据加载……");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo) {
        if (simpleBusInfo.getId() == 12) {
           updateList();
        }
    }
}
