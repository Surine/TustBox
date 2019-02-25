package com.surine.tustbox.UI.View;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.surine.tustbox.Helper.Utils.ScreenUtils.dipToPx;

/**
 * Created by Surine on 2019/1/31.
 * 实现虾米音乐TabLayout效果
 * 参考：https://github.com/loveAndroidAndroid/XiaMiTablayout
 */

public class SuTabLayout extends HorizontalScrollView {

    //默认字体大小
    private final int DEFAULT_NORMAL_TEXT_SIZE_SP = ScreenUtils.sp2px(16);
    private int mNormalTextSize = DEFAULT_NORMAL_TEXT_SIZE_SP;
    //选中字体大小
    private final int DEFAULT_SELECT_TEXT_SIZE_SP = ScreenUtils.sp2px(34);
    private int mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE_SP;
    //字体颜色
    private final int DEFAULT_NORMAL_TEXT_COLOR = Color.BLACK;
    private final int DEFAULT_SELECT_TEXT_COLOR = Color.BLACK;

    //padding
    private final int PADING_START = 10;
    private final int PADING_END = 10;
    private final int PADING_LEFT = 15;
    private final int PADING_RIGHT = 15;

    //利用估值器实现渐变
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private IntEvaluator intEvaluator = new IntEvaluator();

    //顶部栏布局
    private LinearLayout mTabContainer = null;
    //数据源
    private ArrayList<String> mDataList = new ArrayList<>();
    //viewpaper
    private ViewPager viewPager;
    //tab数量
    private int mTabCount;


    public SuTabLayout(Context context) {
        super(context);
        init(context);
    }
    public SuTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public SuTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public SuTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);
        mTabContainer = new LinearLayout(context);
        mTabContainer.setPadding(50,0,0,0);
        mTabContainer.setBackgroundColor(context.getResources().getColor(R.color.background));
        //加入容器
        addView(mTabContainer, 0, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    /**
     * 关联viewpaper
     * @param viewPager
     * */
    public void setupWithViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        if(viewPager == null){
            throw new IllegalArgumentException("NullPointerException:Viewpaper is null");
        }
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new IllegalArgumentException("NullPointerException : pagerAdapter is null");
        }
        //添加页面改变监听器
        viewPager.addOnPageChangeListener(new TabPagerChanger());
        //获取tab数量
        mTabCount = pagerAdapter.getCount();
        //获取当前viewpager的栏目序号
       // mCurrentTabPosition = viewPager.getCurrentItem();
        //配置标题
        SimpleFragmentPagerAdapter simpleFragmentPagerAdapter = (SimpleFragmentPagerAdapter) viewPager.getAdapter();
        List<String> list = simpleFragmentPagerAdapter.getTitles();
        for (String s: list) {
            mDataList.add(s);
        }
        notifyDataSetChanged();
    }

    //更新数据
    private void notifyDataSetChanged() {
        //移除全部内容
        mTabContainer.removeAllViews();
        for (int i = 0; i < mTabCount; i++) {
            final int currentPosition = i;  //当前位置是i
            TextView textView = new TextView(getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize);
            textView.setGravity(Gravity.BOTTOM);
            textView.setText(mDataList.get(i));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(currentPosition);
                }
            });
            mTabContainer.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private class TabPagerChanger implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //获取当前选中的位置的TextView
            final TextView selectedChild = (TextView) mTabContainer.getChildAt(position);
            //获取他的下一个child，如果当前当前选中的child的下一个超越了孩子数量就返回null
            //否则就返回mTabContainer的孩子里的处于当前选中位置的下一个。
            final TextView nextChild = position + 1 < mTabContainer.getChildCount()
                    ? (TextView) mTabContainer.getChildAt(position + 1)
                    : null;


            if (selectedChild != null) {
                //设置选中孩子的字体大小
                selectedChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize - (mSelectTextSize - mNormalTextSize) * positionOffset);
                //初始颜色值
                int bgColor = DEFAULT_SELECT_TEXT_COLOR;
                //根据positionOffset状态设置字体颜色。
                //如果页面向右翻动，这个值不断变大，最后在趋近1的情况后突变为0。
                // 如果页面向左翻动，这个值不断变小，最后变为0。
                if (positionOffset == 0) {
                    //显示初始透明颜色
                    bgColor = DEFAULT_SELECT_TEXT_COLOR;
                } else if (positionOffset > 1) {
                    //滚动到一个定值后,颜色最深,而且不再加深
                    bgColor = DEFAULT_NORMAL_TEXT_COLOR;
                } else {
                    //滚动过程中渐变的颜色
                    bgColor = (int) argbEvaluator.evaluate(positionOffset, DEFAULT_SELECT_TEXT_COLOR, DEFAULT_NORMAL_TEXT_COLOR);
                }
                selectedChild.setTextColor(bgColor);

                int pad;
                if (positionOffset == 0) {
                    pad = PADING_START;
                } else if (positionOffset > 1) {
                    pad = PADING_END;
                } else {
                    //滚动过程中渐变的padding
                    pad = intEvaluator.evaluate(positionOffset, PADING_START, PADING_END);
                }
                selectedChild.setPadding(dipToPx(getContext(), PADING_LEFT), 0, dipToPx(getContext(), PADING_RIGHT), dipToPx(getContext(), pad));

                //设置字体
                if (positionOffset > 0.5) {
                    selectedChild.setTypeface(Typeface.DEFAULT);
                } else {
                    selectedChild.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }

            if (nextChild != null) {
                nextChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize + (mSelectTextSize - mNormalTextSize) * positionOffset);
                //初始颜色值
                int bgColor = DEFAULT_NORMAL_TEXT_COLOR;
                if (positionOffset == 0) {
                    //显示初始透明颜色
                    bgColor = DEFAULT_NORMAL_TEXT_COLOR;
                } else if (positionOffset > 1) {
                    //滚动到一个定值后,颜色最深,而且不再加深
                    bgColor = DEFAULT_SELECT_TEXT_COLOR;
                } else {
                    //滚动过程中渐变的颜色
                    bgColor = (int) argbEvaluator.evaluate(positionOffset, DEFAULT_NORMAL_TEXT_COLOR, DEFAULT_SELECT_TEXT_COLOR);
                }
                nextChild.setTextColor(bgColor);

                int pad;
                if (positionOffset == 0) {
                    pad = PADING_END;
                } else if (positionOffset > 1) {
                    pad = PADING_START;
                } else {
                    //滚动过程中渐变的padding
                    pad = intEvaluator.evaluate(positionOffset, PADING_END, PADING_START);
                }
                nextChild.setPadding(dipToPx(getContext(), PADING_LEFT), 0, dipToPx(getContext(), PADING_RIGHT), dipToPx(getContext(), pad));
                if (Math.abs(positionOffset) > 0.5) {
                    nextChild.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    nextChild.setTypeface(Typeface.DEFAULT);
                }
            }
            //解决点击间隔多个tab时 view的混乱变化
            for (int i = 0;i<mTabContainer.getChildCount();i++){
                View childView = mTabContainer.getChildAt(i);
                if (childView!=selectedChild&&childView!=nextChild){
                    TextView textView = (TextView) childView;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize);
                    textView.setTypeface(Typeface.DEFAULT);
                    textView.setTextColor(DEFAULT_NORMAL_TEXT_COLOR);
                    textView.setPadding(dipToPx(getContext(), PADING_LEFT), 0, dipToPx(getContext(), PADING_RIGHT), dipToPx(getContext(), PADING_END));
                }
            }

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
