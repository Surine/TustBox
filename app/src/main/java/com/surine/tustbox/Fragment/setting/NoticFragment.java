package com.surine.tustbox.Fragment.setting;

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
 * Created by surine on 2017/4/17.
 */

public class NoticFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_gpa_calculate_file, container, false);
        getActivity().setTitle(getString(R.string.notice2));
        WebView webView = (WebView) view.findViewById(R.id.gpa_file_Webview);
        //WebView :load url at assets
        webView.loadUrl(UrlData.notice_url);
        return view;
    }
}
