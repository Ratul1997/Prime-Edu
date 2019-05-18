package com.example.rat.primeedu.Class;

public class ExamDetails {
    private String examname;
    private String examstartdate;
    private String examenddate;
    private String publishresultdate;

    public ExamDetails(String examname, String examstartdate, String examenddate, String publishresultdate) {
        this.examname = examname;
        this.examstartdate = examstartdate;
        this.examenddate = examenddate;
        this.publishresultdate = publishresultdate;
    }

    public ExamDetails(){}

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public String getExamstartdate() {
        return examstartdate;
    }

    public void setExamstartdate(String examstartdate) {
        this.examstartdate = examstartdate;
    }

    public String getExamenddate() {
        return examenddate;
    }

    public void setExamenddate(String examenddate) {
        this.examenddate = examenddate;
    }

    public String getPublishresultdate() {
        return publishresultdate;
    }

    public void setPublishresultdate(String publishresultdate) {
        this.publishresultdate = publishresultdate;
    }
}
