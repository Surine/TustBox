package com.surine.tustbox.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.PermissionManager.TPermissionType;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.surine.tustbox.Adapter.Recycleview.ImageChooseAdapter;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActionActivity extends TustBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private static final String TAG = "SendActionActivity";
    @BindView(R.id.toolbar_send)
    Toolbar mToolbar;
    @BindView(R.id.topic)
    ImageView mTopic;
    @BindView(R.id.imageView3)
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
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ImageChooseAdapter adapter;
    private List<String> data = new ArrayList<>();
    int pic_size = 0;
    private int log_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_action);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        //设置toolbar颜色
       // SystemUI.color_toolbar(this,getSupportActionBar());
        setTitle(getString(R.string.send_status));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                Toast.makeText(SendActionActivity.this, "暂不支持预览，长按可以删除", Toast.LENGTH_SHORT).show();
            }
        });

        mEdit.setText(SharedPreferencesUtil.Read_safe(this,"EditTextContent",""));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish_save();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(keyCode == KeyEvent.KEYCODE_BACK){
           finish_save();
       }
       return super.onKeyDown(keyCode, event);
    }

    private void finish_save() {
        //保存草稿
        if(!mEdit.getText().toString().equals("")){
            SharedPreferencesUtil.Save_safe(SendActionActivity.this,"EditTextContent",mEdit.getText().toString());
            Toast.makeText(SendActionActivity.this, "已保存草稿！", Toast.LENGTH_SHORT).show();
        }
        //延时结束
        new Handler().postDelayed(new Runnable(){
            public void run() {
               finish();
            }
        }, 300);
    }

    @OnClick({R.id.toolbar_send, R.id.topic, R.id.imageView3,R.id.send_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_send:
                break;
            case R.id.topic:
                //话题
                Toast.makeText(SendActionActivity.this, "开发小哥哥正在加班加点研发！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageView3:
                //相册
                takePhoto = getTakePhoto();
                TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
                builder.setWithOwnGallery(true);
                takePhoto.setTakePhotoOptions(builder.create());
                takePhoto.onPickMultiple(12);
                break;
            case R.id.send_button:
                //准备发送

                if(!mEdit.getText().toString().equals("")){
                    log_a = Send();
                }

                switch (log_a){
                    case 200:
                       //发送成功
                        Toast.makeText(SendActionActivity.this, getString(R.string.send_success), Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        //无网络
                        Toast.makeText(SendActionActivity.this, getString(R.string.network_no_connect), Toast.LENGTH_SHORT).show();
                        break;
                    case 401:
                        Toast.makeText(SendActionActivity.this, getString(R.string.para_null), Toast.LENGTH_SHORT).show();
                        break;
                    case 505:break;
                }
                break;
        }
    }

    private int Send() {
        /**
         * 准备参数
         * @parm 心情内容
         * @parm 图片数据集
         * @parm IP
         * @parm TOKEN
         * @parm Tust_number
         * @parm Topic
         * */
         int code = 0;
         String Token = SharedPreferencesUtil.Read_safe(SendActionActivity.this,"TOKEN","");
         String Tust_number = SharedPreferencesUtil.Read(SendActionActivity.this,"tust_number","000000");
         //TODO:默认话题
         String topic = "TUSTBOX_MOOD";
         String mo_content = mEdit.getText().toString();
         String ip = HttpUtil.getIPAddress(SendActionActivity.this);
         if(ip == null){
             //无网络
             code = 400;
         }else{
             if(mo_content.equals("")||Token.equals("")||Tust_number.equals("")){
                 code  = 401;
             }else{
                // HttpUtil.post_file()
             }
         }
         return code;
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            //创建实例
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    //获取成功回掉
    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImages().get(0).getOriginalPath());
        updataRec(result);
    }

    private void updataRec(TResult result) {
        pic_size = data.size() + result.getImages().size();
        if (pic_size > 13) {
            Toast.makeText(SendActionActivity.this, "您只可以再选择" + (12 - data.size()) + "张图片", Toast.LENGTH_SHORT).show();
        } else if (pic_size == 13) {
            Toast.makeText(SendActionActivity.this, "您只能选择12张图片", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < result.getImages().size(); i++) {
                data.add(result.getImages().get(i).getOriginalPath());
            }
            mNumPic.setText(data.size() + "");
            adapter.notifyDataSetChanged();
            mRecPic.smoothScrollToPosition(data.size() - 4);
        }
    }

    //获取失败
    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    //取消
    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }


    //数据恢复
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    //数据接收
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public TPermissionType invoke(InvokeParam invokeParam) {
        TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

}
