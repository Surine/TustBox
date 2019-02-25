package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.NonRootDir;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2019/1/2.
 */

public class NonRootDirAdapter extends BaseQuickAdapter<NonRootDir,BaseViewHolder>{
    public NonRootDirAdapter(int layoutResId, @Nullable List<NonRootDir> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NonRootDir item) {
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.des,item.getPath());

        //配置图标
        if(item.getIs_dir().equals("true")){
            helper.setImageResource(R.id.icon,R.drawable.folder);
        }else{
            helper.setImageResource(R.id.icon,R.drawable.tag);
        }


        if(item.getIcon().equals("video")){
            helper.setImageResource(R.id.icon,R.drawable.movie);
        }else if(item.getIcon().equals("iso")){
            helper.setImageResource(R.id.icon,R.drawable.star);
        }else if(item.getIcon().equals("text")){
            helper.setImageResource(R.id.icon,R.drawable.text);
        }else if(item.getIcon().equals("audio")){
            helper.setImageResource(R.id.icon,R.drawable.music);
        }

    }
}
