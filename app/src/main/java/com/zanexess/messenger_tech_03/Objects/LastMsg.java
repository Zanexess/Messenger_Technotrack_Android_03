package com.zanexess.messenger_tech_03.Objects;

public class LastMsg {
    private String mid;
    private String from;
    private String nick;
    private String body;
    private String time;

    public LastMsg() {
    }

    public LastMsg(String mid, String from, String nick, String body, String time) {
        this.mid = mid;
        this.from = from;
        this.nick = nick;
        this.body = body;
        this.time = time;
    }

    public String getMid() {
        return mid;
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

    public String getTime() {
        return time;
    }
}
