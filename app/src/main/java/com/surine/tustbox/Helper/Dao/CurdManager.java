package com.surine.tustbox.Helper.Dao;

import java.util.List;

/**
 * 数据库操作基本类
 * Created by Surine on 2018/9/2.
 */

public interface CurdManager<T> {

    /**
     * 添加数据
     * @param t 泛型Bean
     * @return 是否成功
     * */
    boolean add(T t);

    /**
     * 按id删数据
     * @param id 数据id
     * @return int 影响的列数
     * */
    int delete(int id);

    /**
     * 按id选择数据
     * @param id 数据id
     * */
    T select(int id);

    /**
     * 更新数据
     * @param t 更新bean
     * @return boolean 返回操作结果
     * */
    boolean update(T t);


    /**
     * 获取全部数据
     * @return 数据集
     * */
    List<T> selectAll();
}
