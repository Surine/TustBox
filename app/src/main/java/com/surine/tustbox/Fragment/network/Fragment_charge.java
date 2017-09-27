package com.surine.tustbox.Fragment.network;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;


/**
 * Created by surine on 17-7-7.
 */
public class Fragment_charge extends Fragment {
    private static final String ARG_ ="Fragment_charge" ;
    View v;
    private WebView charge;
    public static Fragment_charge getInstance(String title) {
        Fragment_charge fra = new Fragment_charge();
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
        v = inflater.inflate(R.layout.fragment_charge, container, false);
        Button charge = (Button) v.findViewById(R.id.button5);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri= Uri.parse(UrlData.charge_page);   //指定网址
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);           //指定Action
                intent.setData(uri);                            //设置Uri
                getActivity().startActivity(intent);
            }
        });
        return v;
    }

}
