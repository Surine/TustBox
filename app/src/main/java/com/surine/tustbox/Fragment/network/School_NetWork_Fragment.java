package com.surine.tustbox.Fragment.network;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/4/6.
 */

public class School_NetWork_Fragment extends Fragment {
    private static final String ARG_ ="School_NetWork_Fragment" ;
    private TabLayout tab;
    private ViewPager viewpager;
    private String random;
    EditText e2;
    EditText e1;
    String last_string;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments =new ArrayList<>();
    private List<String> titles =new ArrayList<>();
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    private int flag = 0;

    public static School_NetWork_Fragment getInstance(String title) {
        School_NetWork_Fragment fra = new School_NetWork_Fragment();
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
        View v = inflater.inflate(R.layout.fragment_viewpager, container, false);
        initOKhttp();
        initLogin(v);   //初始化登录
        return v;

    }

    private void initOKhttp() {
            builder = new OkHttpClient.Builder();
            okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }


    private void initLogin(final View v) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_login_work_view,null);
       e1 = (EditText)view.findViewById(R.id.editText2);
       e2 = (EditText)view.findViewById(R.id.editText3);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO:加密，储存帐号密码
                SaveData(e1.getText().toString(),e2.getText().toString(),v);
            }
        });
        builder.show();
    }

    private void SaveData(String s, String s1,View v) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
       //TODO：保存登录状态
        // editor.putBoolean("is_login_network", true);

        //TODO:加密密码
        editor.putString("password_",s1);
        editor.putString("user_name",s);
        editor.apply();
        Login_prepare();
        //初始化数据
        initData(v);
    }

    private void Login_prepare() {
        Request request = new Request.Builder().url(UrlData.login_net_prepare_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                /*取得script下面的JS变量*/
                Document doc = Jsoup.parse(str);//解析HTML字符串返回一个Document实现
                Elements link = doc.select("script").eq(5);
                String get_number = link.toString().replaceAll("[^0-9]", "");
                Log.d("the_link", "onResponse: "+get_number);
                String get_number2 = get_number.substring(0,get_number.length()-1);
                Log.d("the_link", "onResponse: "+get_number2);
                int last = get_number2.lastIndexOf("0001")+4;
                Log.d("the_link", "onResponse: "+last);
                last_string = get_number2.substring(last,get_number2.length());
                Log.d("the_link", "onResponse: "+last_string);
                Login_Test("xxx","xxx");
            }
        });
    }



    private void Login_Test(String s, String s1) {

        FormBody formBody = new FormBody.Builder()
                .add("account", s)
                .add("Submit","%E7%99%BB+%E5%BD%95")
                .add("checkcode",last_string)
                .add("password", getMD5Str(s1))
                .build();
        Request request = new Request.Builder().post(formBody).url(UrlData.login_net_post_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string().toString();
                //TODO:结束后删除LOG
               if(flag <= 3) {
                   flag = flag + 1;
                   Login_Test("xxx","xxx");
               }else{
                   Message mess = new Message();
                   mess.what = 1;
                   myHandler.sendMessage(mess);
               }
            }
        });
    }

    private void Login_Test_Again(String s, String s1) {
        FormBody formBody = new FormBody.Builder()
                .add("account", s)
                .add("Submit","%E7%99%BB+%E5%BD%95")
                .add("checkcode",last_string)
                .add("password", getMD5Str(s1))
                .add("code",random)
                .build();
        Request request = new Request.Builder().post(formBody).url(UrlData.login_net_post_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string().toString();
                //TODO:结束后删除LOG
                Log.d("jieguo", "onResponse: "+str);
            }
        });
    }


    private void initData(View v) {
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager)v.findViewById(R.id.viewpager);
        tab = (TabLayout)v.findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(Login_NetWork_Fragment.getInstance("login"));
        fragments.add(Login_NetWork_Fragment.getInstance("login"));

        //3.使用string的list集合来添加标题
        titles.add("登录");
        titles.add("充值");

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



    private String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        //16位加密，从第9位到25位
        return md5StrBuff.toString().toLowerCase();
    }


    //use handler to change ui
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    initDialog();
                case 2:

                    break;
                case 3:
                    Snackbar.make(getView(), R.string.login_success,Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.test,null);
        final EditText ed = (EditText) view.findViewById(R.id.get_random);
        ImageView im = (ImageView) view.findViewById(R.id.imageView7);
        Glide.with(getActivity()).load(UrlData.random_code).into(im);
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 random = ed.getText().toString();
                Log.d("shuju", "onClick: "+random);
                Login_Test_Again("xxx","xxx");
            }
        });
        builder.show();
    }


}
