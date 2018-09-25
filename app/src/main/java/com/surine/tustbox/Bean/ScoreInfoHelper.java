package com.surine.tustbox.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Surine on 2018/8/31.
 */

public class ScoreInfoHelper extends DataSupport{
    private int id;
    private String cj;  //成绩
    private String courseAttributeName;  //课程属性
    private String courseName;  //课程名
    private String credit;  //学分
    private String englishCourseName;  //英文名
    private String gradePointScore;  //学分绩
    private String gradeName;  //优秀？及格？
    private String termInfo;  //学期

    public String getTermInfo() {
        return termInfo;
    }

    public void setTermInfo(String termInfo) {
        this.termInfo = termInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String getCourseAttributeName() {
        return courseAttributeName;
    }

    public void setCourseAttributeName(String courseAttributeName) {
        this.courseAttributeName = courseAttributeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getEnglishCourseName() {
        return englishCourseName;
    }

    public void setEnglishCourseName(String englishCourseName) {
        this.englishCourseName = englishCourseName;
    }

    public String getGradePointScore() {
        return gradePointScore;
    }

    public void setGradePointScore(String gradePointScore) {
        this.gradePointScore = gradePointScore;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    @Override
    public String toString() {
        return "ScoreInfoHelper{" +
                "id=" + id +
                ", cj='" + cj + '\'' +
                ", courseAttributeName='" + courseAttributeName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credit='" + credit + '\'' +
                ", englishCourseName='" + englishCourseName + '\'' +
                ", gradePointScore='" + gradePointScore + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", termInfo='" + termInfo + '\'' +
                '}';
    }
}
