package com.surine.tustbox.Fragment.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.BoxAdapter;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/3/29.
 */

public class Box_Fragment extends Fragment {
    private static final String ARG_ = "Box_Fragment";
    RecyclerView box_rec;
    private List<Box> mboxs =new ArrayList<>();
    public static Box_Fragment getInstance(String title) {
        Box_Fragment fra = new Box_Fragment();
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
        View v = inflater.inflate(R.layout.fragment_box, container, false);
        initData();
        box_rec = (RecyclerView) v.findViewById(R.id.box_rec);
        box_rec.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        box_rec.setAdapter(new BoxAdapter(mboxs,getActivity()));
        return v;
    }


    private void initData() {
        Box box = new Box(R.drawable.ic_action_score,"成绩",getData(4),R.color.Tust_Green);
        Box box4 = new Box(R.drawable.ic_action_network,getString(R.string.school_network),"暂时查询不到\n校园网信息",R.color.colorAccent);
        Box box3 = new Box(R.drawable.ic_action_library,"图书馆","暂时查询不到\n图书馆信息",R.color.Tust_Red);
        Box box5 = new Box(R.drawable.ic_action_score,"GP下载平台","暂时查询不到信息",R.color.Tust_more_color_1);
        mboxs.add(box);
        mboxs.add(box3);
        mboxs.add(box5);
    }

    private String getData(int i) {
        SharedPreferences pref =getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String info = null;
       switch (i){
           case 1:break;
           case 2:break;
           case 3:break;
           case 4:info = pref.getString("learn_info",getString(R.string.no_more_info)); break;
           default:break;
       }
       return info;
    }


}
