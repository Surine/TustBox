package com.surine.tustbox.UI.View.Header;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.surine.tustbox.Helper.Utils.AppUtil;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.ToastUtil;

/**
 * Created by Surine on 2019/1/28.
 * 首页分享部分
 */

public class HeaderShare {
    public static View getInstance(final Context context, RecyclerView recyclerView) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_share, (ViewGroup) recyclerView.getParent(), false);

        ImageView qq = view.findViewById(R.id.qq);
        ImageView weChat = view.findViewById(R.id.weChat);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.getShare(context);
            }
        });
        weChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.getShare(context);
            }
        });
        return view;
    }
}
