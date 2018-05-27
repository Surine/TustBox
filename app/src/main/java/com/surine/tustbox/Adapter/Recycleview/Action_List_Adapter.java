package com.surine.tustbox.Adapter.Recycleview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import static com.surine.tustbox.Data.Constants.HTTP;
import static com.surine.tustbox.Data.Constants.PIC_CROP;

/**
 * Created by surine on 2017/10/30.
 */

public class Action_List_Adapter extends BaseQuickAdapter<Action, BaseViewHolder> {


    private String faceUrl;

    public Action_List_Adapter(@LayoutRes int layoutResId, @Nullable List<Action> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Action item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext) ;

        helper.addOnClickListener(R.id.more);
        helper.addOnClickListener(R.id.love);
        helper.addOnClickListener(R.id.action_info_head);

        //九宫格加载
        NineGridView nineGridView = (NineGridView)helper.getView(R.id.iv_ngrid_layout);
       //加载头像
        if(item.getUser()!=null && item.getUser().getFace()!=null){
            if(item.getUser().getFace().equals(UrlData.normalFace)){
                faceUrl = item.getUser().getFace();

            }else{
                if(prefs.getBoolean(FormData.PICCROP,true)){
                    //不开启图片压缩
                    faceUrl = item.getUser().getFace();
                }else{
                    //开启图片压缩
                    faceUrl = item.getUser().getFace()+ PIC_CROP;
                }
            }

           Glide.with(mContext).load(faceUrl).crossFade().into((ImageView) helper.getView(R.id.action_info_head));
       }
       //加载昵称
        if(item.getUser()!=null){
            if(item.getUser().getSchoolname() == null||item.getUser().getSchoolname().equals("")){
                if(item.getUser().getNick_name() == null){
                    helper.setText(R.id.action_info_user_name,"未设置");
                }else{
                    helper.setText(R.id.action_info_user_name,item.getUser().getNick_name());
                }
            }else{
                helper.setText(R.id.action_info_user_name,item.getUser().getSchoolname());
            }
        }
        //加载更新时间
        if(item.getMessages_time()!=null){
            helper.setText(R.id.action_info_update_time,item.getMessages_time());
        }

        //设置点赞数
        if(item.getMessages_agreenum()!=null){
            helper.setText(R.id.love_num,item.getMessages_agreenum());
        }

        //设置文字内容
        if(item.getMessages_info()!=null){
            helper.setText(R.id.action_info_message,item.getMessages_info());
        }
        //主题
        if(item.getMessages_topic()!=null){
            helper.setText(R.id.action_topic,item.getMessages_topic());
        }

        if(item.isIslove()){
            helper.setImageResource(R.id.love,R.drawable.love_red);
        }else{
            helper.setImageResource(R.id.love,R.drawable.love);
        }

        if(item.getPic_ids() != null && !item.getPic_ids().equals("")){
            nineGridView.setVisibility(View.VISIBLE);
            //有图像
            String[] pics = item.getPic_ids().split(",");
            String ThumbnailUrl;
            String BigImageCrop;
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for(int i =0;i < pics.length ; i++){

                if(prefs.getBoolean(FormData.PICCROP,true)){
                    //不开启图片压缩
                    ThumbnailUrl = HTTP+item.getPicbaseurl()+pics[i];
                    BigImageCrop = HTTP+item.getPicbaseurl()+pics[i];
                }else{
                    //开启图片压缩
                    ThumbnailUrl = HTTP+item.getPicbaseurl()+pics[i]+ PIC_CROP;
                    BigImageCrop = HTTP+item.getPicbaseurl()+pics[i]+ PIC_CROP;
                }
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(ThumbnailUrl);
                info.setBigImageUrl(BigImageCrop);
                imageInfo.add(info);

            }
            nineGridView.setAdapter((new NineGridViewClickAdapter(mContext, imageInfo)));
        }else{
            nineGridView.setAdapter((new NineGridViewClickAdapter(mContext, null)));
            nineGridView.setVisibility(View.GONE);
        }



    }
}
