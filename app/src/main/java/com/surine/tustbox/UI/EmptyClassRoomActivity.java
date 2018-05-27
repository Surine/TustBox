package com.surine.tustbox.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by Surine on 2018/5/17.
 */

public class EmptyClassRoomActivity extends TustBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.timg)
    ImageView timg;
    @BindView(R.id.cs_college)
    TextView csCollege;
    @BindView(R.id.csweek)
    TextView csweek;
    @BindView(R.id.csday)
    TextView csday;
    @BindView(R.id.csnumber)
    TextView csnumber;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.button12)
    Button button12;
    @BindView(R.id.empty_class_room_title)
    TextView emptyClassRoomTitle;
    private int eggTemp = 1;
    private int csCollegeNumber = 0;
    private int csDayNumber = 0;
    private int csweekNumber = 0;
    private int csnumberNumber = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_classroom);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.empty_class_room));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @OnClick({R.id.timg, R.id.cs_college, R.id.csweek, R.id.csday, R.id.csnumber, R.id.checkBox, R.id.button12})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timg:
                egg();
                break;
            case R.id.cs_college:
                chooseCollege();
                break;
            case R.id.csweek:
                chooseWeek();
                break;
            case R.id.csday:
                chooseCsDay();
                break;
            case R.id.csnumber:
                chooseCsNumber();
                break;
            case R.id.checkBox:
                getPreValue();
                break;
            case R.id.button12:
                request();
                break;
        }
    }

    private void ui() {
        progressDialog = ProgressDialog.show(EmptyClassRoomActivity.this,"提示","正在连接服务器……");
    }

    //发起请求
    private void request() {
        if(csnumberNumber == 0 || csDayNumber == 0||csweekNumber == 0||csCollegeNumber == 0){
            Snackbar.make(button12,R.string.para_null,Snackbar.LENGTH_SHORT).show();
            return;
        }
        ui();
        String url = UrlData.getEmptyClassRoomInfo+"?s1="+csCollegeNumber+"&s2="+csweekNumber+"&s3="+csDayNumber+"&s4="+csnumberNumber;
        HttpUtil.get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Snackbar.make(button12, R.string.no_net_or_permission,Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String re = response.body().string();
                Log.d("TAG",re);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Intent intent = new Intent(EmptyClassRoomActivity.this,WebViewActivity.class);
                        intent.putExtra("title",getResources().getString(R.string.empty_class_room));
                        intent.putExtra("url",re);
                        intent.putExtra("flag",1); //加载网页源码
                        startActivity(intent);
                    }
                });
            }
        });
    }

    //智能预测
    private void getPreValue() {
        //如果开关打开
        if(checkBox.isChecked()){
            //进行简单的预测
            //获取校区（这里可以通过获取校区字段，只不过我找不到哪个是校区了）
            //于是用来一节课的上课地点来判断
            String school = SharedPreferencesUtil.Read(EmptyClassRoomActivity.this,"15","");
            if(!school.equals("")){
                if(school.contains("西")){
                    csCollege.setText("泰达西苑");
                    csCollegeNumber = 3;
                }else if(school.contains("泰达")&&!school.contains("西")){
                    csCollege.setText("泰达中苑");
                    csCollegeNumber = 2;
                }else if(school.contains("河西")){
                    csCollege.setText("河西校区");
                    csCollegeNumber = 1;
                }
            }

            //教学周推测
            int week = SharedPreferencesUtil.Read(EmptyClassRoomActivity.this,"choice_week",1);
            csweek.setText(MainActivity.str[week - 1]);
            csweekNumber = week;

            //星期推测
            String dayNumber = TimeUtil.GetWeekNumber();
            csday.setText(TimeUtil.GetWeek());
            csDayNumber = Integer.parseInt(dayNumber);

            //课程推测
            if(TimeUtil.GetHour() >= 12){
                csnumberNumber = 3;
                csnumber.setText("第三大节");
            }else{
                csnumberNumber = 2;
                csnumber.setText("第二大节");
            }
        }
    }

    //选择第几节课
    private void chooseCsNumber() {
        final CharSequence[] charSequences = {"第一大节","第二大节","第三大节","第四大节","第五大节","第六大节"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EmptyClassRoomActivity.this);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                csnumber.setText(charSequences[which]);
                csnumberNumber = which + 1;
            }
        }).show();
    }

    //选择周几
    private void chooseCsDay() {
        final CharSequence[] charSequences = {"周一","周二","周三","周四","周五","周六","周日"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EmptyClassRoomActivity.this);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                csday.setText(charSequences[which]);
                csDayNumber = which + 1;
            }
        }).show();
    }

    //选择校区
    private void chooseCollege() {
        final CharSequence[] charSequences = {"河西校区","泰达中苑","泰达西苑"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EmptyClassRoomActivity.this);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                csCollege.setText(charSequences[which]);
                csCollegeNumber = which + 1;
            }
        }).show();
    }

    //选择周
    private void chooseWeek() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmptyClassRoomActivity.this);
        builder.setItems(MainActivity.str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                csweek.setText(MainActivity.str[which]);
                csweekNumber = which + 1;
            }
        }).show();
    }

    private void egg() {
       // timg.animate().scaleX(1).setDuration(200).start();
        switch (eggTemp){
            case 1:
                emptyClassRoomTitle.setText("别戳我，\n快自习去！");
                break;
            case 2:
                emptyClassRoomTitle.setText("别戳啦!\n让人脑袋大！");
                timg.animate().scaleX(2).setDuration(200).start();
                break;
            case 3:
                emptyClassRoomTitle.setText("再戳我，\n我闪退了！哼");
                timg.animate().scaleX(1).alpha(0.5f).setDuration(200).start();
                break;
            case 4:
                emptyClassRoomTitle.setText("我真的，\n搞破坏了！");
                setTitle("本宝宝生气了！");
                button12.animate().rotation(360f).setDuration(200).start();
                break;
            case 5:
                emptyClassRoomTitle.setText("好好好，\n还欺负我！");
                button12.setClickable(false);
                button12.animate().rotation(0f).setDuration(200).start();
                button12.setText("屏蔽按钮，看你怎么办！");
                break;
            case 6:
                emptyClassRoomTitle.setText("我错了,大佬们！\n  ...╥﹏╥...");
                timg.animate().alpha(1f).setDuration(200).start();
                setTitle(getResources().getString(R.string.empty_class_room));
                button12.setClickable(true);
                button12.setText("您是老大！您继续……");
                eggTemp = 0;
                break;
        }
        eggTemp++;
    }
}
