package com.example.rat.primeedu.Class;

public class StudentDetails {

    private String studentname;
    private String studentfathername;
    private String studentmothername;
    private String studentfatheroccupation;
    private String studentmotheroccupation;
    private String studentfathercontactno;
    private String studentmothercontactno;
    private String studentaddress;

    public StudentDetails(String studentname, String studentfathername, String studentmothername, String studentfatheroccupation, String studentmotheroccupation, String studentfathercontactno, String studentmothercontactno, String studentaddress) {
        this.studentname = studentname;
        this.studentfathername = studentfathername;
        this.studentmothername = studentmothername;
        this.studentfatheroccupation = studentfatheroccupation;
        this.studentmotheroccupation = studentmotheroccupation;
        this.studentfathercontactno = studentfathercontactno;
        this.studentmothercontactno = studentmothercontactno;
        this.studentaddress = studentaddress;
    }

    public StudentDetails(){}

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentfathername() {
        return studentfathername;
    }

    public void setStudentfathername(String studentfathername) {
        this.studentfathername = studentfathername;
    }

    public String getStudentmothername() {
        return studentmothername;
    }

    public void setStudentmothername(String studentmothername) {
        this.studentmothername = studentmothername;
    }

    public String getStudentfatheroccupation() {
        return studentfatheroccupation;
    }

    public void setStudentfatheroccupation(String studentfatheroccupation) {
        this.studentfatheroccupation = studentfatheroccupation;
    }

    public String getStudentmotheroccupation() {
        return studentmotheroccupation;
    }

    public void setStudentmotheroccupation(String studentmotheroccupation) {
        this.studentmotheroccupation = studentmotheroccupation;
    }

    public String getStudentfathercontactno() {
        return studentfathercontactno;
    }

    public void setStudentfathercontactno(String studentfathercontactno) {
        this.studentfathercontactno = studentfathercontactno;
    }

    public String getStudentmothercontactno() {
        return studentmothercontactno;
    }

    public void setStudentmothercontactno(String studentmothercontactno) {
        this.studentmothercontactno = studentmothercontactno;
    }

    public String getStudentaddress() {
        return studentaddress;
    }

    public void setStudentaddress(String studentaddress) {
        this.studentaddress = studentaddress;
    }
}
