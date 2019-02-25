package com.surine.tustbox.Pojo.EventBusBean;

/**
 * Created by surine on 2017/4/8.
 * 0 课表更新
 * 1 从发送动态页面发出通知动态页面更新动态列表
 * 2 从发送评论页面发出通知评论页面更新评论列表
 * 3 从发送回复页面发出通知回复页面更新回复列表
 * 4 从发送评论页面发出通知动态详情页面更新动态内容
 * 5 ActionInfoActivty -> lovefragment 更新点赞列表
 * 6 ReplyInCommentActivty -> Commentfragment 删除回复后更新某项评论的回复数
 * 7 CommentFragment -> ActionInfoActivity 删除回复后更新动态详情的回复数
 * 8 ActionInfoActivity -> LoveFragment 取消赞后更新love列表，
 *
 * 9 EditCourseActivty ——> EditCourseFragment 通知保存数据
 * 10 EditCourseActivty ——> EditCourseFragment 通知删除数据
 * 11 EditCourseActivty ——> EditCourseFragment 通知新建数据
 * 12 ScoreNewTermFragment/AllScoreFragment ——> ScoreDbFragment 通知更新数据库数据
 *
 */

public class SimpleEvent {
    private int id;
    private String message;

    public SimpleEvent(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
