package com.surine.tustbox.Fragment.main;

/**
 * Created by surine on 2017/9/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.R;

/**
 * Created by surine on 2017/6/3.
 */

public class SchoolFragment extends Fragment{
    public static final String ARG = "Fragment";
    View v;
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
        initView();
        return v;
    }

    private void initView() {
    }
}