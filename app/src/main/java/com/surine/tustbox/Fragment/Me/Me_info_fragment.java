package com.surine.tustbox.Fragment.Me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.StuInfoAdapter;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/5/12.
 */

public class Me_info_fragment extends Fragment{
    private List<Student_info> mstudent_infos = new ArrayList<>();
    private RecyclerView student_res;
    private static final String ARG_ = "Me_info_fragment";

    public static Me_info_fragment getInstance(String title) {
        Me_info_fragment fra = new Me_info_fragment();
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
        View v = inflater.inflate(R.layout.fragment_me_info, container, false);

        mstudent_infos = DataSupport.findAll(Student_info.class);
        student_res = (RecyclerView) v.findViewById(R.id.student_info);
        student_res.setLayoutManager(new LinearLayoutManager(getActivity()));
        student_res.setAdapter(new StuInfoAdapter(mstudent_infos));

        return v;
    }

}
