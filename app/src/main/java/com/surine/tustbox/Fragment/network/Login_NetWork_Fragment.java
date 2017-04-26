package com.surine.tustbox.Fragment.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.R;

/**
 * Created by surine on 2017/4/6.
 */

public class Login_NetWork_Fragment extends Fragment{

    private static final String ARG_ ="Login_NetWork_Fragment" ;

    public static Login_NetWork_Fragment getInstance(String title) {
        Login_NetWork_Fragment fra = new Login_NetWork_Fragment();
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
        View v = inflater.inflate(R.layout.fragment_login_network, container, false);
        initView(v);   //初始化view
        //初始化数据
        initData();
        return v;

    }

    private void initData() {
    }

    private void initView(View v) {
    }
}
