package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.surine.tustbox.Bean.ActionInfo;
import com.surine.tustbox.Bean.Image_Grid_Info;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/10/30.
 */

public class Action_List_Adapter extends BaseQuickAdapter<ActionInfo, BaseViewHolder> {


    public Action_List_Adapter(@LayoutRes int layoutResId, @Nullable List<ActionInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionInfo item) {
        //九宫格加载
        NineGridView nineGridView = (NineGridView)helper.getView(R.id.iv_ngrid_layout);
       //加载头像
        if(item.getHead_url()!=null){
           Glide.with(mContext).load(item.getHead_url()).crossFade().into((ImageView) helper.getView(R.id.action_head));
       }
       //加载昵称
        if(item.getName()!=null){
            helper.setText(R.id.user_name,item.getName());
        }
        //加载更新时间
        if(item.getTime()!=null){
            helper.setText(R.id.update_time,item.getTime());
        }
        //设置文字内容
        if(item.getText()!=null){
            helper.setText(R.id.tv2,item.getText());
        }
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<Image_Grid_Info> image_grid_infos = item.getImage_grid_infos();
        for(int i=0;i<item.getImage_grid_infos().size();i++){
            Log.d("XXX",item.getImage_grid_infos().get(i).getUrl());
        }
        //加载图像
        if(item.getImage_grid_infos() != null){

            //有图像
                for (Image_Grid_Info image : image_grid_infos) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(image.getUrl());
                    info.setBigImageUrl(image.getUrl());
                    imageInfo.add(info);
                }
        }
        nineGridView.setAdapter((new NineGridViewClickAdapter(mContext, imageInfo)));
        if (item.getImage_grid_infos() != null && item.getImage_grid_infos().size() == 1) {
            nineGridView.setSingleImageRatio(Float.parseFloat(item.getImage_grid_infos().get(0).getWidth()) * 1.0f / Float.parseFloat(item.getImage_grid_infos().get(0).getHeight()));
        }

    }
}
