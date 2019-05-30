package com.example.rat.primeedu.Class;

public class MotherDetails {
    private String studentmothername;
    private String studentmotheroccupation;
    private String studentmothercontactno;
    private String motherprofilepic;

    public MotherDetails(String studentmothername, String studentmotheroccupation, String studentmothercontactno, String motherprofilepic) {
        this.studentmothername = studentmothername;
        this.studentmotheroccupation = studentmotheroccupation;
        this.studentmothercontactno = studentmothercontactno;
        this.motherprofilepic = motherprofilepic;
    }

    public String getMotherprofilepic() {
        return motherprofilepic;
    }

    public void setMotherprofilepic(String motherprofilepic) {
        this.motherprofilepic = motherprofilepic;
    }

    public MotherDetails() {
    }

    public String getStudentmothername() {
        return studentmothername;
    }

    public void setStudentmothername(String studentmothername) {
        this.studentmothername = studentmothername;
    }

    public String getStudentmotheroccupation() {
        return studentmotheroccupation;
    }

    public void setStudentmotheroccupation(String studentmotheroccupation) {
        this.studentmotheroccupation = studentmotheroccupation;
    }

    public String getStudentmothercontactno() {
        return studentmothercontactno;
    }

    public void setStudentmothercontactno(String studentmothercontactno) {
        this.studentmothercontactno = studentmothercontactno;
    }
}
