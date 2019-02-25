package com.surine.tustbox.UI.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surine.tustbox.R;

/**
 * Created by Surine on 2019/1/30.
 */

public class VgTopbar extends RelativeLayout {
    private TextView title;
    private ImageView leftIcon;
    private ImageView rightIcon;
    private OnClickListener listener;
    public VgTopbar(Context context) {
        super(context);
        init(context);
    }

    public VgTopbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VgTopbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VgTopbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vg_topbar_left_right_and_title,this);
        title = findViewById(R.id.tustBoxName);
        leftIcon = findViewById(R.id.lefeIcon);
        rightIcon = findViewById(R.id.rightIcon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftButton();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightButton();
            }
        });
    }

    public void setListener(OnClickListener listener){
        this.listener = listener;
    }

    public interface OnClickListener{
        void leftButton();
        void rightButton();
    }

    /**
     * 设置topbar标题
     * @param titleMsg 标题
     * */
    public VgTopbar setTitle(String titleMsg){
        if(titleMsg != null)
            title.setText(titleMsg);
        return this;
    }

    /**
     * 设置左图标
     * @param id 图标id
     * */
    public VgTopbar setLeftIcon(int id){
        if(leftIcon != null)
            leftIcon.setImageResource(id);
        return this;
    }

    /**
     * 设置右图标
     * @param id 图标id
     * */
    public VgTopbar setRightIcon(int id){
        if(rightIcon != null)
            rightIcon.setImageResource(id);
        return this;
    }


    /**
     * 显示隐藏左图标
     * @param gone 为true的时候隐藏，false显示
     * */
    public VgTopbar setLeftIconGone(boolean gone){
        if(leftIcon != null){
            if(gone){
                leftIcon.setVisibility(GONE);
            }else{
                leftIcon.setVisibility(VISIBLE);
            }
        }
        return this;
    }


    /**
     * 显示隐藏右图标
     * @param gone 为true的时候隐藏，false显示
     * */
    public VgTopbar setRightIconGone(boolean gone){
        if(rightIcon != null){
            if(gone){
                rightIcon.setVisibility(GONE);
            }else{
                rightIcon.setVisibility(VISIBLE);
            }
        }
        return this;
    }

    /**
     * 获取view
     * */
    public View getView(int tag){
        if(tag == 1){
            return leftIcon;
        }else if(tag == 2){
            return title;
        }else if(tag == 3){
            return rightIcon;
        }
        return null;
    }
}
