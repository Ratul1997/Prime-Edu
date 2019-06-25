package com.primeedu.rat.primeedu.Class;

public class StudentDetails {

    private String studentfname,studenlname;
    private String studentaddress;
    private String studentprofilepicture;
    private String gender,contactnumber;
    private GurdianDetails gurdianDetails;
    private FatherDetails fatherDetails;
    private MotherDetails motherDetails;

    public StudentDetails(String studentfname, String studenlname, String studentaddress, String studentprofilepicture, String gender, String contactnumber, GurdianDetails gurdianDetails, FatherDetails fatherDetails, MotherDetails motherDetails) {
        this.studentfname = studentfname;
        this.studenlname = studenlname;
        this.studentaddress = studentaddress;
        this.studentprofilepicture = studentprofilepicture;
        this.gender = gender;
        this.contactnumber = contactnumber;
        this.gurdianDetails = gurdianDetails;
        this.fatherDetails = fatherDetails;
        this.motherDetails = motherDetails;
    }

    public StudentDetails() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getStudentfname() {
        return studentfname;
    }

    public void setStudentfname(String studentfname) {
        this.studentfname = studentfname;
    }

    public String getStudenlname() {
        return studenlname;
    }

    public void setStudenlname(String studenlname) {
        this.studenlname = studenlname;
    }

    public String getStudentaddress() {
        return studentaddress;
    }

    public void setStudentaddress(String studentaddress) {
        this.studentaddress = studentaddress;
    }

    public String getStudentprofilepicture() {
        return studentprofilepicture;
    }

    public void setStudentprofilepicture(String studentprofilepicture) {
        this.studentprofilepicture = studentprofilepicture;
    }

    public GurdianDetails getGurdianDetails() {
        return gurdianDetails;
    }

    public void setGurdianDetails(GurdianDetails gurdianDetails) {
        this.gurdianDetails = gurdianDetails;
    }

    public FatherDetails getFatherDetails() {
        return fatherDetails;
    }

    public void setFatherDetails(FatherDetails fatherDetails) {
        this.fatherDetails = fatherDetails;
    }

    public MotherDetails getMotherDetails() {
        return motherDetails;
    }

    public void setMotherDetails(MotherDetails motherDetails) {
        this.motherDetails = motherDetails;
    }
}


