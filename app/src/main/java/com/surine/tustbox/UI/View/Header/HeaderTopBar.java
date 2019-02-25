package com.surine.tustbox.UI.View.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.V5_SettingActivity;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import static com.surine.tustbox.Helper.Utils.TimeUtil.*;

/**
 * Created by Surine on 2019/1/28.
 * 首页顶部栏部分
 */

public class HeaderTopBar{
    public static View getInstance(final Context context, RecyclerView recyclerView){
        View view = LayoutInflater.from(context).inflate(R.layout.header_bar,(ViewGroup) recyclerView.getParent(),false);

        //设置按钮监听事件
        ImageView mainSetting = view.findViewById(R.id.mainSetting);
        mainSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, V5_SettingActivity.class);
                context.startActivity(intent);
            }
        });

        TextView textView = view.findViewById(R.id.tustBoxName);
        textView.setText(getDayCC(getDate(yMdHm))+" · "+getMonthCC(getDate(yMdHm)));

        ImageView backSrc = view.findViewById(R.id.tustBoxHead);
        String imagePath = SharedPreferencesUtil.Read(context, Constants.BACKGROUND_IMG_SAVE, null);
        if (imagePath != null) {
            Glide.with(context).
                    load(imagePath)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(backSrc) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                        }
                    });
        }

        return view;
    }
}
