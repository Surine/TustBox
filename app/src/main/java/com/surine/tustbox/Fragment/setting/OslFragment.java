package com.surine.tustbox.Fragment.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.surine.tustbox.R;

/**
 * Created by surine on 2017/4/17.
 */

public class OslFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_osl, container, false);
        WebView webView = (WebView) view.findViewById(R.id.gpa_file_Webview);
        //WebView :load url at assets
        webView.loadUrl("file:///android_asset/Html/osl.html");
        FloatingActionButton mfab;
        mfab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri= Uri.parse("https://github.com/surine");   //指定网址
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);           //指定Action
                intent.setData(uri);                            //设置Uri
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
