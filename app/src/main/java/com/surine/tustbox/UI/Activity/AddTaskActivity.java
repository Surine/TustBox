package com.surine.tustbox.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.DialogColorAdapter;
import com.surine.tustbox.Helper.Dao.TaskDao;
import com.surine.tustbox.Pojo.EventBusBean.TaskCURD;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.UserUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends TustBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainBack)
    RelativeLayout mainBack;
    @BindView(R.id.task_name_edit)
    EditText taskNameEdit;
    @BindView(R.id.task_point_edit)
    EditText taskPointEdit;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.time)
    ImageView time;
    @BindView(R.id.tagImage)
    ImageView tagImage;
    @BindView(R.id.color)
    ImageView color;
    @BindView(R.id.more)
    ImageView more;

    private String action = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final int EXAM = 1;
    public static final int MEETING = 2;
    public static final int TRIP = 3;
    public static final int DAILY = 4;
    public static final int OTHER = 5;
    private final String[] tagNameForTask = Constants.tagNameForTask;  //使用默认标签分类
    private final int[] colorsFromConstants = Constants.coolapkColor;  //使用酷安色系

    private Context context;
    private List<Integer> colors = new ArrayList<>();

    private int mTag = 4;   //默认是日常
    private String dateData  = null;  //日期
    private String timeData = null;   //时间
    private int mColor = 0;   //颜色
    private boolean isShare = false;   //是否分享

    private int taskId = -1;
    private Task mTask;
    private Task task;
    private int taskPosition = -1;

    private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_add_task);
        context = this;
        taskDao = new TaskDao();
        ButterKnife.bind(this);
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitle("添加任务");
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitle);

        //编辑功能
        taskId = getIntent().getIntExtra(Constants.TASK_ID, -1);
        taskPosition = getIntent().getIntExtra(Constants.TASK_POSITION,-1);
        if(taskId != -1 && taskPosition != -1){
            mTask = DataSupport.find(Task.class, taskId);
            taskNameEdit.setText(mTask.getTask_name());
            taskPointEdit.setText(mTask.getTask_postion());
            mColor = mTask.getTask_color();
            timeData = mTask.getTask_time();
            mTag = mTask.getTask_type();
            isShare = mTask.isIs_Share();
        }

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
            finishWithAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finishWithAnimation();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishWithAnimation() {
        Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.task_enter_anim,
                R.anim.task_exit_anim);
        finish();
    }


    @OnClick({R.id.time, R.id.tagImage, R.id.color, R.id.more,R.id.reset,R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time:
                //显示时间对话框
                showDateDialog();
                break;
            case R.id.tagImage:
                //显示标签对话框
                showTagDialog();
                break;
            case R.id.color:
                //显示颜色
                showColorDialog();
                break;
            case R.id.more:
                //显示更多对话框
                showMoreDialog();
                break;
            case R.id.ok:
                //空输入
                if(taskNameEdit.getText().toString().equals("")||taskPointEdit.getText().toString().equals("")){
                    return;
                }
                //检查时间
                if(timeData == null || dateData == null || timeData.equals("") || dateData.equals("")){
                    Snackbar.make(toolbar, R.string.task_please_check_time,Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //颜色随机
                if(mColor == 0){
                    try {
                        mColor = colorsFromConstants[new Random().nextInt(10)];
                    } catch (Exception e) {
                        mColor = colorsFromConstants[0];
                    }
                }
                //显示确认对话框
                showOkdialog();
                break;
            case R.id.reset:
                mTag = 4;   //默认是日常
                dateData  = null;  //日期
                timeData = null;   //时间
                int mColor = 0;   //颜色
                isShare = false;   //是否分享
                taskPointEdit.setText("");
                taskNameEdit.setText("");
                break;
        }
    }

    //显示确认对话框
    private void showOkdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_ok,null);
        builder.setView(view);

        TextView textView = (TextView) view.findViewById(R.id.intent);
        final Button tag = (Button) view.findViewById(R.id.checkTag);
        tag.setText(tagNameForTask[mTag - 1]); //避免溢出，所以要减一
        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        String intent = "Error";
        if(isShare){
            intent = dateData+" "+timeData+"在"+taskPointEdit.getText().toString()+"做："+taskNameEdit.getText().toString()+"(确认分享)";
        }else{
            intent = dateData+" "+timeData+"在"+taskPointEdit.getText().toString()+"做："+taskNameEdit.getText().toString();
        }
        textView.setText(intent);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();

               //准备添加
               if(taskId == -1){
                    task = new Task();
               }else{
                   task = mTask;
               }
               task.setTask_color(mColor);  //设置背景色
               task.setTask_time(dateData+" "+timeData);  //时间
               task.setTask_postion(taskPointEdit.getText().toString()); //设置地点
               task.setTask_name(taskNameEdit.getText().toString());  //设置名称
               task.setIs_Share(isShare);  //是否分享
               task.setState(0);
               task.setTask_is_important(false);   //是否重要
               task.setTask_type(mTag);  //设置标签
               task.setTask_tust_number(UserUtil.getTustNumber(context));  //设置用户名
               task.setCreate_time(String.valueOf(System.currentTimeMillis()));
               if(taskId == -1){
                   if(taskDao.add(task)){
                       Toast.makeText(AddTaskActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                       int message = DataSupport.findLast(Task.class).getId();
                       EventBus.getDefault().post(new TaskCURD(1,TaskCURD.ADD,message));  //无message传递
                       updateWidget();
                  }else{
                       Toast.makeText(AddTaskActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                   }
                   finish();
               }else{
                   if(taskDao.update(task)){
                       Toast.makeText(AddTaskActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                       EventBus.getDefault().post(new TaskCURD(1,TaskCURD.MODIFY,taskPosition));  //无message传递
                       updateWidget();
                   }else{
                       Toast.makeText(AddTaskActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                   }
                   finish();
               }


            }
        });

    }

    private void updateWidget() {
        //发送桌面小部件广播
        Intent updateIntent = new Intent(action);
        sendBroadcast(updateIntent);
    }

    //显示时间对话框
    private void showDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_date,null);
        builder.setView(view);

        final AlertDialog dateDialog = builder.create();
        dateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dateDialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthTemp;
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                if(month < 10){
                    monthTemp = "0"+month;
                }else{
                    monthTemp = String.valueOf(month);
                }
                String dayTemp;
                int day = datePicker.getDayOfMonth();
                if(day < 10){
                    dayTemp = "0"+day;
                }else{
                    dayTemp = String.valueOf(day);
                }
                dateData = year+"-"+monthTemp+"-"+dayTemp;
                dateDialog.dismiss();
                //配置日期
                //显示时间对话框
                 showTimeDialog();
            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });
    }

    //显示时间对话框
    private void showTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_time,null);
        builder.setView(view);

        final AlertDialog timeDialog = builder.create();
        timeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timeDialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //配置时间
                String hourTemp;
                String minTemp;
                int hour = timePicker.getCurrentHour();
                if(hour < 10){
                    hourTemp = "0"+hour;
                }else{
                    hourTemp = String.valueOf(hour);
                }
                int min = timePicker.getCurrentMinute();
                if(min < 10){
                    minTemp = "0"+min;
                }else{
                    minTemp = String.valueOf(min);
                }
                timeData = hourTemp+":"+minTemp;
                //配置日期
                Snackbar.make(toolbar,"您选择了："+dateData+" "+timeData,Snackbar.LENGTH_SHORT).show();
                timeDialog.dismiss();
            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.dismiss();
            }
        });

    }

    //显示颜色对话框
    private void showColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_color,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        colors.clear();
        //加载颜色列表，（搞Arrays.asList没搞好……）
        for(int i = 0;i < colorsFromConstants.length;i++){
            colors.add(colorsFromConstants[i]);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.colorList);
        recyclerView.setLayoutManager(new GridLayoutManager(AddTaskActivity.this,4));

        DialogColorAdapter dialogColorAdapter = new DialogColorAdapter(R.layout.item_task_color_dialog,colors);
        recyclerView.setAdapter(dialogColorAdapter);
        dialogColorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //选择颜色
                dialog.dismiss();
                Snackbar.make(toolbar,"您选择了："+Constants.coolapkColorName[position]+"(From CoolApk)",Snackbar.LENGTH_SHORT).show();
                mColor = colorsFromConstants[position];  //颜色赋值
            }
        });

    }

    //显示标签对话框
    private void showTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_tag,null);
        builder.setView(view);
        LinearLayout exam = (LinearLayout) view.findViewById(R.id.examTouch);
        LinearLayout buss = (LinearLayout) view.findViewById(R.id.bussTouch);
        LinearLayout trip = (LinearLayout) view.findViewById(R.id.tripTouch);
        LinearLayout things = (LinearLayout) view.findViewById(R.id.thingTouch);
        LinearLayout other = (LinearLayout) view.findViewById(R.id.otherTouch);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag = EXAM;
                Snackbar.make(toolbar,"您选择了："+tagNameForTask[mTag-1],Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        buss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag = MEETING;  //会议
                Snackbar.make(toolbar,"您选择了："+tagNameForTask[mTag-1],Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag = TRIP;    //旅行
                Snackbar.make(toolbar,"您选择了："+tagNameForTask[mTag-1],Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        things.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag = DAILY;  //日常
                Snackbar.make(toolbar,"您选择了："+tagNameForTask[mTag-1],Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag = OTHER;     //其他
                Snackbar.make(toolbar,"您选择了："+tagNameForTask[mTag-1],Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    //展示更多
    private void showMoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_task_more,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.shareTask);
        checkBox.setChecked(isShare);
        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    //确认分享
                    isShare = true;
                    Snackbar.make(toolbar, "您已确认分享",Snackbar.LENGTH_SHORT).show();
                }else{
                    //取消分享
                    isShare = false;
                    Snackbar.make(toolbar, "您已取消分享",Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }
}
