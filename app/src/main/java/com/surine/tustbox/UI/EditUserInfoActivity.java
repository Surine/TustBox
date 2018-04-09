package com.surine.tustbox.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.surine.tustbox.Util.UploadQiniuService.buildFileHeadUrl;

public class EditUserInfoActivity extends TustBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_head)
    CircleImageView editHead;
    @BindView(R.id.edit_sign)
    TextView editSign;
    @BindView(R.id.change_nick_name)
    RelativeLayout changeNickName;
    @BindView(R.id.change_sex)
    RelativeLayout changeSex;
    @BindView(R.id.change_college)
    RelativeLayout changeCollege;
    private Context context;
    private TakePhoto takePhoto;
    public InvokeParam invokeParam;
    private String tokenFromServer;
    Configuration config = new Configuration.Builder()
            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();
    UploadManager uploadManager = new UploadManager(config);
    private String keyFromQiniu;
    private String buildUrl;
    private int yourChoice;
    final String[] str = new String[]{
            "男","女","保密"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);
        context = this;
        Glide.with(this).load(getIntent().getStringExtra(FormData.face_server)).placeholder(R.drawable.school_shape).into(editHead);
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("动态详情");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    @OnClick({R.id.edit_head, R.id.edit_sign,R.id.change_nick_name, R.id.change_sex, R.id.change_college})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_head:
                //相册
                takePhoto = getTakePhoto();
                TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
                builder.setWithOwnGallery(true);
                takePhoto.setTakePhotoOptions(builder.create());
                Config_compress(takePhoto);
                takePhoto.onPickFromDocumentsWithCrop(Config_the_file(), getCropOptions());
                break;
            case R.id.edit_sign:
                showEditDialog(1,"修改您的个性签名");
                break;
            case R.id.change_nick_name:
                showEditDialog(2,"修改您的昵称");
                break;
            case R.id.change_sex:
                changeSex();
                break;
            case R.id.change_college:
                showEditDialog(3,"修改您的学院信息");
                break;
        }
    }

    private void changeSex() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改性别");
        yourChoice = 0;
        //default selection
        builder.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yourChoice = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (yourChoice != -1) {
                     changeSexToServer(str[yourChoice]);
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void changeSexToServer(String s) {
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String token = SharedPreferencesUtil.Read_safe(context, FormData.token, "");
        buildUrl = UrlData.setUserSex + "?" + FormData.token + "=" + token + "&" + FormData.uid + "=" + tust_number + "&" + FormData.sex + "=" + s;

        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG",x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.EditUserInfoActivityUpdateSuccess, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "无需修改！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Config_compress(TakePhoto takePhoto) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight() - 140;
        int maxSize = 409600;
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

    private CropOptions getCropOptions() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = 200;
        int height = 200;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    private Uri Config_the_file() {
        File file = new File(Environment.getExternalStorageDirectory(), "/head/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
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

    private void showEditDialog(final int i, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_view_editview, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_view);

        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送
                if (!editText.getText().equals("")) {
                   changeSignToServer(editText.getText().toString(),i);
                } else {
                    Toast.makeText(context, R.string.SendActionActivityNullParam, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void changeSignToServer(String s, int i) {
        if(s.equals("")){
            return;
        }
        if(i == 1){
            if(s.length() > 25){
                return;
            }
        }else if(i == 2){
            if(s.length() > 8){
                return;
            }
        }else if(i == 3){
            if(s.length() > 20){
                return;
            }
        }
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String token = SharedPreferencesUtil.Read_safe(context, FormData.token, "");

        if(i == 1){
            buildUrl = UrlData.setUserSign + "?" + FormData.token + "=" + token + "&" + FormData.uid + "=" + tust_number + "&" + FormData.sign_server + "=" + s;
        }else if(i == 2){
            buildUrl = UrlData.setUserName + "?" + FormData.token + "=" + token + "&" + FormData.uid + "=" + tust_number + "&" + FormData.school_name_server + "=" + s;
        }else if(i == 3){
            buildUrl = UrlData.setUserCollege + "?" + FormData.token + "=" + token + "&" + FormData.uid + "=" + tust_number + "&" + FormData.college_server + "=" + s;
        }

        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG",x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.EditUserInfoActivityUpdateSuccess, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.EditUserInfoActivityUpdateFail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        //TODO获取成功
        Log.d("show_url", "showImg: " + images.get(0).getOriginalPath());
        Glide.with(this).load(new File(images.get(0).getOriginalPath())).into(editHead);
        updateHead(images.get(0).getOriginalPath());
    }

    private void updateHead(final String originalPath) {
        HttpUtil.get(UrlData.getToken).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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

                                //七牛上传服务
                                uploadManager.put(new File(originalPath), buildFileHeadUrl(context, originalPath), tokenFromServer,
                                        new UpCompletionHandler() {
                                            @Override
                                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                                if (info.isOK()) {
                                                    Log.i("qiniu", "Upload Success");
                                                    try {
                                                        keyFromQiniu = res.getString("key");
                                                        if (keyFromQiniu != null) {
                                                            sendHeadUrl(keyFromQiniu);
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Toast.makeText(EditUserInfoActivity.this, R.string.SendActionActivityQiniuError, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, null);

                            } else {
                                Toast.makeText(EditUserInfoActivity.this, R.string.SendActionActivityQiniuTokenError, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    //更新url
    private void sendHeadUrl(String keyFromQiniu) {
        String tust_number = SharedPreferencesUtil.Read(context, FormData.tust_number_server, "");
        String token = SharedPreferencesUtil.Read_safe(context, FormData.token, "");
        String buildUrl = UrlData.setUserFace + "?" + FormData.token + "=" + token + "&" + FormData.uid + "=" + tust_number + "&" + FormData.face_server + "=" + keyFromQiniu;
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG", x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.EditUserInfoActivityUpdateSuccess, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, R.string.EditUserInfoActivityUpdateFail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

}
