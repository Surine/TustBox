package com.surine.tustbox.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Helper.Dao.TaskDao;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.Pojo.EventBusBean.TaskCURD;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskInfoActivity extends TustBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.taskInfoBack)
    RelativeLayout taskInfoBack;
    @BindView(R.id.taskInfoImage)
    ImageView taskInfoImage;
    @BindView(R.id.task_info_name)
    TextView taskInfoName;
    @BindView(R.id.task_info_time)
    TextView taskInfoTime;
    @BindView(R.id.task_info_position)
    TextView taskInfoPosition;
    private Context context;
    private int taskId;
    private Task mTask;
    private int taskPostion;
    private TaskDao taskDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_task_info);
        ButterKnife.bind(this);
        context = this;
        taskDao = new TaskDao();
        setSupportActionBar(toolbar);
        setTitle("详情");
        toolbar.setBackgroundColor(getResources().getColor(R.color.trans_white));
        toolbar.setTitleTextAppearance(context, R.style.ToolbarTitleWhite);
        taskId = getIntent().getIntExtra(Constants.TASK_ID, -1);
        taskPostion = getIntent().getIntExtra(Constants.TASK_POSITION, -1);
        if (taskId == -1 || taskPostion == -1) {
            Toast.makeText(context, R.string.pleasetryrestartapp, Toast.LENGTH_SHORT).show();
            finish();
        }
        mTask = taskDao.select(taskId);
        taskInfoImage.setBackgroundResource(mTask.getTask_color());
        if(mTask.getTask_name() != null){
            taskInfoName.setText(mTask.getTask_name());
        }
        if(mTask.getTask_time() != null){
            if(TimeUtil.getTimeSubString(mTask.getTask_time()) == null){
                taskInfoTime.setText(mTask.getTask_time());
            }else{
                taskInfoTime.setText(TimeUtil.getTimeSubString(mTask.getTask_time()));
            }
        }
        if(mTask.getTask_postion() != null){
            taskInfoPosition.setText(mTask.getTask_postion());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.edit:
                Intent intent = new Intent(TaskInfoActivity.this,AddTaskActivity.class);
                intent.putExtra(Constants.TASK_ID,taskId);
                intent.putExtra(Constants.TASK_POSITION,taskPostion);
                startActivity(intent);
                finish();
                break;
            case R.id.delete:
                showDeleteDialog();
                break;
        }
        return true;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_a_text_notice,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView message = (TextView) view.findViewById(R.id.message);

        label.setText("是否删除？");
        message.setText("请谨慎操作，删除之后将不可恢复！");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskDao.delete(taskId) > 0){
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new TaskCURD(1,TaskCURD.DELETE,taskPostion));
                    finish();
                }else{
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }
}
