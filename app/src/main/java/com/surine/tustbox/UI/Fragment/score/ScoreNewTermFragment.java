package com.surine.tustbox.UI.Fragment.score;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.surine.tustbox.Adapter.Recycleview.ScoreBaseAdapter;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Manager.OkhttpManager;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Pojo.Cookies;
import com.surine.tustbox.Pojo.ScoreInfoHelper;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Pojo.EventBusBean.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.EncryptionUtil;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.surine.tustbox.App.Data.FormData.SET_COOKIE;

/**
 * Created by surine on 2017/4/8.
 */

public class ScoreNewTermFragment extends Fragment {
    private static final String ARG_ = "ScoreNewTermFragment";
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
    private List<ScoreInfoHelper> scores = new ArrayList<>();
    private ScoreBaseAdapter adapter;
    private List<ScoreInfoHelper> mScoreFromDB;
    private String tag;
    private FragmentActivity mContext;
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    private String tustNumber = null;
    private String cookieMsg = "";
    private String myVerifyCodeString = "";

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

        //初始化必要数据
        tustNumber = new TustBoxUtil(getActivity()).getUid();

        //初始化okhttp
        initOkhttp();

        //首先加载动画
        doubleBounce = new DoubleBounce();
        spinKit.setIndeterminateDrawable(doubleBounce);

        //加载验证码
        initVerifyCode();


        //初始化列表
        adapter = new ScoreBaseAdapter(R.layout.item_score, scores);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        listScore.setLayoutManager(new LinearLayoutManager(mContext));
        listScore.setAdapter(adapter);

