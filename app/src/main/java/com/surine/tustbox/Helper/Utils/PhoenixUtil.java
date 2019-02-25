package com.surine.tustbox.Helper.Utils;

import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MimeType;

/**
 * Created by Surine on 2019/2/21.
 * 图片选择器工具
 */

public class PhoenixUtil {

    /**
     * 加载默认配置
     * @param maxPickNumber 最大选择数量
     * */
    public static PhoenixOption with(int maxPickNumber){
        PhoenixOption po = new PhoenixOption();
        po.theme(PhoenixOption.THEME_BLUE);  //主题
        po.fileType(MimeType.ofImage());  //设置只显示图片
        po.enableGif(false);   //禁止显示gif
        po.maxPickNumber(maxPickNumber);  //最大选择
        po.minPickNumber(1); //最小选择
        po.spanCount(4);  //每行显示个数
        po.enablePreview(true); // 是否开启预览
        po.enableAnimation(true); // 选择界面图片点击效果
        po.enableCompress(true);  // 是否开启压缩
        po.compressPictureFilterSize(1024); //多少kb以下的图片不压缩
        po.thumbnailHeight(160);  // 选择界面图片高度
        po.thumbnailWidth(160);   // 选择界面图片宽度
        return po;
    }
}
