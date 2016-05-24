package com.zanexess.messenger_tech_03.Messages;

public class EvMessageResponceData implements Data {
    private String chid;
    private String from;
    private String nick;
    private String body;

    public EvMessageResponceData() {
    }

    public EvMessageResponceData(String chid, String from, String nick, String body) {
        this.chid = chid;
        this.from = from;
        this.nick = nick;
        this.body = body;
    }

    public String getChid() {
        return chid;
    }

    public String getFrom() {
        return from;
    }

    public String getNick() {
        return nick;
    }

    public String getBody() {
        return body;
    }
}
