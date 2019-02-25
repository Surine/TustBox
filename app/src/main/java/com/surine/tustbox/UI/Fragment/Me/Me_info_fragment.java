package com.surine.tustbox.UI.Fragment.Me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.StuInfoAdapter;
import com.surine.tustbox.Pojo.Student_info;
import com.surine.tustbox.Pojo.User;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by surine on 2017/5/12.
 */

public class Me_info_fragment extends Fragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private List<Student_info> mstudent_infos = new ArrayList<>();

    private static final String ARG_ = "Me_info_fragment";
    private User user;

    public static Me_info_fragment getInstance(User user) {
        Me_info_fragment fra = new Me_info_fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_, user);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        user = (User) bundle.get(ARG_);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycleview, container, false);
        unbinder = ButterKnife.bind(this, v);

        Student_info student_info = new Student_info("学院：",user.getCollege());
        mstudent_infos.add(student_info);
        student_info = new Student_info("性别：",user.getSex());
        mstudent_infos.add(student_info);
        student_info = new Student_info("创建时间：",user.getCrtime());
        mstudent_infos.add(student_info);
        if(user.getTust_number() != null)
        student_info = new Student_info("年级：","20"+user.getTust_number().substring(0,2));
        mstudent_infos.add(student_info);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(new StuInfoAdapter(mstudent_infos));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
