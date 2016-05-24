package com.zanexess.messenger_tech_03.Objects;

public class Channel {
    private String chid;
    private String name;
    private String descr;
    private String online;

    public Channel() {
    }

    public Channel(String chid, String name, String descr, String online) {
        this.chid = chid;
        this.name = name;
        this.descr = descr;
        this.online = online;
    }

    public String getChid() {
        return chid;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public String getOnline() {
        return online;
    }
}
