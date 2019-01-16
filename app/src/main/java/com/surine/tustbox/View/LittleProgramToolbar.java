package com.surine.tustbox.View;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surine.tustbox.Interface.AddFunctionListenter;
import com.surine.tustbox.R;

/**
 * Created by Surine on 2019/1/2.
 */

public class LittleProgramToolbar extends RelativeLayout {
    private TextView title;
    private ImageView toolbar_info;
    private ImageView exit;
    private LinearLayout linearLayout;
    private BottomSheetDialog bottomSheetDialog;
    private RelativeLayout addFunction;
    private AddFunctionListenter addFunctionListenter;
    private TextView text;

    public LittleProgramToolbar(Context context) {
        super(context);
        init(context);
    }

    public LittleProgramToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LittleProgramToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LittleProgramToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    //初始化
    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_little_program_toolbar,this);
        title = findViewById(R.id.textView21);
        toolbar_info = findViewById(R.id.toolbar_info);
        exit = findViewById(R.id.exit);
        linearLayout = findViewById(R.id.network_note);
        text = findViewById(R.id.netNoteText);
        addFunction = findViewById(R.id.add_function);
        addFunction.setVisibility(GONE);  //默认额外功能是不显示的
        addFunction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFunctionListenter.onClick();
            }
        });

        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity)context;
                activity.finish();
            }
        });

        toolbar_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.dialog_little_program_info);
                bottomSheetDialog.show();
            }
        });

    }

    /**
     * 配置标题
     * @param t 标题字符串
     * */
    public LittleProgramToolbar setTitle(String t){
        if(title != null)
            title.setText(t);
        return this;
    }

    /**
     * 隐藏网络提醒
     * @param is 是否隐藏
     * 用于提醒用户是否需要校园网
     * */
    public LittleProgramToolbar setNoteGone(boolean is){
        if(linearLayout != null){
            if(is){
                linearLayout.setVisibility(GONE);
            }else{
                linearLayout.setVisibility(INVISIBLE);
            }
        }
        return this;
    }


    /**
     * 显示，隐藏额外功能按钮
     * @param is 是否显示
     * */
    public LittleProgramToolbar setAddFunctionVisible(boolean is){
        if(addFunction != null){
            if(is){
                addFunction.setVisibility(VISIBLE);
            }else{
                addFunction.setVisibility(GONE);
            }
        }
        return this;
    }

    public LittleProgramToolbar setNoteText(String msg){
        if(text != null){
            text.setText(msg);
        }
        return this;
    }



    public void setOnClickAddFunctionListener(AddFunctionListenter addFunctionListener){
        this.addFunctionListenter = addFunctionListener;
    }
}
