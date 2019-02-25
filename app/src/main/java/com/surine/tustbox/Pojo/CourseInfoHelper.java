package com.surine.tustbox.Pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by Surine on 2018/8/23.
 * 新教务实体类
 */

public class CourseInfoHelper extends DataSupport{

    private int id;
    private String courseName;  //课程名
    private String attendClassTeacher;  //教师
    private String studyModeName;//课程状态（正常or重修）
    private int jwColor;  //颜色
    private String programPlanName; //计划方案
    private String unit; //学分

    private String weekDescription; //哪几周上课
    private String classDay; //周几上
    private String classSessions; //第几节
    private String continuingSession; //小课还是大课（2或者4）
    private String coureNumber; //课序号
    private String teachingBuildingName; //教学楼
    private String campusName; //校园
    private String classroomName; //教室


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAttendClassTeacher() {
        return attendClassTeacher;
    }

    public void setAttendClassTeacher(String attendClassTeacher) {
        this.attendClassTeacher = attendClassTeacher;
    }

    public String getStudyModeName() {
        return studyModeName;
    }

    public void setStudyModeName(String studyModeName) {
        this.studyModeName = studyModeName;
    }

    public int getJwColor() {
        return jwColor;
    }

    public void setJwColor(int jwColor) {
        this.jwColor = jwColor;
    }

    public String getProgramPlanName() {
        return programPlanName;
    }

    public void setProgramPlanName(String programPlanName) {
        this.programPlanName = programPlanName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWeekDescription() {
        return weekDescription;
    }

    public void setWeekDescription(String weekDescription) {
        this.weekDescription = weekDescription;
    }

    public String getClassDay() {
        return classDay;
    }

    public void setClassDay(String classDay) {
        this.classDay = classDay;
    }

    public String getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(String classSessions) {
        this.classSessions = classSessions;
    }

    public String getContinuingSession() {
        return continuingSession;
    }

    public void setContinuingSession(String continuingSession) {
        this.continuingSession = continuingSession;
    }

    public String getCoureNumber() {
        return coureNumber;
    }

    public void setCoureNumber(String coureNumber) {
        this.coureNumber = coureNumber;
    }

    public String getTeachingBuildingName() {
        return teachingBuildingName;
    }

    public void setTeachingBuildingName(String teachingBuildingName) {
        this.teachingBuildingName = teachingBuildingName;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    @Override
    public String toString() {
        return "CourseInfoHelper{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", attendClassTeacher='" + attendClassTeacher + '\'' +
                ", studyModeName='" + studyModeName + '\'' +
                ", jwColor=" + jwColor +
                ", programPlanName='" + programPlanName + '\'' +
                ", unit='" + unit + '\'' +
                ", weekDescription='" + weekDescription + '\'' +
                ", classDay='" + classDay + '\'' +
                ", classSessions='" + classSessions + '\'' +
                ", continuingSession='" + continuingSession + '\'' +
                ", coureNumber='" + coureNumber + '\'' +
                ", teachingBuildingName='" + teachingBuildingName + '\'' +
                ", campusName='" + campusName + '\'' +
                ", classroomName='" + classroomName + '\'' +
                '}';
    }
}

