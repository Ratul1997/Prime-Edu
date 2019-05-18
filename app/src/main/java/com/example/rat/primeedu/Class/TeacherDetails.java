package com.example.rat.primeedu.Class;

public class TeacherDetails {
    private String teachername;
    private String teachersubject;
    private String teacherstatus;
    private String teachercontactno;
    private String teachercurrentschool;
    private String teacheremail;
    private String teacheraddress;

    public TeacherDetails(String teachername, String teachersubject, String teacherstatus, String teachercontactno, String teachercurrentschool, String teacheremail, String teacheraddress) {
        this.teachername = teachername;
        this.teachersubject = teachersubject;
        this.teacherstatus = teacherstatus;
        this.teachercontactno = teachercontactno;
        this.teachercurrentschool = teachercurrentschool;
        this.teacheremail = teacheremail;
        this.teacheraddress = teacheraddress;
    }

    public TeacherDetails(){

    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeachersubject() {
        return teachersubject;
    }

    public void setTeachersubject(String teachersubject) {
        this.teachersubject = teachersubject;
    }

    public String getTeacherstatus() {
        return teacherstatus;
    }

    public void setTeacherstatus(String teacherstatus) {
        this.teacherstatus = teacherstatus;
    }

    public String getTeachercontactno() {
        return teachercontactno;
    }

    public void setTeachercontactno(String teachercontactno) {
        this.teachercontactno = teachercontactno;
    }

    public String getTeachercurrentschool() {
        return teachercurrentschool;
    }

    public void setTeachercurrentschool(String teachercurrentschool) {
        this.teachercurrentschool = teachercurrentschool;
    }

    public String getTeacheremail() {
        return teacheremail;
    }

    public void setTeacheremail(String teacheremail) {
        this.teacheremail = teacheremail;
    }

    public String getTeacheraddress() {
        return teacheraddress;
    }

    public void setTeacheraddress(String teacheraddress) {
        this.teacheraddress = teacheraddress;
    }
}
