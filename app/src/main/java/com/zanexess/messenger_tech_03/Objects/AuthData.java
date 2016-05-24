package com.zanexess.messenger_tech_03.Objects;

public class AuthData {
    private String sid;
    private String cid;
    private String nick;

    public AuthData(String sid, String cid, String nick) {
        this.sid = sid;
        this.cid = cid;
        this.nick = nick;
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
