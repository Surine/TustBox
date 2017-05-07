package com.surine.tustbox.Fragment.score;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/4/8.
 */

public class Score_Fragment extends Fragment{
    private static final String ARG_ ="Score_Fragment" ;
    private static final long CONNECT_TIMEOUT = 5;
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    private TabLayout tab;
    private ViewPager viewpager;
    String id_card_pswd;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments =new ArrayList<>();
    private List<String> titles =new ArrayList<>();
    int times =1;
    View v;
    Elements content_Text;
    public static Score_Fragment getInstance(String title) {
        Score_Fragment fra = new Score_Fragment();
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
        v = inflater.inflate(R.layout.fragment_viewpager, container, false);
        initOKhttp();
        startConnect();
        return v;
    }

    //初始化okhttp
    private void initOKhttp() {
        builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);;
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }

    //初始化布局
    private void initLayout() {

        getActivity().setTitle(R.string.score);
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager)v.findViewById(R.id.viewpager);
        tab = (TabLayout)v.findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(This_School_Term_Score_Fragment.getInstance("1"));
        fragments.add(This_All_Score_Fragment.getInstance("2"));
       // fragments.add(This_School_Term_Score_Fragment.getInstance("3"));

        //3.使用string的list集合来添加标题
        titles.add("本学期成绩");
        titles.add("方案成绩");
        titles.add("其他");

        //4.初始化适配器（传入参数：FragmentManager，碎片集合，标题）
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getActivity().getSupportFragmentManager(), fragments, titles);
        //5.设置viewpager适配器
        viewpager.setAdapter(pagerAdapter);

        //6.设置缓存
        /*
        *  * 注意：设置缓存的原因
        * 在加载Tab-A时会实例化Tab-B中fragment，依次调用：onAttach、
        * onCreate、onCreateView、onActivityCreated、onStart和onResume。
        * 同样切换到Tab-B时也会初始化Tab-C中的fragment。（Viewpager预加载）
        * 但是fragment中的数据(如读取的服务器数据)没有相应清除，导致重复加载数据。
        *
        *
        * 注意：ps:我们在使用viewpager时会定义一个适配器adapter，其中实例化了一个fragment列表，
        * 所以在tab切换时fragment都是已经实例化好的，所以在切换标签页时是不会重新实例化fragment
        * 对象的，因而在fragment中定义的成员变量是不会被重置的。所以为列表初始化数据需要注意这个问题。
        *
        * 参考网址：https://my.oschina.net/buobao/blog/644699
*/
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tab.setupWithViewPager(viewpager);


        //8.viewpager的监听器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动监听器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页卡选中监听器
            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {

                }
                else if (position==1)
                {

                }
                else if(position==2)
                {

                }
            }
            //滚动状态变化监听器
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
}

    //建立链接
    private void startConnect() {
        login();   //登录
    }

    private void login() {
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        try {
            String base64 =  pref.getString("pswd",null);

            // 对base64加密后的数据进行解密
            Log.i("Test", "decode >>>" + new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
            id_card_pswd =  new String
                    (Base64.decode(base64.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "给密码解密时产生错误!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        FormBody formBody = new FormBody.Builder()
                .add(getString(R.string.id1), pref.getString("tust_number",null))
                .add(getString(R.string.id2), id_card_pswd)
                .build();
        Request request = new Request.Builder().post(formBody).url(UrlData.login_post_url).build();
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
                Log.d(ARG_, "onResponse: " + str);

                //解决Cookies不正确问题
                if(times == 2){
                    Message mess = new Message();
                    mess.what = 3;
                    myHandler.sendMessage(mess);
                    getScore();
                }else{
                    login();
                    times = times+1;
                }
            }

        });
    }

    private void getScore() {
        //删除数据
        DataSupport.deleteAll(Score_Info.class);
        Request request = new Request.Builder().
                url(UrlData.socre_get_url).build();
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
    //解析并储存
    private void Jsoup_the_Html(String str) {
        Document doc = Jsoup.parse(str);
        Elements content2 = doc.select("tr");
        for (int i = 7; i < content2.size(); i++) {
            content_Text = content2.get(i).select("td");
            Score_Info score_info = new Score_Info();
            score_info.setType("THIS");
            score_info.setName(content_Text.get(2).text());
            score_info.setEnglish_name(content_Text.get(3).text());
            score_info.setScore(content_Text.get(9).text());
            score_info.setCredit(content_Text.get(4).text());
            score_info.setRanking(content_Text.get(10).text());
            score_info.setAve(content_Text.get(8).text());
            score_info.save();
        }
        Log.d(ARG_, "Jsoup_the_Html: 解析成功");
        getAllScore();
    }

    private void getAllScore() {
        Request request = new Request.Builder().
                url(UrlData.all_socre_get_url).build();
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
                Log.d(ARG_, "onResponse: " + str);
                Jsoup_All(str);
            }
        });
    }

    private void Jsoup_All(String str) {
        Document doc = Jsoup.parse(str);
        Elements content = doc.select("tr");
        for (int i = 6; i < content.size()-9; i++) {
            content_Text = content.get(i).select("td");
            Score_Info score_info = new Score_Info();
            score_info.setType("ALL");
            score_info.setName(content_Text.get(2).text());
            score_info.setEnglish_name(content_Text.get(3).text());
            score_info.setScore(content_Text.get(6).text());
            score_info.setCredit(content_Text.get(4).text());
            score_info.setRanking("无");
            score_info.setAve("无");
            score_info.save();
        }
        String info_string = content.get(content.size()-8).text();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("learn_info",info_string);
        editor.apply();
        Message mess = new Message();
        mess.what = 1;
        myHandler.sendMessage(mess);
    }


    //use handler to change ui
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Snackbar.make(getView(), R.string.save_success,Snackbar.LENGTH_SHORT).show();
                    initLayout();
                    break;
                case 2:
                    Snackbar.make(getView(), R.string.fail,Snackbar.LENGTH_LONG).setAction("查看本地数据", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initLayout();
                        }
                    }).show();
                    break;
                case 3:
                    Snackbar.make(getView(), R.string.login_success,Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };


}
