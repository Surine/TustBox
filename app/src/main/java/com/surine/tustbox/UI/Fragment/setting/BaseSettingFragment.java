package com.surine.tustbox.UI.Fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.labels.LabelsView;
import com.surine.tustbox.Adapter.Recycleview.SettingAdapter;
import com.surine.tustbox.Pojo.SettingItem;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.UIPictureActivity;
import com.surine.tustbox.UI.Activity.V5_AboutAppActivity;
import com.surine.tustbox.Helper.Utils.AppUtil;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.surine.tustbox.App.Data.Constants.ABOUT;
import static com.surine.tustbox.App.Data.Constants.ACCOUNTBIND;
import static com.surine.tustbox.App.Data.Constants.BANNERBACKOUND;
import static com.surine.tustbox.App.Data.Constants.FEEDBACK;
import static com.surine.tustbox.App.Data.Constants.HIDEBOTTOM;
import static com.surine.tustbox.App.Data.Constants.INDEXCARD;
import static com.surine.tustbox.App.Data.Constants.NORMALPAGE;
import static com.surine.tustbox.App.Data.Constants.QQ;
import static com.surine.tustbox.App.Data.Constants.SUPPORT;
import static com.surine.tustbox.App.Data.Constants.WIDGETBACKOUND;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Read;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Save_safe;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.save;

/**
 * Created by Surine on 2019/1/31.
 */

public class BaseSettingFragment extends Fragment {

    private static BaseSettingFragment ourInstance = new BaseSettingFragment();
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    private SettingAdapter settingAdapter;

    Unbinder unbinder;
    private List<SettingItem> data = new ArrayList<>();

    public static BaseSettingFragment getInstance() {
        return ourInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_setting, container, false);
        unbinder = ButterKnife.bind(this, view);

        data = SettingData.getBaseData(getActivity());

        settingAdapter = new SettingAdapter(R.layout.item_setting,data);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(settingAdapter);

        initClickListener();

        return view;
    }


    //加载设置项监听器
    private void initClickListener() {
        settingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (data.get(position).getTitle()){
                    case ACCOUNTBIND:
                        //加载账号绑定页面
                        ToastUtil.show("本版本暂未开放~");
                        break;
                    case BANNERBACKOUND:
                        //选择图片
                        startActivity(new Intent(getActivity(),UIPictureActivity.class));
                        getActivity().finish();
                        break;
                    case WIDGETBACKOUND:
                        //小部件
                        setWidgetBackground(data.get(position).getView());
                        break;
                    case INDEXCARD:
                        //首页card
                        setIndexCard();
                        break;
                    case NORMALPAGE:
                        //设置默认页面
                        setNormalPage(data.get(position).getView());
                        break;
                    case HIDEBOTTOM:
                        //隐藏底部栏
                        setHideBottom(data.get(position).getView());
                        break;
                    case FEEDBACK:
                        //反馈
                        AppUtil.feedBack(getActivity());
                        break;
                    case QQ:
                        AppUtil.copyQQ(getActivity());
                        break;
                    case SUPPORT:
                        AppUtil.pay(getActivity());
                        break;
                    case ABOUT:
                        startActivity(new Intent(getActivity(), V5_AboutAppActivity.class));
                        break;
                }
            }
        });
    }

    //设置小部件背景
    private void setWidgetBackground(View view) {
        Switch s = (Switch) view;
        boolean hide = Read(getActivity(),WIDGETBACKOUND,false);
        //当前是隐藏
        if(hide){
            //执行非隐藏UI及存储
            s.setChecked(false);
            save(getActivity(),WIDGETBACKOUND,false);
            ToastUtil.show("已显示桌面小部件背景，刷新小部件生效");
        }else{
            s.setChecked(true);
            save(getActivity(),WIDGETBACKOUND,true);
            ToastUtil.show("已隐藏桌面小部件背景，刷新小部件生效");
        }
    }

    //选择首页展示的信息卡
    private void setIndexCard() {
        final BottomSheetDialog bottomSheetDialog  = new BottomSheetDialog(getActivity());
        View btmView = LayoutInflater.from(getActivity()).inflate(R.layout.view_btm_dialog_setting_page,null);
        bottomSheetDialog.setContentView(btmView);
        Button submit = btmView.findViewById(R.id.button17);
        LabelsView labelsView = btmView.findViewById(R.id.labels);

        final ArrayList<String> labStrings = new ArrayList<>();
        labStrings.add("SCHEDULE");
        labStrings.add("TASK");
        labStrings.add("SHARE");
        labStrings.add("底线");
        labelsView.setLabels(labStrings);
        labelsView.setSelectType(LabelsView.SelectType.MULTI);
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                LogUtil.d(label.getText()+""+isSelect);
                if(position == 3){
                    if(!isSelect){
                        ToastUtil.show("真的底线也不给人家留的嘛！ ＞…＜");
                    }
                }
                save(getActivity(),INDEXCARD+position,isSelect);
            }
        });
        //默认选中
        labelsView.setSelects(0,1,2,3);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("已选择！请手动重启！");
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    //设置默认首页
    private void setNormalPage(final View view) {
        final TextView tv = (TextView)view;

        final BottomSheetDialog bottomSheetDialog  = new BottomSheetDialog(getActivity());
        View btmView = LayoutInflater.from(getActivity()).inflate(R.layout.view_btm_dialog_setting_page,null);
        bottomSheetDialog.setContentView(btmView);
        Button submit = btmView.findViewById(R.id.button17);
        LabelsView labelsView = btmView.findViewById(R.id.labels);
        ArrayList<String> labStrings = new ArrayList<>();
        labStrings.add("首页");
        labStrings.add("课表");
        labStrings.add("任务");
        labStrings.add("盒子");
        labStrings.add("动态");

        //读取保存数值
        int choose = Read(getActivity(),NORMALPAGE,0);
        //设置view字符串
        tv.setText(labStrings.get(choose));
        labelsView.setLabels(labStrings);
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                LogUtil.d(label.getText()+""+isSelect);
                //存本项的值
                save(getActivity(),NORMALPAGE,position);
                tv.setText(label.getText());
            }
        });
        labelsView.setSelects(choose);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("已选择！请手动重启！");
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    //设置底部栏
    private void setHideBottom(View view) {
        Switch s = (Switch) view;
        boolean hide = Read(getActivity(),HIDEBOTTOM,false);
        //当前是隐藏
        if(hide){
            //执行非隐藏UI及存储
            s.setChecked(false);
            save(getActivity(),HIDEBOTTOM,false);
            ToastUtil.show("已显示底部栏，重启APP生效");
        }else{
            s.setChecked(true);
            save(getActivity(),HIDEBOTTOM,true);
            ToastUtil.show("已隐藏底部栏，重启APP生效");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
