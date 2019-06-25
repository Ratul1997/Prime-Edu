package com.primeedu.rat.primeedu.Class;

public class FatherDetails {
    private String studentfathername;
    private String studentfatheroccupation;
    private String studentfathercontactno;
    private String fatherprofilepic;

    public FatherDetails(String studentfathername, String studentfatheroccupation, String studentfathercontactno, String fatherprofilepic) {
        this.studentfathername = studentfathername;
        this.studentfatheroccupation = studentfatheroccupation;
        this.studentfathercontactno = studentfathercontactno;
        this.fatherprofilepic = fatherprofilepic;
    }

    public FatherDetails() {
    }

    public String getfatherprofilepic() {
        return fatherprofilepic;
    }

    public void setfatherprofilepic(String fatherprofilepic) {
        this.fatherprofilepic = fatherprofilepic;
    }

    public String getStudentfathername() {
        return studentfathername;
    }

    public void setStudentfathername(String studentfathername) {
        this.studentfathername = studentfathername;
    }

    public String getStudentfatheroccupation() {
        return studentfatheroccupation;
    }

    public void setStudentfatheroccupation(String studentfatheroccupation) {
        this.studentfatheroccupation = studentfatheroccupation;
    }

    public String getStudentfathercontactno() {
        return studentfathercontactno;
    }

    public void setStudentfathercontactno(String studentfathercontactno) {
        this.studentfathercontactno = studentfathercontactno;
    }
}
