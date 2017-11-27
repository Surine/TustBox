package com.surine.tustbox.Fragment.main;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.Action_List_Adapter;
import com.surine.tustbox.Bean.ActionInfo;
import com.surine.tustbox.Bean.Image_Grid_Info;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.SendActionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/6/3.
 */

public class SchoolFragment extends Fragment{
    public static final String ARG = "Fragment";
    View v;
    private RecyclerView list_res;
    private Action_List_Adapter adapter;
    List<ActionInfo> data = new ArrayList<>();
    private FloatingActionButton fab;

    public static SchoolFragment getInstance(String title){
        SchoolFragment fragment = new SchoolFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_school,container,false);
        testdata();

        initView();
        return v;
    }

    private void testdata() {
           List<Image_Grid_Info> image = new ArrayList<>();
           Image_Grid_Info image_grid_info = new Image_Grid_Info("http://upload-images.jianshu.io/upload_images/972352-9911637db5512613.png?" +
                   "imageMogr2/auto-orient/strip%7CimageView2/2/w/1240", "400", "400");
           Image_Grid_Info image_grid_info2 = new Image_Grid_Info("http://p2.so.qhmsg.com/sdr/1151_768_/t01239f7b0935ad16f7.jpg", "400", "400");
           Image_Grid_Info image_grid_info3 = new Image_Grid_Info("http://cdnq.duitang.com/uploads/item/201503/09/20150309190558_4wAGE.thumb.700_0.jpeg", "400", "400");
           image.add(image_grid_info3);

           ActionInfo ai = new ActionInfo(1, "https://i.loli.net/2017/10/18/59e71d7296e0c.jpg",
                   "开发小哥哥", 2001, "16:32:54", "哇！其实很久之前就写了前端代码，可是后端一直处于待Code状态，" +
                   "也许完成这个功能之后，开发小哥哥就踏上了一条考研的不归路，且行且珍惜，寒假后见！", image, "10", "1", "");
           data.add(ai);

    }

    private void initView() {
        list_res = (RecyclerView) v.findViewById(R.id.res_list);
        fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        adapter = new Action_List_Adapter(R.layout.item_action,data);
        list_res.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_res.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendActionActivity.class));
            }
        });
    }
}