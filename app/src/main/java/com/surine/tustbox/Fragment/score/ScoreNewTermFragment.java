package com.surine.tustbox.Fragment.score;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.surine.tustbox.Adapter.Recycleview.MessageAdapter;
import com.surine.tustbox.Adapter.Recycleview.ScoreBaseAdapter;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.ScoreInfo;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.NetWork.JavaNetCookieJar;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

public class ScoreNewTermFragment extends Fragment {
    private static final String ARG_ = "ScoreNewTermFragment";
    private static final long CONNECT_TIMEOUT = 5;
    @BindView(R.id.listScore)
    RecyclerView listScore;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.loading_text)
    Button loadingText;
    Unbinder unbinder;
    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    String id_card_pswd;
    int times = 1;
    View v;
    Elements content_Text;
    private DoubleBounce doubleBounce;
    private List<ScoreInfo> scores = new ArrayList<>();
    private ScoreBaseAdapter adapter;
    private List<ScoreInfo> mScoreFromDB;
    private String tag;
    private FragmentActivity mContext;

    public static ScoreNewTermFragment getInstance(String title) {
        ScoreNewTermFragment fra = new ScoreNewTermFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        tag = bundle.getString(ARG_);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_now_term, container, false);
        unbinder = ButterKnife.bind(this, v);



        //首先加载动画
        doubleBounce = new DoubleBounce();
        spinKit.setIndeterminateDrawable(doubleBounce);
        builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        //初始化列表
        adapter = new ScoreBaseAdapter(R.layout.item_score, scores);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        listScore.setLayoutManager(new LinearLayoutManager(mContext));
        listScore.setAdapter(adapter);
        //建立链接
        startConnect();
        return v;
    }

    //建立链接
    private void startConnect() {
        if(loadingText != null){
            loadingText.setClickable(false);
            loadingText.setText("正在登录教务处……");
        }

        try {
            loginJWC();   //登录
        } catch (Exception e) {
            e.printStackTrace();
            if(loadingText != null){
                loadingText.setClickable(false);
                loadingText.setText("异常发生，请退出APP重试");
            }
        }
    }

    private void loginJWC() throws Exception{

        try {
            // 对base64加密后的数据进行解密
            id_card_pswd = EncryptionUtil.base64_de(SharedPreferencesUtil.Read(mContext, "pswd", ""));
        } catch (Exception e) {
            loadingText.setClickable(true);
            loadingText.setText("解码错误，点我重试");
            e.printStackTrace();
        }

        FormBody formBody = new FormBody.Builder()
                .add(FormData.login_id, SharedPreferencesUtil.Read(mContext, "tust_number", ""))
                .add(FormData.login_pswd, id_card_pswd)
                .build();
        Request request = new Request.Builder().post(formBody).url(UrlData.login_post_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    if(mContext == null){
                        return;
                    }
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(loadingText != null){
                                loadingText.setClickable(true);
                                loadingText.setText(R.string.scorenewtermfragmentnetnoconn);
                            }

                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                if(mContext == null){
                    return;
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解决Cookies不正确问题
                        if (times == 2) {
                            if(loadingText != null) {
                                loadingText.setClickable(false);
                                loadingText.setText("登录成功，正在获取本学期成绩……");
                            }
                            try {
                                getScore();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                if(loadingText != null) {
                                    loadingText.setClickable(false);
                                    loadingText.setText("首次登录成功，正在确认登录……\n如果您长时间看见本提示，请返回首页重新进入！");
                                }
                                times = times + 1;
                                loginJWC();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private void getScore() throws Exception{
        Request request = new Request.Builder().
                url(UrlData.socre_get_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mContext == null){
                    return;
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(loadingText != null) {
                            loadingText.setClickable(true);
                            loadingText.setText(R.string.scorenewtermfragmentnetnoconn);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                if(mContext == null){
                    return;
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(loadingText != null) {
                            loadingText.setClickable(false);
                            loadingText.setText("正在解析数据……");
                        }
                    }
                });
                try {
                    Jsoup_the_Html(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //解析并储存
    private void Jsoup_the_Html(String str) throws Exception{
        //删除数据
        DataSupport.deleteAll(ScoreInfo.class);
        Document doc = Jsoup.parse(str);
        Elements content2 = doc.select("tr");
        try {
            for (int i = 7; i < content2.size(); i++) {
                content_Text = content2.get(i).select("td");
                ScoreInfo score_info = new ScoreInfo();
                score_info.setType("THIS");
                score_info.setName(content_Text.get(2).text());
                score_info.setEnglish_name(content_Text.get(3).text());
                score_info.setScore(content_Text.get(9).text());
                score_info.setCredit(content_Text.get(4).text());
                score_info.setRanking(content_Text.get(10).text());
                score_info.setAve(content_Text.get(8).text());
                score_info.save();
            }
            if(mContext == null){
                return;
            }
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(loadingText != null) {
                        loadingText.setClickable(false);
                        loadingText.setText("解析成功！");
                    }
                    updateList();
                }
            });

            //抓取全部成绩
            getAllScore();

        } catch (Exception e) {
            e.printStackTrace();
            if(mContext == null){
                return;
            }
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(loadingText != null) {
                        loadingText.setClickable(true);
                        loadingText.setText("数据解析失败！请返回首页重试");
                    }
                }
            });
        }

    }

    private void updateList() {

        mScoreFromDB = DataSupport.where("type = ?", "THIS").find(ScoreInfo.class);
        for(ScoreInfo score:mScoreFromDB){
            scores.add(score);
        }
        adapter.notifyDataSetChanged();
        if(loadingText == null){
            return;
        }
        if(spinKit == null){
            return;
        }
        loadingText.setVisibility(View.GONE);
        spinKit.setVisibility(View.GONE);

    }

    private void getAllScore() {
        Request request = new Request.Builder().
                url(UrlData.all_socre_get_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                Jsoup_All(str);
            }
        });
    }

    private void Jsoup_All(String str) {
        try {
            Document doc = Jsoup.parse(str);
            Elements content = doc.select("tr");
            for (int i = 6; i < content.size() - 9; i++) {
                content_Text = content.get(i).select("td");
                ScoreInfo score_info = new ScoreInfo();
                score_info.setType("ALL");
                score_info.setName(content_Text.get(2).text());
                score_info.setEnglish_name(content_Text.get(3).text());
                score_info.setScore(content_Text.get(6).text());
                score_info.setCredit(content_Text.get(4).text());
                score_info.setRanking("无");
                score_info.setAve("无");
                score_info.save();
            }
            String info_string = content.get(content.size() - 8).text();
            if(mContext != null){
                SharedPreferences.Editor editor = mContext.getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("learn_info", info_string);
                editor.apply();
            }


            EventBus.getDefault().post(new SimpleEvent(12,""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.loading_text)
    public void onViewClicked() {
      //  getActivity().finish();
        startConnect();
    }
}
