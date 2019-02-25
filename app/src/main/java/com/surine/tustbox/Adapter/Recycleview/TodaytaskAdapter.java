package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Helper.Utils.TimeUtil;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2019/2/22.
 */

public class TodaytaskAdapter extends BaseQuickAdapter<Task,BaseViewHolder> {
    public TodaytaskAdapter(int layoutResId, @Nullable List<Task> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Task item) {
        helper.setText(R.id.taskName, item.getTask_name());
        helper.setText(R.id.taskRoom,item.getTask_postion()+" "+item.getTask_time());
        helper.addOnClickListener(R.id.taskCard);
    }
}
