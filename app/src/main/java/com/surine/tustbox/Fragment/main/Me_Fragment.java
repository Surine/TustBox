package com.surine.tustbox.Fragment.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Adapter.Recycleview.StuInfoAdapter;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/3/28.
 */

public class Me_Fragment extends Fragment {
    private List<Student_info> mstudent_infos = new ArrayList<>();
    private static final String ARG_ = "Me_Fragment";
    private RecyclerView student_res;
    private ImageView head;
    private TextView mTextView2;
    private TextView mTextView3;
    public static Me_Fragment getInstance(String title) {
        Me_Fragment fra = new Me_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);
        //初始化数据
        initData();
        head  = (ImageView) v.findViewById(R.id.head);
        mTextView2 = (TextView) v.findViewById(R.id.textView2);
        mTextView3 = (TextView) v.findViewById(R.id.textView3);
        loadHead();
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        mTextView2.setText(pref.getString("stu_name","未知用户"));
        mTextView3.setText(pref.getString("part","未知学院"));
        student_res = (RecyclerView) v.findViewById(R.id.student_info);
        student_res.setLayoutManager(new LinearLayoutManager(getActivity()));
        student_res.setAdapter(new StuInfoAdapter(mstudent_infos));
        return v;
    }

    private void loadHead() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        boolean value = prefs.getBoolean("setting_close_show_picture", false);
        if(value) {
            Glide.with(getActivity()).load(R.drawable.head).into(head);
        }else{
            Glide.with(getActivity()).load(String.valueOf(getActivity().getFilesDir()+"/head.jpg")).into(head);
        }
    }

    private void initData() {
        mstudent_infos = DataSupport.findAll(Student_info.class);
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==1){
            loadHead();
        }
    }
}
