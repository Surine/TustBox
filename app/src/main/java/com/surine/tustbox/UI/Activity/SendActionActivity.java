package com.surine.tustbox.UI.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.PermissionManager.TPermissionType;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.surine.tustbox.Adapter.Recycleview.ImageChooseAdapter;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.PhoenixUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Pojo.Subject;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Pojo.EventBusBean.SimpleEvent;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE;
import static com.surine.tustbox.App.Data.Constants.PICTURE_CHOOSE_LIST;
import static com.surine.tustbox.App.Data.Constants.staticTopicItems;
import static com.surine.tustbox.Helper.Utils.UploadQiniuService.buildFileUrl;

public class SendActionActivity extends TustBaseActivity {

    private static final String TAG = "SendActionActivity";
    @BindView(R.id.toolbar_send)
    Toolbar mToolbar;
    @BindView(R.id.takePicture)
    ImageView mImageView3;
    @BindView(R.id.rec_pic)
    RecyclerView mRecPic;
    @BindView(R.id.num_show)
    TextView mNumShow;
    @BindView(R.id.num_pic)
    TextView mNumPic;
    @BindView(R.id.edit)
    EditText mEdit;
    @BindView(R.id.send_button)
    ImageView mSendButton;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.topic_show)
    TextView topicShow;
    @BindView(R.id.relativeLayout)
    LinearLayout relativeLayout;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ImageChooseAdapter adapter;
    private List<String> data = new ArrayList<>();
    private List<String> dataFromQiniu = new ArrayList<>();
    int pic_size = 0;
    private int log_a;
    private String tokenFromServer = null;
    Configuration config = new Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();
    UploadManager uploadManager = new UploadManager(config);
    private int qiniupics = 0;  //for循环
    private String keyFromQiniu;
    private int picNumToQiniu = 0;  //发送到七牛服务器的图片数量
    private StringBuilder ids;
    private String Content;  //文本内容
    private Context context;
    private String topic = "#小天App#";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUI.setStatusBarUI(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_action);
        ButterKnife.bind(this);
        context = this;
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.send_status));
        mToolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);
        topic = getIntent().getStringExtra(FormData.messages_topic);

        topicShow.setText(topic);
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mNumShow.setText(s.length() + "");
            }
        });

        adapter = new ImageChooseAdapter(R.layout.item_pic, data);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecPic.setAdapter(adapter);
        mRecPic.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                data.remove(position);
                adapter.notifyItemRemoved(position);
                mNumPic.setText(data.size() + "");
                return false;
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, ImagePreViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PICTURE_CHOOSE_LIST, (Serializable) data);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });

        mEdit.setText(SharedPreferencesUtil.Read_safe(this, "EditTextContent", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    //set the back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            finish_save();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish_save();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finish_save() {
        //保存草稿
        if (!mEdit.getText().toString().equals("")) {
            SharedPreferencesUtil.Save_safe(SendActionActivity.this, "EditTextContent", mEdit.getText().toString());
            Toast.makeText(SendActionActivity.this, "已保存草稿！", Toast.LENGTH_SHORT).show();
        }
        //延时结束
        new Handler().postDelayed(new Runnable() {
            public void run() {
                finishWithAnimation();
            }
        }, 300);
    }



    private void finishWithAnimation() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.task_enter_anim,
                R.anim.task_exit_anim);
        finish();
    }

    @OnClick({R.id.toolbar_send, R.id.takePicture, R.id.send_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_send:
                break;
            case R.id.takePicture:
                 //最多选择12张
                choosePicture(12);
                break;
            case R.id.send_button:
                String type = SharedPreferencesUtil.Read_safe(context, FormData.USER_TYPE, "0");
                if (type.equals("3")) {
                    Toast.makeText(this, R.string.SendActionActivityAccountError, Toast.LENGTH_SHORT).show();
                } else {
                    picNumToQiniu = 0;
                    //准备发送
                    startGetToken();
                }
                break;
        }
    }

    /**
     * 选择照片
     * @param i 最大选择张数
     * */
    private void choosePicture(int i) {
        PhoenixUtil.with(12)
                .start(SendActionActivity.this, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE);
    }

    private void startGetToken() {
        if (data.size() == 0) {
            sendAction();
        } else {
            HttpUtil.get(UrlData.getToken).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSendButton.setEnabled(true);
                            Toast.makeText(SendActionActivity.this, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                tokenFromServer = jsonObject.getString("token");
                                if (tokenFromServer != null) {
                                    for (qiniupics = 0; qiniupics < data.size(); qiniupics++) {
                                        Log.d("TAF", data.get(qiniupics));
                                        //TODO：图片压缩
                                        //TODO：多线程发送
                                        //七牛上传服务
                                        uploadManager.put(new File(data.get(qiniupics)), buildFileUrl(context, data.get(qiniupics)), tokenFromServer,
                                                new UpCompletionHandler() {
                                                    @Override
                                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                                                        if (info.isOK()) {
                                                            Log.i("qiniu", "Upload Success");
                                                            try {
                                                                keyFromQiniu = res.getString("key");
                                                                dataFromQiniu.add(keyFromQiniu);
                                                                picNumToQiniu++;
                                                                ToastUtil.show("正在后台发送第"+picNumToQiniu+"张图片，可关闭当前页面");
                                                                //上传成功
                                                                if (picNumToQiniu == data.size()) {
                                                                    //发送
                                                                    sendAction();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            mSendButton.setEnabled(true);
                                                            Log.i("qiniu", "Upload Fail");
                                                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                                            Toast.makeText(SendActionActivity.this, R.string.SendActionActivityQiniuError, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, null);

                                    }

                                } else {
                                    Toast.makeText(SendActionActivity.this, R.string.SendActionActivityQiniuTokenError, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        }

    }


    private void sendAction() {
        Content = mEdit.getText().toString();
        ids = new StringBuilder();
        if (dataFromQiniu.size() >= 1) {
            for (String url : dataFromQiniu) {
                ids.append(url + ",");
            }
        } else {
            if (Content.equals("")) {
                Toast.makeText(this, R.string.SendActionActivityNullParam, Toast.LENGTH_SHORT).show();
                return;
            }
            ids.append("");
        }
        startSend();

    }

    public void startSend(){

        ToastUtil.show("图片上传完成，正在发送动态至小天服务器，可以手动退出本页面");

        String Token = SharedPreferencesUtil.Read_safe(SendActionActivity.this, "TOKEN", "");
        String Tust_number = SharedPreferencesUtil.Read(SendActionActivity.this, "tust_number", "000000");

        String ip = HttpUtil.getIPAddress(SendActionActivity.this);
        String device = Build.BRAND + "-" + Build.MODEL;


        FormBody formbody = new FormBody.Builder()
                .add(FormData.token, Token)
                .add(FormData.tust_number_server, Tust_number)
                .add(FormData.messages_info, Content)
                .add(FormData.messages_topic, "#小天App#")
                .add(FormData.messages_device, device)
                .add(FormData.pic_ids, ids.toString())
                .build();

        HttpUtil.post(UrlData.sendAction, formbody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSendButton.setEnabled(true);
                        Toast.makeText(SendActionActivity.this, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String x = response.body().string();
                LogUtil.d(x);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(x);
                            if (jsonObject.getInt("jcode") == 400) {
                                //发布成功
                                //成功之后清理pic_ids列表
                                ids = new StringBuilder();
                                dataFromQiniu.clear();
                                //清空草稿
                                SharedPreferencesUtil.Save_safe(SendActionActivity.this, "EditTextContent", "");
                                Toast.makeText(SendActionActivity.this, R.string.SendActionActivtySendSuccess, Toast.LENGTH_SHORT).show();
                                //通知更新
                                EventBus.getDefault().post(new SimpleEvent(1, "S"));
                                //结束活动
                                finish();
                            } else {
                                Toast.makeText(SendActionActivity.this, R.string.SendActionActivtySendError, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //显示话题
    private void showTopic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择话题");
        builder.setItems(staticTopicItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                topic = staticTopicItems[which];
                topicShow.setText(topic);
            }
        }).show();
    }


    //加载图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //返回的数据
            List<MediaEntity> result = Phoenix.result(data);
           updataRec(result);
        }
    }



    private void updataRec(List<MediaEntity> result) {
        pic_size = data.size() + result.size();
        if (pic_size > 13) {
            Toast.makeText(SendActionActivity.this, "您只可以再选择" + (12 - data.size()) + "张图片", Toast.LENGTH_SHORT).show();
        } else if (pic_size == 13) {
            Toast.makeText(SendActionActivity.this, "您只能选择12张图片", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < result.size(); i++) {
                data.add(result.get(i).getFinalPath());
            }
            mNumPic.setText(data.size() + "");
            adapter.notifyDataSetChanged();
           // mRecPic.smoothScrollToPosition(data.size() - 4);
        }
    }



}
