package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.SettingItem;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2019/1/31.
 */

public class SettingAdapter extends BaseQuickAdapter<SettingItem,BaseViewHolder> {
    public SettingAdapter(int layoutResId, @Nullable List<SettingItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingItem item) {
        helper.setText(R.id.settingName,item.getTitle());
        helper.setText(R.id.subSettingName,item.getSubTitle());
        helper.setImageResource(R.id.iconSetting,item.getIcon());
        if(item.getView() != null){
            LinearLayout linearLayout = helper.getView(R.id.viewSetting);
            linearLayout.addView(item.getView());
        }
    }
}
