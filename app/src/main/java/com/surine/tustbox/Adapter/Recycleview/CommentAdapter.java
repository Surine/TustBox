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
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.R;

import java.util.List;

import static com.surine.tustbox.Data.Constants.HTTP;
import static com.surine.tustbox.Data.Constants.PIC_CROP;

/**
 * Created by Surine on 2018/2/23.
 */

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    private String faceUrl;

    public CommentAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext) ;

        helper.addOnClickListener(R.id.item_comment_head);
        //加载头像
        if(item.getUser()!=null && item.getUser().getFace()!=null){
            if(prefs.getBoolean(FormData.PICCROP,true)){
                //不开启图片压缩
                faceUrl = item.getUser().getFace();
            }else{
                //开启图片压缩
                faceUrl = item.getUser().getFace()+ PIC_CROP;
            }
            Glide.with(mContext).load(faceUrl).crossFade().into((ImageView) helper.getView(R.id.item_comment_head));
        }

        //加载昵称
        if(item.getUser()!=null){
            if(item.getUser().getSchoolname() == null||item.getUser().getSchoolname().equals("")){
                if(item.getUser().getNick_name() == null){
                    helper.setText(R.id.item_comment_username,"未设置");
                }else{
                    helper.setText(R.id.item_comment_username,item.getUser().getNick_name());
                }
            }else{
                helper.setText(R.id.item_comment_username,item.getUser().getSchoolname());
            }
        }
        //加载评论时间
        if(item.getComment_time()!=null){
            helper.setText(R.id.item_comment_time,item.getComment_time());
        }
        //加载评论内容
        if(item.getComment_content()!=null){
            helper.setText(R.id.comment_info,item.getComment_content());
        }
        //加载评论
        if(item.getComment_say_num() != 0){
            TextView textView = (TextView) helper.getView(R.id.item_comment_reply_num);
            textView.setVisibility(View.VISIBLE);
            helper.setText(R.id.item_comment_reply_num,"查看 "+item.getComment_say_num()+" 条回复");
        }else{
            TextView textView = (TextView) helper.getView(R.id.item_comment_reply_num);
            textView.setVisibility(View.GONE);
        }
    }
}
