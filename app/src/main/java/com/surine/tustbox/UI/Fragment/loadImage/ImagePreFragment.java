package com.surine.tustbox.UI.Fragment.loadImage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static java.lang.System.out;

/**
 * Created by Surine on 2019/2/21.
 */

public class ImagePreFragment extends Fragment {
    @BindView(R.id.photoView)
    PhotoView photoView;

    Unbinder unbinder;
    private String url;
    public static final String ARG = "ImagePreFragment";

    public static ImagePreFragment getInstance(String url) {
        ImagePreFragment imagePreFragment = new ImagePreFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, url);
        imagePreFragment.setArguments(bundle);
        return imagePreFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        url = bundle.getString(ARG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        //加载图片预览器
        photoView.setScaleType(ImageView.ScaleType.CENTER);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("保存图片");
                builder.setMessage("是否保存该图片？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePicture();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                return true;
            }
        });
        //加载
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.school_shape)
                .error(R.drawable.school_shape)
                .fitCenter()
                .into(photoView);

        return view;
    }

    //保存本图片
    private void savePicture() {
        HttpUtil.get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                byte[] bytes = response.body().bytes();
                final Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                RunOnUiThread.updateUi(getActivity(), new UpdateUIListenter() {
                    @Override
                    public void update() {
                        saveBitmap(bitmap);
                    }
                });
            }
        });
    }


    private void saveBitmap(Bitmap bitmap) {
       String filename = UUID.randomUUID() + ".png";
       FileOutputStream fOut = null;
       File f = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), filename);
       try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            ToastUtil.show("文件已存储至：/sdcard/download");
        } catch (IOException e) {
            ToastUtil.getError();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
