package com.surine.tustbox.Fragment.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surine.tustbox.Adapter.Recycleview.Score_Adapter;
import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/8.
 */

public class This_School_Term_Score_Fragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<Score_Info> mScore_infos = new ArrayList<>();
    private List<Score_Info> mLastScore_infos = new ArrayList<>();
    private static final String ARG_ ="This_School_Term_Score_Fragment" ;
    View v;
    public static This_School_Term_Score_Fragment getInstance(String title) {
        This_School_Term_Score_Fragment fra = new This_School_Term_Score_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_viewpager_and_list, container, false);
        //初始化数据
        initData();

        return v;

    }

    private void initData() {
        mScore_infos = DataSupport.findAll(Score_Info.class);
        for(Score_Info score:mScore_infos){
            if(score.getType().equals("THIS")){
                mLastScore_infos.add(score);
            }
        }
        if(mLastScore_infos.size()!=0){
            initView();   //初始化view
        }else{
            Toast.makeText(getActivity(),"本地没有数据哦！",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.score_rec_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new Score_Adapter(mLastScore_infos,getActivity()));
    }

}
