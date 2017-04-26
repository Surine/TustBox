package com.surine.tustbox.Fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.R;

/**
 * Created by surine on 2017/4/17.
 */

public class AboutFragment extends Fragment{
    FloatingActionButton mFloatingActionButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about_app, container, false);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        return view;
    }

    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "欢迎使用小天盒子：surine.cn/download/app-release.apk");
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表  
        startActivity(Intent.createChooser(intent, "小天盒子分享"));
    }
}
