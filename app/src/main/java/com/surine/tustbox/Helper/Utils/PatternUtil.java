package com.surine.tustbox.Helper.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by surine on 2017/9/2.
 * 正则规则
 */

public class PatternUtil {


    /********************
     * 提取字符串里的数字，并且转换为List表返回
     * **********************/
    public static List<String> getNumber(String a){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(a);
        List<String> list = new ArrayList<>();
        while (m.find()){
            list.add(m.group());
        }
        return list;
    }

    /**
     * @param text n个文本
     * @return true 文本正常，false文本为空或W者""
     * */
    public static final boolean Wow(String... text){
        for (int i = 0; i < text.length; i++) {
            if(text[i] == null || text[i].isEmpty()){
                return true;
            }
        }
        return false;
    }

}
