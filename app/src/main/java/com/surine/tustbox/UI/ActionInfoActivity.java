package com.surine.tustbox.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.action.CommentFragment;
import com.surine.tustbox.Fragment.action.LoveFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.Data.Constants.HTTP;
import static com.surine.tustbox.Data.Constants.PIC_CROP;
import static com.surine.tustbox.Fragment.PageFragment.ThirdPageFragment.DOWNTOREFRESH;

/**
 * Created by Surine on 2018/2/23.
 * 动态详情
 */

public class ActionInfoActivity extends TustBaseActivity {
    @BindView(R.id.action_info_head)
    CircleImageView actionInfoHead;
    @BindView(R.id.action_info_user_name)
    TextView actionInfoUserName;
    @BindView(R.id.action_info_update_time)
    TextView actionInfoUpdateTime;
    @BindView(R.id.action_info_message)
    TextView actionInfoMessage;
    @BindView(R.id.iv_ngrid_layout)
    NineGridView ivNgridLayout;
    @BindView(R.id.action_info_device)
    TextView actionInfoDevice;
    @BindView(R.id.action_info_love_num)
    TextView actionInfoLoveNum;
    @BindView(R.id.action_info_comment_num)
    TextView actionInfoCommentNum;
    @BindView(R.id.re1)
    RelativeLayout re1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.comment)
    ImageView comment;
    @BindView(R.id.love)
    ImageView love;
    @BindView(R.id.CoordinatorLayout)
    android.support.design.widget.CoordinatorLayout CoordinatorLayout;
    private Context context;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private SimpleFragmentPagerAdapter pagerAdapter;
    private int did = 0;
    private String uid = null;
    private Action action;
    private String ThumbnailUrl;
    private String BigImageCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_info);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        context = this;
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("动态详情");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        did = getIntent().getIntExtra(FormData.did, 0);
        initActionInfo(did);
        initViewPager();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViewPager() {
        fragments.add(CommentFragment.getInstance(String.valueOf(did)));
        fragments.add(LoveFragment.getInstance(String.valueOf(did)));
        titles.add("评论");
        titles.add("赞");
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewpager);
    }

    private void initActionInfo(int did) {
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        HttpUtil.get(UrlData.getActionById + "?id=" + did + "&" + FormData.uid + "=" + tust_number).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActionInfoActivity.this, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAF", x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        action = GsonUtil.parseJsonWithGson(jsonObject.getString(FormData.JDATA), Action.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (action.getUser().getFace() != null) {
                                    //设置头像
                                    try {
                                        Glide.with(ActionInfoActivity.this).load(action.getUser().getFace()).into(actionInfoHead);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                  //喜欢
                                if (action.isIslove()) {
                                  love.setImageResource(R.drawable.love_red);
                                }else {
                                    love.setImageResource(R.drawable.love);
                                }

                                //加载昵称
                                if(action.getUser()!=null){
                                    if(action.getUser().getSchoolname() == null||action.getUser().getSchoolname().equals("")){
                                        if(action.getUser().getNick_name() == null){
                                            actionInfoUserName.setText("未设置");
                                        }else{
                                            actionInfoUserName.setText(action.getUser().getNick_name());
                                        }
                                    }else{
                                        actionInfoUserName.setText(action.getUser().getSchoolname());
                                    }
                                }


                                if (action.getMessages_time() != null) {
                                    //设置时间
                                    actionInfoUpdateTime.setText(action.getMessages_time());
                                }
                                if (action.getMessages_info() != null) {
                                    //设置内容
                                    actionInfoMessage.setText(action.getMessages_info());
                                }
                                if (action.getMessages_device() != null) {
                                    //设置设备
                                    actionInfoDevice.setText(action.getMessages_device());
                                }
                                if (action.getMessages_agreenum() != null) {
                                    //设置点赞数
                                    actionInfoLoveNum.setText(action.getMessages_agreenum());
                                }
                                if (action.getMessages_commentnum() != null) {
                                    //设置评论数
                                    actionInfoCommentNum.setText(action.getMessages_commentnum());
                                }

                                if(action.getUid()!=null){
                                    uid = action.getUid();
                                }

                                if (action.getPic_ids() != null && !action.getPic_ids().equals("")) {
                                    //有图像
                                    String[] pics = action.getPic_ids().split(",");
                                    ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context) ;
                                    for (int i = 0; i < pics.length; i++) {
                                        if(prefs.getBoolean("pic_crop",true)){
                                            //不开启图片压缩
                                            ThumbnailUrl = HTTP+action.getPicbaseurl()+pics[i];
                                            BigImageCrop = HTTP+action.getPicbaseurl()+pics[i];
                                        }else{
                                            //开启图片压缩
                                            ThumbnailUrl = HTTP+action.getPicbaseurl()+pics[i]+ PIC_CROP;
                                            BigImageCrop = HTTP+action.getPicbaseurl()+pics[i];
                                        }
                                        ImageInfo info = new ImageInfo();
                                        info.setThumbnailUrl(ThumbnailUrl);
                                        info.setBigImageUrl(BigImageCrop);
                                        imageInfo.add(info);

                                    }
                                    ivNgridLayout.setAdapter((new myNineClickAdapter(context, imageInfo)));
                                }
                            }
                        });
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    class myNineClickAdapter extends NineGridViewClickAdapter {
        int statusHeight;

        public myNineClickAdapter(Context context, List<ImageInfo> imageInfo) {
            super(context, imageInfo);
            statusHeight = getStatusHeight(context);
        }

        @Override
        protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
            for (int i = 0; i < imageInfo.size(); i++) {
                ImageInfo info = imageInfo.get(i);
                View imageView;
                if (i < nineGridView.getMaxSize()) {
                    imageView = nineGridView.getChildAt(i);
                } else {
                    //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
                    imageView = nineGridView.getChildAt(nineGridView.getMaxSize() - 1);
                }
                info.imageViewWidth = imageView.getWidth();
                info.imageViewHeight = imageView.getHeight();
                int[] points = new int[2];
                imageView.getLocationInWindow(points);
                info.imageViewX = points[0];
                info.imageViewY = points[1] - statusHeight;
            }

            Intent intent = new Intent(context, ImagePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @OnClick({R.id.love,R.id.comment,R.id.floatingActionButton, R.id.action_info_head, R.id.action_info_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                //发送评论
                sendComment();
                break;
            case R.id.action_info_head:
                startActivity(new Intent(context, UserInfoActivity.class).putExtra(FormData.uid,uid));
                break;
            case R.id.action_info_message:
                break;
            case R.id.love:
                loveThisAction(did);
                break;
            case R.id.comment:
                //发送评论
                sendComment();
                break;
        }
    }

    //点赞
    private void loveThisAction(int id) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context,FormData.token,"");
        String tust_number = SharedPreferencesUtil.Read(context,FormData.tust_number_server,"");
        String buildUrl = UrlData.loveAction+"?"+FormData.token+"="+token+"&"+FormData.did+"="+
                id+"&"+FormData.uid+"="+tust_number;
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String x = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //点赞成功
                                initActionInfo(did);
                                //通知更新 TODO:可以改作局部刷新
                                EventBus.getDefault().post(new SimpleEvent(4,"S"));
                                EventBus.getDefault().post(new SimpleEvent(5,"S"));
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 401){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.null_input, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 500){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //取消赞
                                initActionInfo(did);
                                //通知更新
                                EventBus.getDefault().post(new SimpleEvent(4,"S"));
                                //取消赞通知列表更新
                                EventBus.getDefault().post(new SimpleEvent(8,action.getUid()));
                            }
                        });
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    //发送评论
    private void sendComment() {
        String type = SharedPreferencesUtil.Read_safe(context, FormData.USER_TYPE, "0");
        if(type.equals("3")){
            Toast.makeText(context, R.string.SendActionActivityAccountError, Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.ActionInfoActivitySendComment);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_view_editview, null);
            final EditText editText = (EditText) view.findViewById(R.id.edit_view);
            builder.setView(view);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //发送
                    if (!editText.getText().equals("")) {
                        sendCommentToServer(editText.getText().toString());
                    } else {
                        Toast.makeText(context, R.string.SendActionActivityNullParam, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        }
    }

    //发送评论到服务器
    private void sendCommentToServer(String s) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(context, "TOKEN", "");
        String tust_number = SharedPreferencesUtil.Read(context, "tust_number", "");
        FormBody formBody = new FormBody.Builder()
                .add(FormData.token, token)
                .add(FormData.did, String.valueOf(getIntent().getIntExtra(FormData.did, 0)))
                .add(FormData.uid, tust_number)
                .add(FormData.comment_content, s)
                .build();
        HttpUtil.post(UrlData.addComment, formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String x = response.body().string();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActionInfoActivity.this, R.string.ActionInfoActivityCommentSuccess, Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new SimpleEvent(2, "S"));
                                initActionInfo(did);  //更新动态页面
                                EventBus.getDefault().post(new SimpleEvent(4, "S"));  //动态页的评论数
                            }
                        });
                    } else if (jsonObject.getInt(FormData.JCODE) == 401) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActionInfoActivity.this, R.string.null_input, Toast.LENGTH_SHORT).show();
                                // finish();
                            }
                        });
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetMessage(SimpleEvent simpleBusInfo) {
        if (simpleBusInfo.getId() == 7) {
            //更新回复数量
            initActionInfo(did);
        }
    }
}
