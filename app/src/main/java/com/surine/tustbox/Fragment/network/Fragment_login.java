package com.surine.tustbox.Fragment.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.EventBusBean.Net_EventBus;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import java.io.Serializable;


/**
 * Created by surine on 17-7-7.
 */
public class Fragment_login extends Fragment {
    private static final String ARG_ ="Fragment_login" ;
    private TextView use_time;
    private TextView use_data;
    private TextView use_fee;
    private Button clear;
    private Net_EventBus nes;
    View v;
    public static Fragment_login getInstance(Net_EventBus title) {
        Fragment_login fra = new Fragment_login();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_, (Serializable) title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        nes = (Net_EventBus) bundle.getSerializable(ARG_);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login_network, container, false);
        initView();
        addListener();
        return v;
    }

    private void addListener() {
       clear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferencesUtil.save(getActivity(),"IS_LOGIN_NETWORK",false);
               SharedPreferencesUtil.save(getActivity(),"TUST_PSWD_NETWORK", "");
               SharedPreferencesUtil.save(getActivity(),"TUST_NUMBER_NETWORK","");
               Toast.makeText(getActivity(),R.string.clear_success,Toast.LENGTH_SHORT).show();
               getActivity().finish();
           }
       });
    }

    private void initView() {
       use_time = (TextView) v.findViewById(R.id.use_time);
       use_data = (TextView) v.findViewById(R.id.use_data);
       use_fee = (TextView) v.findViewById(R.id.use_fee);
       clear = (Button) v.findViewById(R.id.clear_net);
       intData();
    }

    private void intData() {
       use_data.setText(nes.getUse_data());
       use_fee.setText(nes.getUse_fee());
       use_time.setText(nes.getUse_time());
    }

}
