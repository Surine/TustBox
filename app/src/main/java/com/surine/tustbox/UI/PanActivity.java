package com.surine.tustbox.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class PanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan);

        String ps = SharedPreferencesUtil.Read_safe(PanActivity.this,"PSWD_PAN", "1111111");
        String tm = SharedPreferencesUtil.Read(PanActivity.this,"NUMBER_PAN","11111111");
        if(ps.equals("1111111")||tm.equals("11111111")){
            //设置登录对话框
            ShowLoginDialog();
        }else{
            //登录云盘
            LogPan(ps, tm);
        }

    }


    private void ShowLoginDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_view_login_work_view,null);
        final EditText number = (EditText) view.findViewById(R.id.tust_number);
        final EditText pswd = (EditText) view.findViewById(R.id.network_passwd);
        pswd.setHint("云盘密码");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(number.getText().toString().equals("")||pswd.getText().toString().equals("")){
                    Toast.makeText(PanActivity.this, R.string.null_input, Toast.LENGTH_SHORT).show();
                    ShowLoginDialog();
                }else{
                    //储存已经登陆的数据
                    SharedPreferencesUtil.Save_safe(PanActivity.this,"PSWD_PAN", pswd.getText().toString());
                    SharedPreferencesUtil.Save(PanActivity.this,"NUMBER_PAN",number.getText().toString());

                    LogPan(pswd.getText().toString(),number.getText().toString());
                }
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    private void LogPan(String ps, String tn) {
        //请求参数
        FormBody formBody = new FormBody.Builder()
                .add(FormData.link_device, "web")
                .add(FormData.link_name, "web")
                .add(FormData.username, tn)
                .add(FormData.password, ps)
                .add(FormData.locale, ps)
                .build();
        //构造url
        String pan_login_url = UrlData.pan_login+"username="+tn +"&password="+ EncryptionUtil.url_en(ps);
        Log.d("TEST",pan_login_url);
        HttpUtil.post(UrlData.pan_login,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PanActivity.this, R.string.get_data_use_fail,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("TEST",s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                  //获取数据失败
                     Toast.makeText(PanActivity.this,"数据获取成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
