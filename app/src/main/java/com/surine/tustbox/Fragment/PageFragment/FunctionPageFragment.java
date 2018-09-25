package com.surine.tustbox.Fragment.PageFragment;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Box_new_adapter;
import com.surine.tustbox.Adapter.Recycleview.SchoolUrlAdapter;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.Bean.SchoolUrl;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.EmptyClassRoomActivity;
import com.surine.tustbox.UI.NetWorkActivity;
import com.surine.tustbox.UI.ScoreActiviy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FunctionPageFragment extends Fragment {
    public static final String ARG = "Fragment";

    @BindView(R.id.box_rec)
    RecyclerView boxRec;

    View v;

    private RecyclerView recyclerView;

    private List<Box> mboxs = new ArrayList<>();
    private List<SchoolUrl> mSchoolUrls = new ArrayList<>();
    private SchoolUrlAdapter adapterSchoolUrl;
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

        initBlockUrl();
        return v;
    }

    private void initBlockUrl() {
        initUrlView();
    }

    private void initUrlView() {
        initUrlData();
        recyclerView = v.findViewById(R.id.urlRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterSchoolUrl = new SchoolUrlAdapter(R.layout.item_school_url, mSchoolUrls);
        recyclerView.setAdapter(adapterSchoolUrl);
       adapterSchoolUrl.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
           @Override
           public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mSchoolUrls.get(position).getUrlText())));
           }
       });
    }

    private void initUrlData() {
        SchoolUrl schoolUrl = new SchoolUrl();
        schoolUrl.setUrlName("校园网充值");
        schoolUrl.setUrlIntro("这里可以为您的校园网充值");
        schoolUrl.setUrlText(UrlData.charge_page);
        mSchoolUrls.add(schoolUrl);

        SchoolUrl tv_ejia = new SchoolUrl();
        tv_ejia.setUrlName("易佳TV");
        tv_ejia.setUrlIntro("这里可以免流量看电视，记得连接校园网但不要登帐号哦！");
        tv_ejia.setUrlText(UrlData.ejiaTv);
        mSchoolUrls.add(tv_ejia);

        SchoolUrl myCospxk = new SchoolUrl();
        myCospxk.setUrlName("评教系统");
        myCospxk.setUrlIntro("这里可以评教，密码的话，老师应该说过，XiaXiaXia~");
        myCospxk.setUrlText(UrlData.myCos);
        mSchoolUrls.add(myCospxk);

        SchoolUrl pan = new SchoolUrl();
        pan.setUrlName("科大云盘");
        pan.setUrlIntro("赶紧去看你私藏的 (Xiao) 电影~");
        pan.setUrlText(UrlData.pan);
        mSchoolUrls.add(pan);

        SchoolUrl help = new SchoolUrl();
        help.setUrlName("Help小站");
        help.setUrlIntro("学校电话，校历，校班车……");
        help.setUrlText(UrlData.help);
        mSchoolUrls.add(help);
    }


    private void initBlockTools() {
        //初始化数据
        initToolData();
        //初始化视图
        initToolView();
    }

    private void initToolData() {
        //成绩
        Box score = new Box(R.drawable.ic_action_score, "成绩", null, R.color.Tust_Green);
        //网络
        Box net = new Box(R.drawable.ic_action_network, "网络", null, R.color.colorPrimary);
        //空教室
        Box emptyClassRoom = new Box(R.drawable.ic_action_empty_classroom, "空教室", null, R.color.Tust_more_color_1);
        mboxs.add(score);
        mboxs.add(net);
        mboxs.add(emptyClassRoom);
    }


    private void initToolView() {
        boxRec.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        adapter = new Box_new_adapter(R.layout.item_box, mboxs);
        boxRec.setAdapter(adapter);
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
                }


            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
