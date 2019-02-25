package com.surine.tustbox.Adapter.Recycleview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Reply;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.R;

import java.util.List;

import static com.surine.tustbox.App.Data.Constants.PIC_CROP;

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
        helper.addOnClickListener(R.id.item_reply_head);

        //加载头像
        loadHead(helper,item);
        //加载名字
        loadName(helper,item);

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

    /**
     * 加载昵称
     * */
    private void loadName(BaseViewHolder helper, Reply item) {
        if (item.getUser() == null)
            return;

        //设置用户名
        if(item.getUser().getNick_name() != null)
            helper.setText(R.id.item_reply_username,item.getUser().getNick_name());

        if(item.getUser().getSchoolname() != null)
            helper.setText(R.id.item_reply_username,item.getUser().getSchoolname());

    }

    /**
     * 加载头像
     * */
    private void loadHead(BaseViewHolder helper, Reply item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext) ;
        if(item.getUser() == null)
            return;
        if(item.getUser().getFace() == null)
            return;

        if(prefs.getBoolean(FormData.PICCROP,true)){
            //不开启图片压缩
            faceUrl = item.getUser().getFace();
        }else{
            //开启图片压缩
            faceUrl = item.getUser().getFace()+ PIC_CROP;
        }
        Glide.with(mContext).load(faceUrl).crossFade().into((ImageView) helper.getView(R.id.item_reply_head));

    }
}
