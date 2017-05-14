package com.surine.tustbox.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/3/30.
 */

public class Course_Info extends DataSupport{
    private String dev;  //培养方案
    private String course_number;  //课程号
    private String course_name;   //课程名
    private String class_number;   //课序号
    private String score;  //学分
    private String tag;   //课程属性
    private String exm;   //考试类型
    private String teacher;  //教师
    private String method;  //修读方式
    private String status;   //选课方式
    private String week;  //周
    private String week_number;   //星期
    private String class_; //节次
    private String class_count;  //节数
    private String school;   //小区
    private String building;  //教学楼
    private String classroom;   //教室
    private int id;    //id
    private String note;  //备注
    private String homework;  //作业
    private int color;   //颜色
    private int user;  //用户

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getCourse_number() {
        return course_number;
    }

    public void setCourse_number(String course_number) {
        this.course_number = course_number;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExm() {
        return exm;
    }

    public void setExm(String exm) {
        this.exm = exm;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeek_number() {
        return week_number;
    }

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public String getClass_() {
        return class_;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public String getClass_count() {
        return class_count;
    }

    public void setClass_count(String class_count) {
        this.class_count = class_count;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
