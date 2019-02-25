package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2018/8/24.
 */

public class TimeAndPlaceListBean {

    private String coureNumber;  //课序号
    private int classDay; //周几
    private int classSessions; //第几节
    private String campusName; //校园名
    private String teachingBuildingName; //教学楼
    private String classroomName;  //教室
    private String weekDescription;  //那些周上
    private int continuingSession;  //持续几节

    public String getCoureNumber() {
        return coureNumber;
    }

    public void setCoureNumber(String coureNumber) {
        this.coureNumber = coureNumber;
    }

    public int getClassDay() {
        return classDay;
    }

    public void setClassDay(int classDay) {
        this.classDay = classDay;
    }

    public int getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(int classSessions) {
        this.classSessions = classSessions;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getTeachingBuildingName() {
        return teachingBuildingName;
    }

    public void setTeachingBuildingName(String teachingBuildingName) {
        this.teachingBuildingName = teachingBuildingName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getWeekDescription() {
        return weekDescription;
    }

    public void setWeekDescription(String weekDescription) {
        this.weekDescription = weekDescription;
    }

    public int getContinuingSession() {
        return continuingSession;
    }

    public void setContinuingSession(int continuingSession) {
        this.continuingSession = continuingSession;
    }

    public TimeAndPlaceListBean(String coureNumber, int classDay, int classSessions, String campusName, String teachingBuildingName, String classroomName, String weekDescription, int continuingSession) {
        this.coureNumber = coureNumber;
        this.classDay = classDay;
        this.classSessions = classSessions;
        this.campusName = campusName;
        this.teachingBuildingName = teachingBuildingName;
        this.classroomName = classroomName;
        this.weekDescription = weekDescription;
        this.continuingSession = continuingSession;
    }

    public TimeAndPlaceListBean() {
    }

    @Override
    public String toString() {
        return "TimeAndPlaceListBean{" +
                "coureNumber='" + coureNumber + '\'' +
                ", classDay=" + classDay +
                ", classSessions=" + classSessions +
                ", campusName='" + campusName + '\'' +
                ", teachingBuildingName='" + teachingBuildingName + '\'' +
                ", classroomName='" + classroomName + '\'' +
                ", weekDescription='" + weekDescription + '\'' +
                ", continuingSession=" + continuingSession +
                '}';
    }
}