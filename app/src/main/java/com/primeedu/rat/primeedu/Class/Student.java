package com.primeedu.rat.primeedu.Class;

import java.io.Serializable;

public class Student implements Serializable {
    private String schoolcode;
    private String studentcode;
    private String currentid;
    private String currentsection;
    private String currentclass;
    private String sutdentpass;
    private String token;

    private transient StudentDetails studentDetails;

    public Student(String schoolcode, String studentcode, String currentid, String currentsection, String currentclass, String sutdentpass, StudentDetails studentDetails,String token) {
        this.schoolcode = schoolcode;
        this.studentcode = studentcode;
        this.currentid = currentid;
        this.currentsection = currentsection;
        this.currentclass = currentclass;
        this.sutdentpass = sutdentpass;
        this.studentDetails = studentDetails;
        this.token = token;
    }

    public Student() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSchoolcode() {
        return schoolcode;
    }

    public void setSchoolcode(String schoolcode) {
        this.schoolcode = schoolcode;
    }

    public String getStudentcode() {
        return studentcode;
    }

    public void setStudentcode(String studentcode) {
        this.studentcode = studentcode;
    }

    public String getCurrentid() {
        return currentid;
    }

    public void setCurrentid(String currentid) {
        this.currentid = currentid;
    }

    public String getCurrentsection() {
        return currentsection;
    }

    public void setCurrentsection(String currentsection) {
        this.currentsection = currentsection;
    }

    public String getCurrentclass() {
        return currentclass;
    }

    public void setCurrentclass(String currentclass) {
        this.currentclass = currentclass;
    }

    public String getSutdentpass() {
        return sutdentpass;
    }

    public void setSutdentpass(String sutdentpass) {
        this.sutdentpass = sutdentpass;
    }

    public StudentDetails getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(StudentDetails studentDetails) {
        this.studentDetails = studentDetails;
    }
}
