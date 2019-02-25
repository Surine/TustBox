package com.surine.tustbox.UI.Fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.jph.takephoto.app.TakePhotoFragment;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.Helper.Utils.PhoenixUtil;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE;

/**
 * Created by surine on 2017/4  use TakePhotoLibrary
 * Modified by surine on 2019/1/27  use Phoenix
 */

public class SetBackgroundFragment extends Fragment {
    ImageView imageview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_set_background,container,false);
        imageview = view.findViewById(R.id.my_choose_image);
        //开始加载图片
        PhoenixUtil.with(1)
                .start(this, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //返回的数据
            List<MediaEntity> result = Phoenix.result(data);
            String path = result.get(0).getFinalPath();
            SharedPreferencesUtil.save(getActivity(), Constants.BACKGROUND_IMG_SAVE,path);
            Glide.with(this).load(path).into(imageview);
            Snackbar.make(imageview,"图片储存成功，请重启",Snackbar.LENGTH_LONG).setAction("重启", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TustBaseActivity.KillAPP();
                }
            }).show();
        }
    }




}
