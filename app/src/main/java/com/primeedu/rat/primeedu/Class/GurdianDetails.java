package com.primeedu.rat.primeedu.Class;

public class GurdianDetails {
    private String studentgurdianname;
    private String studentgurdianoccupation;
    private String studentgurdiancontactno;
    private String gurdianprofilepic;
    private String relation;

    public GurdianDetails(String studentgurdianname, String studentgurdianoccupation, String studentgurdiancontactno, String gurdianprofilepic, String relation) {
        this.studentgurdianname = studentgurdianname;
        this.studentgurdianoccupation = studentgurdianoccupation;
        this.studentgurdiancontactno = studentgurdiancontactno;
        this.gurdianprofilepic = gurdianprofilepic;
        this.relation = relation;
    }

    public GurdianDetails() {
    }

    public String getStudentgurdianname() {
        return studentgurdianname;
    }

    public void setStudentgurdianname(String studentgurdianname) {
        this.studentgurdianname = studentgurdianname;
    }

    public String getStudentgurdianoccupation() {
        return studentgurdianoccupation;
    }

    public void setStudentgurdianoccupation(String studentgurdianoccupation) {
        this.studentgurdianoccupation = studentgurdianoccupation;
    }

    public String getStudentgurdiancontactno() {
        return studentgurdiancontactno;
    }

    public void setStudentgurdiancontactno(String studentgurdiancontactno) {
        this.studentgurdiancontactno = studentgurdiancontactno;
    }

    public String getGurdianprofilepic() {
        return gurdianprofilepic;
    }

    public void setGurdianprofilepic(String gurdianprofilepic) {
        this.gurdianprofilepic = gurdianprofilepic;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
