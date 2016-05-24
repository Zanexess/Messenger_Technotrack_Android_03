package com.zanexess.messenger_tech_03.Messages;

public class LoginResponceData implements Data {
    private String status;
    private String error;
    private String sid;
    private String cid;
    private String nick;

    public LoginResponceData() {

    }

    public LoginResponceData(String status, String error, String sid, String cid, String nick) {
        this.status = status;
        this.error = error;
        this.sid = sid;
        this.cid = cid;
        this.nick = nick;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getSid() {
        return sid;
    }

    public String getCid() {
        return cid;
    }

    public String getNick() {
        return nick;
    }
}