        return v;
    }


    private void initVerifyCode() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_score_verifycode,null);
        final ImageView verifyCode =view.findViewById(R.id.verifyCodeImage);
        final EditText inputVerifyCode = view.findViewById(R.id.inputVerifyCode);
        okHttpClient.newCall(new Request.Builder().url(UrlData.captchaUrl).build()).
                enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取验证码图片比特流
                final byte[] picBt = response.body().bytes();
                Headers headers = response.headers();
                //获取验证码所携带的cookie
                cookieMsg = headers.get(SET_COOKIE);
                RunOnUiThread.updateUi(getActivity(), new UpdateUIListenter() {
                    @Override
                    public void update() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picBt, 0, picBt.length);
                        verifyCode.setImageBitmap(bitmap);
                    }
                });
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("验证身份");
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(inputVerifyCode.getText().toString().isEmpty()){
                    return;
                }
                myVerifyCodeString = inputVerifyCode.getText().toString();
                //建立链接
                startConnect();
            }
        });
        builder.show();
    }

    //加载简洁版本请求
    private void initOkhttp() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    //建立链接
    private void startConnect() {
        if(loadingText != null){
            loadingText.setClickable(false);
            loadingText.setText("正在登录教务处……");
        }

        try {
           // loginJWC();   //登录
            loginJwcNew();  //新版本登录教务处
        } catch (Exception e) {
            e.printStackTrace();
            if(loadingText != null){
                loadingText.setClickable(false);
                loadingText.setText("异常发生，请退出APP重试");
            }
        }
    }

    //新版登录教务处
    private void loginJwcNew() {

        try {
            // 对base64加密后的数据进行解密
            id_card_pswd = EncryptionUtil.base64_de(SharedPreferencesUtil.Read(mContext, "pswd", ""));
        } catch (Exception e) {
            loadingText.setClickable(true);
            loadingText.setText("解码错误，点我重试");
            e.printStackTrace();
            return;
        }

        //学号为空
        if(tustNumber == null){
            return;
        }

        //构建表单
        FormBody formBody = new FormBody.Builder()
                .add(FormData.login_id_new, tustNumber)
                .add(FormData.login_pswd_new, id_card_pswd)
                .add(FormData.login_captcha_new, myVerifyCodeString)
                .build();

        Request request = new Request.Builder()
                .addHeader(SET_COOKIE,cookieMsg)
                .url(UrlData.login_post_url_new)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingText.setClickable(true);
                        loadingText.setText("网络错误，点我重试");                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d(ARG_,str);
                final Document doc = Jsoup.parse(str);
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(doc.title().equals(getString(R.string.manager_))){
                            getScoreJWC();
                        }else{
                            loadingText.setClickable(true);
                            loadingText.setText("身份验证失败，点我重试");
                        }
                    }
                });
            }
        });
    }

    //获取成绩新版
    private void getScoreJWC() {
        Request request = new Request.Builder()
                .url(UrlData.getScoreUrl)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingText.setClickable(true);
                        loadingText.setText("网络错误，点我重试");                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String str = response.body().string();
               Log.d(ARG_,str);
                //删除数据
                DataSupport.deleteAll(ScoreInfoHelper.class);
               try {
                    //准备解析
                    JSONObject jsonObj = new JSONObject(str);
                    //获取成绩列表
                    String cjList = jsonObj.getString("lnList");
                    Log.d(ARG_,cjList);
                    JSONArray jsonArray =  new JSONArray(cjList);
                   for (int i = 0; i < jsonArray.length(); i++) {
                       String jsArrayString = jsonArray.getString(i);
                       JSONObject toGetCjList = new JSONObject(jsArrayString);
                       //学期
                      // String termInfo = toGetCjList.getString("cjbh");
                       //学期编号
                       String termNumber = String.valueOf(i + 1);
                       //Log.d(ARG_,"学期"+ termInfo);
                       JSONArray cjbh = new JSONArray(toGetCjList.getString("cjList"));
                       for (int j = 0; j < cjbh.length(); j++) {
                           Log.d(ARG_,"学期"+cjbh.get(j));
                           JSONObject score = new JSONObject(cjbh.get(j).toString());
                           String cj = score.getString("cj");
                           String courseAttributeName = score.getString("courseAttributeName");
                           String courseName = score.getString("courseName");
                           String credit = score.getString("credit");
                           String englishCourseName = score.getString("englishCourseName");
                           String gradePointScore = score.getString("gradePointScore");
                           String gradeName = score.getString("gradeName");

                           ScoreInfoHelper scoreInfoHelper = new ScoreInfoHelper();
                           scoreInfoHelper.setCj(cj);
                           scoreInfoHelper.setCourseAttributeName(courseAttributeName);
                           scoreInfoHelper.setCourseName(courseName);
                           scoreInfoHelper.setCredit(credit);
                           scoreInfoHelper.setEnglishCourseName(englishCourseName);
                           scoreInfoHelper.setGradePointScore(gradePointScore);
                           scoreInfoHelper.setGradeName(gradeName);
                           scoreInfoHelper.setTermInfo(termNumber);
                           scoreInfoHelper.save();
                       }
                   }

                   if(getActivity() == null){
                       throw new JSONException("NullPointer");
                   }
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           updateList();  //加载列表
                       }
                   });

                   //通知AllScoreFragment来更新数据库
                   EventBus.getDefault().post(new SimpleEvent(12,""));


               } catch (JSONException e) {
                   if(loadingText == null){
                       return;
                   }
                   loadingText.setClickable(true);
                   loadingText.setText("解析失败！点我重试");
                    e.printStackTrace();
               }
            }
        });
    }


    private void updateList() {

        ScoreInfoHelper scoreInfoHelper = DataSupport.findLast(ScoreInfoHelper.class);
        if(scoreInfoHelper == null){
            return;
        }
       // mScoreFromDB = DataSupport.order("termInfo DESC").find(ScoreInfoHelper.class);
        mScoreFromDB = DataSupport.where("termInfo = ?",scoreInfoHelper.getTermInfo()).find(ScoreInfoHelper.class);

        for(ScoreInfoHelper score:mScoreFromDB){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.loading_text)
    public void onViewClicked() {
        startConnect();
    }
}
