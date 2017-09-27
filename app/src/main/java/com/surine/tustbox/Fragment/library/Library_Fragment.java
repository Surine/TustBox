package com.surine.tustbox.Fragment.library;

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
import android.widget.Toast;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Bean.Book_Info;
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
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/4/10.
 */

public class Library_Fragment extends Fragment{
    private static final String ARG_ = "Library_Fragment";
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments =new ArrayList<>();
    private List<String> titles =new ArrayList<>();
    int times =1;
    View v;
    String random_ps = getRandomString(10,1);
    String random_number = getRandomString(9,2);
    String random_ps2 = getRandomString(10,1);

    public static String getRandomString(int length,int type) { //length表示生成字符串的长度,type表示类型
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String base2 = "0123456789";
        Random random = new Random();
        if(type == 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(base.length());
                sb.append(base.charAt(number));
            }
            return sb.toString();
        }
        else{
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(base2.length());
                sb.append(base2.charAt(number));
            }
            return sb.toString();
        }

    }

    public static Library_Fragment getInstance(String title) {
        Library_Fragment fra = new Library_Fragment();
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
        //第一次登录后记住密码，并且保存登录状态
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        if(!pref.getBoolean("login_success_about_library_pass",false)){
            showLogin();
        } else{
            StartLogin();   //开始登录
        }
        return v;
    }

    private void initView() {

        getActivity().setTitle(getString(R.string.library));
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager)v.findViewById(R.id.viewpager);
        tab = (TabLayout)v.findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(Borrow_book_fragment.getInstance("1"));
        // fragments.add(This_School_Term_Score_Fragment.getInstance("2"));
        // fragments.add(This_School_Term_Score_Fragment.getInstance("3"));

        //3.使用string的list集合来添加标题
        titles.add("借阅书籍");
        titles.add("书籍查询");
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

    private void showLogin() {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_login_work_view,null);
        final EditText id = (EditText) view.findViewById(R.id.tust_number);
        final EditText pswd = (EditText) view.findViewById(R.id.network_passwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton("下一步", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(id.getText().toString().equals("")||pswd.getText().toString().equals("")){
                    Snackbar.make(view, "输入为空",Snackbar.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("library_id",id.getText().toString());
                    editor.putString("library_pswd",pswd.getText().toString());
                    editor.putBoolean("login_success_about_library_pass",true);
                    editor.apply();
                   StartLogin();
                }
            }
        });
        builder.setNeutralButton("密码说明", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showInfo();
            }
        });
        builder.show();
    }

    private void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("密码说明");
        builder.setMessage("1.校园网密码是学校要求填写个人信息时修改过的\n" +
                "2.图书馆密码默认是123,如果有同学修改过那就是修改过的密码");
        builder.setPositiveButton(R.string.i_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               showLogin();
            }
        });
        builder.show();
    }


    private void StartLogin() {
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
       login("R030KX"+pref.getString("library_id","65535"),pref.getString("library_pswd","65535"));
    }

    private void login(final String student_id, final String student_pswd) {
        FormBody formBody = new FormBody.Builder()
                .add("user_id", student_id)
                .add("password",student_pswd)
                .add("ps",random_ps+"/高校联合馆/"+random_number+"/303")
                .build();
        Log.d("url_test", "login: "+UrlData.library_url+random_ps+"/高校联合馆/"+random_number+"/303");
        Request request = new Request.Builder().post(formBody).url(UrlData.library_url+random_ps+"/高校联合馆/"+random_number+"/303").build();
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
                if(str.contains("登录无效")){
                    Toast.makeText(getActivity(),"帐号或者密码错误，请查看密码说明。",Toast.LENGTH_LONG).show();
                }else {
                    //解决Cookies不正确问题
                    if (times == 2) {
                        Message mess = new Message();
                        mess.what = 3;
                        myHandler.sendMessage(mess);
                        get_My_borrow_Book();
                    } else {
                        login(student_id, student_pswd);
                        times = times + 1;
                    }
                }
            }

        });
    }

    //借书
    private void get_My_borrow_Book() {
        //删除数据
        DataSupport.deleteAll(Book_Info.class);
        FormBody formBody = new FormBody.Builder()
                .add("ps",random_ps2+"/科技大学馆/"+random_number+"/30")
                .build();
        Log.d("url_test", "login: "+UrlData.library_url+random_ps2+"/科技大学馆/"+random_number+"/30");
        Request request = new Request.Builder().post(formBody).
                url(UrlData.my_borrow_url+random_ps2+"/科技大学馆/"+random_number+"/30").build();
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
                Log.d("the_my", "onResponse: " + str);
                Jsoup_the_Html(str);
            }
        });
    }

    private void Jsoup_the_Html(String str) {
        Document doc = Jsoup.parse(str);
        Elements content2 = doc.select("tr");
        Log.d("the_jsoup", "Jsoup_the_Html: "+content2.get(0).text());
       // Log.d("the_jsoup", "Jsoup_the_Html: "+content2.get(1).text());
        for (int i = 1; i < content2.size()-1; i++) {
            Elements content_Text = content2.get(i).select("td");
            Book_Info book_info = new Book_Info();
            book_info.setBook_name(content_Text.get(0).text());
            book_info.setAuthor(content_Text.get(1).text());
            book_info.setDead_line(content_Text.get(2).text());
            book_info.setStatus(content_Text.get(3).text());
            book_info.setMoney(content_Text.get(4).text());
            book_info.save();
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
                    Snackbar.make(getView(), R.string.save_success,Snackbar.LENGTH_SHORT).show();
                   initView();
                    break;
                case 2:
                    Snackbar.make(getView(), R.string.fail,Snackbar.LENGTH_LONG).setAction("查看本地数据", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                    initView();
                        }
                    }).show();
                    break;
                case 3:
                    Snackbar.make(getView(), R.string.login_success,Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //初始化okhttp
    private void initOKhttp() {
        builder = new OkHttpClient.Builder();
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
    }
}

