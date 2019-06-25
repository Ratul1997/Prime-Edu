package com.primeedu.rat.primeedu.Class;

public class TeachersPostClass {
    private String msg;
    private String teacherid;
    private String teachername;

    public TeachersPostClass(String msg, String teacherid ,String teachername) {
        this.msg = msg;
        this.teacherid = teacherid;
        this.teachername = teachername;
    }

    public TeachersPostClass() {
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

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
