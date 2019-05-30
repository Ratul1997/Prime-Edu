package com.example.rat.primeedu.Class;

public class NoticeDetails {
    private String msg;
    private String msgUrl;

    public NoticeDetails(String msg, String msgUrl) {
        this.msg = msg;
        this.msgUrl = msgUrl;
    }

    public NoticeDetails() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }
}
