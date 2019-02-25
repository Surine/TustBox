package com.surine.tustbox.UI.View.Header;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.R;

/**
 * Created by Surine on 2019/1/28.
 */

public class HeaderBtmLine {
    public static View getInstance(final Context context, RecyclerView recyclerView) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_btm_line, (ViewGroup) recyclerView.getParent(), false);
        return view;
        }
    }
