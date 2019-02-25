package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Love;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/2/23.
 */

public class LoveAdapter extends BaseQuickAdapter<Love, BaseViewHolder> {
    public LoveAdapter(int layoutResId, @Nullable List<Love> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Love item) {
        helper.addOnClickListener(R.id.item_love_head);
        //加载头像
        if(item.getUser()!=null && item.getUser().getFace()!=null){
            Glide.with(mContext).load(item.getUser().getFace()).crossFade().into((ImageView) helper.getView(R.id.item_love_head));
        }

        //加载昵称
        if(item.getUser()!=null){
            if(item.getUser().getSchoolname() == null||item.getUser().getSchoolname().equals("")){
                if(item.getUser().getNick_name() == null){
                    helper.setText(R.id.item_love_username,"未设置");
                }else{
                    helper.setText(R.id.item_love_username,item.getUser().getNick_name());
                }
            }else{
                helper.setText(R.id.item_love_username,item.getUser().getSchoolname());
            }
        }
    }
}

