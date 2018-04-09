package com.surine.tustbox.Adapter.Recycleview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.Comment;
import com.surine.tustbox.Bean.Reply;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.R;

import java.util.List;

import static com.surine.tustbox.Data.Constants.PIC_CROP;

/**
 * Created by Surine on 2018/2/24.
 */

public class ReplyAdapter extends BaseQuickAdapter<Reply, BaseViewHolder> {
    private String faceUrl;

    public ReplyAdapter(int layoutResId, @Nullable List<Reply> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Reply item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext) ;

        helper.addOnClickListener(R.id.item_reply_head);
        //加载头像
        if(item.getUser()!=null && item.getUser().getFace()!=null){
            if(prefs.getBoolean(FormData.PICCROP,true)){
                //不开启图片压缩
                faceUrl = item.getUser().getFace();
            }else{
                //开启图片压缩
                faceUrl = item.getUser().getFace()+ PIC_CROP;
            }
            Glide.with(mContext).load(faceUrl).crossFade().into((ImageView) helper.getView(R.id.item_reply_head));
        }

        //加载昵称
        if(item.getUser()!=null){
            if(item.getUser().getSchoolname() == null||item.getUser().getSchoolname().equals("")){
                if(item.getUser().getNick_name() == null){
                    helper.setText(R.id.item_reply_username,"未设置");
                }else{
                    helper.setText(R.id.item_reply_username,item.getUser().getNick_name());
                }
            }else{
                helper.setText(R.id.item_reply_username,item.getUser().getSchoolname());
            }
        }

        //加载评论时间
        if(item.getSay_time()!=null){
            helper.setText(R.id.item_reply_time,item.getSay_time());
        }
        //加载评论内容
        if(item.getSay_info()!=null){
            helper.setText(R.id.comment_info,item.getSay_info());
        }
        //加载评论内容
        if(!item.getSay_at_name().equals("")){
            helper.setText(R.id.item_say_at,"@"+item.getSay_at_name());
        }else{
            helper.setText(R.id.item_say_at,"");
        }

    }
}
