package com.primeedu.rat.primeedu.Class;

public class ExamDetails {
    private String examname;
    private String examstartdate;
    private String examenddate;
    private String publishresultdate;
    private String routine;

    public ExamDetails(String examname, String examstartdate, String examenddate, String publishresultdate, String routine) {
        this.examname = examname;
        this.examstartdate = examstartdate;
        this.examenddate = examenddate;
        this.publishresultdate = publishresultdate;
        this.routine = routine;
    }

    public ExamDetails(){}

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

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
