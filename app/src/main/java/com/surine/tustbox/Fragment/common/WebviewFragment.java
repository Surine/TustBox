package com.surine.tustbox.Fragment.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;

/**
 * Created by surine on 2017/9/24.
 */

public class WebviewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_webview, container, false);
        WebView webView = (WebView) view.findViewById(R.id.web_view);
        //WebView :load url at assets
        webView.loadUrl(UrlData.notice_url);
        return view;
    }
}