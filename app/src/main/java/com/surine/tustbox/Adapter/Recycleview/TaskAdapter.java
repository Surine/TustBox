package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.TimeUtil;

import java.util.List;

/**
 * Created by Surine on 2018/7/13.
 */

public class TaskAdapter extends BaseQuickAdapter<Task, BaseViewHolder> {
    private int i = 1;
    private String dateTemp = "";
    public TaskAdapter(int layoutResId, @Nullable List<Task> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Task item) {
        TextView day = helper.getView(R.id.day);
        TextView month = helper.getView(R.id.month);
        helper.setText(R.id.task_name, item.getTask_name());
        helper.setText(R.id.task_time, item.getTask_time());
        helper.setText(R.id.task_point,"@"+item.getTask_postion());

        //设置图标
        switch (item.getTask_type()){
            case -2:
                //课程
                helper.setImageResource(R.id.task_icon,R.drawable.ic_school_white_24dp);
                break;
            case 1:
                //考试
                helper.setImageResource(R.id.task_icon,R.drawable.ic_edit_white_24dp);
                break;
            case 2:
                //会议
                helper.setImageResource(R.id.task_icon,R.drawable.ic_group_white);
                break;
            case 3:
                //旅行
                helper.setImageResource(R.id.task_icon,R.drawable.trip_white);
                break;
            case 4:
                //琐事
                helper.setImageResource(R.id.task_icon,R.drawable.sun_white);
                break;
            case 5:
                //其他
                helper.setImageResource(R.id.task_icon,R.drawable.other_white_24dp);
                break;
        }

        helper.setImageResource(R.id.task_color,item.getTask_color());
        if(dateTemp.equals(TimeUtil.getDateByTimeString(item.getTask_time()))){
            day.setVisibility(View.GONE);
            month.setVisibility(View.GONE);
        }else{
            day.setVisibility(View.VISIBLE);
            day.setVisibility(View.VISIBLE);
            dateTemp = TimeUtil.getDateByTimeString(item.getTask_time());
            helper.setText(R.id.day, TimeUtil.getDayByTimeString(item.getTask_time()));
            helper.setText(R.id.month,TimeUtil.getMonthCC(item.getTask_time()));
        }
        dateTemp = TimeUtil.getDateByTimeString(item.getTask_time());
        i++;

        if(TimeUtil.compareDate(item.getTask_time(), TimeUtil.getDate(TimeUtil.yMdHm)) == -1){
            helper.setText(R.id.task_name, item.getTask_name()+"  [已结束]");
        }

    }
}
