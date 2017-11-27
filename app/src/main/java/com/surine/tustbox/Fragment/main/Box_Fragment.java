package com.surine.tustbox.Fragment.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Box_new_adapter;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Box_info_Activty;
import com.surine.tustbox.UI.NetWorkActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/3/29.
 */

public class Box_Fragment extends Fragment {
    private static final String ARG_ = "Box_Fragment";
    RecyclerView box_rec;
    @BindView(R.id.box_rec)
    RecyclerView mBoxRec;
    @BindView(R.id.title1)
    TextView mTitle1;
    @BindView(R.id.title2)
    TextView mTitle2;
    @BindView(R.id.textView22)
    TextView mTextView22;
    @BindView(R.id.button6)
    Button mButton6;
    @BindView(R.id.button7)
    Button mButton7;
    @BindView(R.id.title3)
    TextView mTitle3;
    @BindView(R.id.title4)
    TextView mTitle4;
    @BindView(R.id.button9)
    Button mButton9;
    @BindView(R.id.text_bus_s_e)
    TextView mTextBusSE;
    @BindView(R.id.text_bus_s_e_2)
    TextView mTextBusSE2;
    @BindView(R.id.bus_relative)
    LinearLayout mBusRelative;
    @BindView(R.id.button8)
    Button mButton8;
    @BindView(R.id.more_to_say)
    TextView mMoreToSay;
    Unbinder unbinder;
    private List<Box> mboxs = new ArrayList<>();
    private Box_new_adapter adapter;

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
        box_rec.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new Box_new_adapter(R.layout.item_box, mboxs);
        box_rec.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(mboxs.get(position).getBox_name().equals("网络")){
                    startActivity(new Intent(getActivity(), NetWorkActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), Box_info_Activty.class).putExtra("item_box", mboxs.get(position).getBox_name()));
                }
            }
        });
        unbinder = ButterKnife.bind(this, v);


        return v;
    }


    private void initData() {
        Box box = new Box(R.drawable.ic_action_score, "成绩", getData(4), R.color.Tust_Green);
        Box box4 = new Box(R.drawable.ic_action_network, "网络", "暂时查询不到\n校园网信息", R.color.colorPrimary);
        Box box3 = new Box(R.drawable.ic_action_library, "图书", "暂时查询不到\n图书馆信息", R.color.Tust_Red);
        Box box5 = new Box(R.drawable.ic_action_gp, "GP", "暂时查询不到信息", R.color.Tust_more_color_1);
        mboxs.add(box);
        mboxs.add(box3);
        mboxs.add(box5);
        mboxs.add(box4);
    }

    private String getData(int i) {
        SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String info = null;
        switch (i) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                info = pref.getString("learn_info", getString(R.string.no_more_info));
                break;
            default:
                break;
        }
        return info;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.button6, R.id.button7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button6:
                Toast.makeText(getActivity(),"本功能将在考试前开放！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button7:
                Toast.makeText(getActivity(),"本功能将在考试前开放！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
