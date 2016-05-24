package com.zanexess.messenger_tech_03.Messages;

public class EvEnterResponceData implements Data {
    private String chid;
    private String uid;
    private String nick;

    public EvEnterResponceData() {
    }

    public EvEnterResponceData(String chid, String uid, String nick) {
        this.chid = chid;
        this.uid = uid;
        this.nick = nick;
    }

    public String getChid() {
        return chid;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }
}
