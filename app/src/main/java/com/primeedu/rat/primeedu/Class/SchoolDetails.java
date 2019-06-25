package com.primeedu.rat.primeedu.Class;

import java.io.Serializable;

public class SchoolDetails implements Serializable{
    private String schoolname;
    private String schoolemail;
    private String schoolstudentnumber;
    private String schoolstaffnumbers;
    private String schoolteachernumbers;
    private String schooladdress;
    private String schoolcontactnumber;

    public SchoolDetails(String schoolname, String schoolemail, String schoolstudentnumber, String schoolstaffnumbers, String schoolteachernumbers, String schooladdress, String schoolcontactnumber) {
        this.schoolname = schoolname;
        this.schoolemail = schoolemail;
        this.schoolstudentnumber = schoolstudentnumber;
        this.schoolstaffnumbers = schoolstaffnumbers;
        this.schoolteachernumbers = schoolteachernumbers;
        this.schooladdress = schooladdress;
        this.schoolcontactnumber = schoolcontactnumber;
    }

    public SchoolDetails(){

    }



    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getSchoolemail() {
        return schoolemail;
    }

    public void setSchoolemail(String schoolemail) {
        this.schoolemail = schoolemail;
    }

    public String getSchoolstudentnumber() {
        return schoolstudentnumber;
    }

    public void setSchoolstudentnumber(String schoolstudentnumber) {
        this.schoolstudentnumber = schoolstudentnumber;
    }

    public String getSchoolstaffnumbers() {
        return schoolstaffnumbers;
    }

    public void setSchoolstaffnumbers(String schoolstaffnumbers) {
        this.schoolstaffnumbers = schoolstaffnumbers;
    }

    public String getSchoolteachernumbers() {
        return schoolteachernumbers;
    }

    public void setSchoolteachernumbers(String schoolteachernumbers) {
        this.schoolteachernumbers = schoolteachernumbers;
    }

    public String getSchooladdress() {
        return schooladdress;
    }

    public void setSchooladdress(String schooladdress) {
        this.schooladdress = schooladdress;
    }

    public String getSchoolcontactnumber() {
        return schoolcontactnumber;
    }

    public void setSchoolcontactnumber(String schoolcontactnumber) {
        this.schoolcontactnumber = schoolcontactnumber;
    }
}
