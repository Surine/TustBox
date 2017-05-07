package com.surine.tustbox.Fragment.gp_download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.Recycleview.GPdownload_Adapter;
import com.surine.tustbox.Bean.GpInfo;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by surine on 2017/4/24.
 */

public class GP_download_Fragment extends Fragment {
    private static final String ARG_ = "GP_download_Fragment";
    private static final long CONNECT_TIMEOUT = 5;
    private RecyclerView gp_rec;
    private OkHttpClient.Builder builder;
    View v;
    private OkHttpClient okHttpClient;
    private List<GpInfo> mgpinfos = new ArrayList<>();
    public static GP_download_Fragment getInstance(String title) {
        GP_download_Fragment fra = new GP_download_Fragment();
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_gp, container, false);
        initData();
        init_Recycleview();
        return v;
    }

    private void initData() {
        initOKhttp();
        startConnect();
    }

    private void init_Recycleview() {
        gp_rec = (RecyclerView) v.findViewById(R.id.gp_rec);
        gp_rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        gp_rec.setAdapter(new GPdownload_Adapter(mgpinfos,getActivity()));
    }

    //init khttp
    private void initOKhttp() {
        builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);;
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }

    //start get
    private void startConnect() {
        Request request = new Request.Builder().url(UrlData.gp_download).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message mess = new Message();
                mess.what = 2;
                myHandler.sendMessage(mess);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                Jsoup_the_Html(str);
            }
        });
    }

    private void Jsoup_the_Html(String str) {
            Document doc = Jsoup.parse(str);
           //get h2 tag
            Elements content = doc.select("h2");
            for (int i = 0; i < content.size(); i++) {
                Log.d(ARG_, i+"H2标签" + content.get(i).text());
            }
            //get ul tag
            Elements content2 = doc.select("ul");
            for (int l = 0; l < 10; l++) {
                Log.d(ARG_, l+"UL标签: " + content2.get(l).text());
            }
            //get img
            Elements content3= doc.getElementsByTag("img");
            for (int j = 0; j < content3.size(); j++) {
                Log.d(ARG_, j+"IMG: " + content3.get(j).attr("src"));
            }
            //get link
            Elements content4= doc.select("a");
            for (int j = 0; j < content4.size(); j++) {
            Log.d(ARG_, j+"HREF: " + content4.get(j).attr("href"));
            }
            //loadinfo
            for(int k = 0;k<9;k++){
                GpInfo gpinfo = new GpInfo(content3.get(k).attr("src"),content.get(k).text(),content2.get(k+1).text(),UrlData.gp_download_short+content4.get((k*2)+6).attr("href"));
                mgpinfos.add(gpinfo);
            }
        for(int k = 9;k<content3.size();k++){
            GpInfo gpinfo = new GpInfo(content3.get(k).attr("src"),content.get(k).text(),"无相关简介",UrlData.gp_download_short+content4.get((k*2)+6).attr("href"));
            mgpinfos.add(gpinfo);
        }



            Message mess = new Message();
            mess.what = 1;
            myHandler.sendMessage(mess);

    }


    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    getActivity().setTitle("GP下载平台");
                    Snackbar.make(getView(), "加载成功",Snackbar.LENGTH_SHORT).show();
                    init_Recycleview();
                    break;
                case 2:
                    Snackbar.make(getView(), "加载失败",Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
