package com.example.rat.primeedu.Class;

public class TeachersNotice {
    private String msg;
    private String teacherid;

    public TeachersNotice(String msg, String teacherid) {
        this.msg = msg;
        this.teacherid = teacherid;
    }

    public TeachersNotice(){}
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }
}
