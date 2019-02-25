package com.surine.tustbox.Mvp.Dao;

import com.surine.tustbox.Helper.Dao.CurdManager;
import com.surine.tustbox.Pojo.JwcUserInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Surine on 2018/9/2.
 */

public class UserInfoDao implements CurdManager<JwcUserInfo> {

    /**
     * 添加个人信息到数据库
     * @param jwcUserInfo 待添加的个人信息
     * @return 是否添加成功
     * */
    @Override
    public boolean add(JwcUserInfo jwcUserInfo) {
        return jwcUserInfo.save();
    }

    /**
     * 删除个人信息数据
     * @param id 被删除条目的id
     * @return 影响的数据数目
     * */
    @Override
    public int delete(int id) {
        return DataSupport.delete(JwcUserInfo.class,id);
    }

    /**
     * 查询个人信息数据
     * @param id 个人信息id
     * @return 个人信息bean
     * */
    @Override
    public JwcUserInfo select(int id) {
        return DataSupport.find(JwcUserInfo.class,id);
    }

    /**
     * 更新个人信息数据
     * (本APP里暂时没有用到)
     * @param jwcUserInfo 待修改的数据
     * @return 修改状态
     * */
    @Override
    public boolean update(JwcUserInfo jwcUserInfo) {
        return jwcUserInfo.save();
    }

    @Override
    public List<JwcUserInfo> selectAll() {
        return null;
    }
}
