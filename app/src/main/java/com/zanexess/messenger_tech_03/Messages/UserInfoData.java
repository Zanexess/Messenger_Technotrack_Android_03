package com.zanexess.messenger_tech_03.Messages;

public class UserInfoData implements Data {
    private String user;
    private String cid;
    private String sid;

    public UserInfoData(String from) {
    }

    public UserInfoData(String user, String cid, String sid) {
        this.user = user;
        this.cid = cid;
        this.sid = sid;
    }

    public String getUser() {
        return user;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }
}
