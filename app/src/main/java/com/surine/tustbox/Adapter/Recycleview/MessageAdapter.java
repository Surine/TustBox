package com.surine.tustbox.Adapter.Recycleview;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Message;
import com.surine.tustbox.R;

import java.util.List;

import static com.surine.tustbox.App.Data.Constants.COMMENT_ACTION;
import static com.surine.tustbox.App.Data.Constants.LOVE_ACTION;
import static com.surine.tustbox.App.Data.Constants.REPLY_AT_SOMEONE;
import static com.surine.tustbox.App.Data.Constants.REPLY_COMMENT;

/**
 * Created by Surine on 2018/2/25.
 */

public class MessageAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable List<Message> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        helper.addOnClickListener(R.id.item_message_head);
        //加载头像
        if(item.getUser()!=null && item.getUser().getFace()!=null){
            Glide.with(mContext).load(item.getUser().getFace()).crossFade().into((ImageView) helper.getView(R.id.item_message_head));
        }

        //加载昵称
        if(item.getUser()!=null){
            if(item.getUser().getSchoolname() == null||item.getUser().getSchoolname().equals("")){
                if(item.getUser().getNick_name() == null){
                    helper.setText(R.id.item_message_name,"未设置");
                }else{
                    helper.setText(R.id.item_message_name,item.getUser().getNick_name());
                }
            }else{
                helper.setText(R.id.item_message_name,item.getUser().getSchoolname());
            }
        }

        //加载更新时间
        if(item.getTime()!=null){
            helper.setText(R.id.item_message_time,item.getTime());
        }
        //加载消息内容
        if(item.getText() != null){
            helper.setText(R.id.item_message_info,item.getText());
        }

        if(item.getType() == LOVE_ACTION){
           helper.setText(R.id.item_message_type,"赞了你的动态");
           helper.setText(R.id.item_message_info,"");
        }else if(item.getType() == COMMENT_ACTION){
            helper.setText(R.id.item_message_type,"评论了你的动态:");
        }else if(item.getType() == REPLY_COMMENT){
            helper.setText(R.id.item_message_type,"回复了你的评论:");
        }else if(item.getType() == REPLY_AT_SOMEONE){
            helper.setText(R.id.item_message_type,"提到了你:");
        }

        TextView tv = (TextView) helper.getView(R.id.item_message_isread);

        if(item.getIsread() == 0){
            tv.setVisibility(View.VISIBLE);
        }else if(item.getIsread() == 1){
            tv.setVisibility(View.GONE);
        }

    }
}
