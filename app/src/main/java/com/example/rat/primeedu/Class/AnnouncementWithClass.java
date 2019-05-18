package com.example.rat.primeedu.Class;

public class AnnouncementWithClass {
    private String MSg;
    private String Classes;

    public String getMSg() {
        return MSg;
    }

    public void setMSg(String MSg) {
        this.MSg = MSg;
    }

    public String getClasses() {
        return Classes;
    }

    public void setClasses(String classes) {
        Classes = classes;
    }

    public AnnouncementWithClass(String MSg, String classes) {

        this.MSg = MSg;
        Classes = classes;
    }
}
