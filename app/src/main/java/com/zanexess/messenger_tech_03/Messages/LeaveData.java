package com.zanexess.messenger_tech_03.Messages;

public class LeaveData implements Data {
    private String cid;
    private String sid;
    private String channel;

    public LeaveData() {
    }

    public LeaveData(String cid, String sid, String channel) {
        this.cid = cid;
        this.sid = sid;
        this.channel = channel;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public String getChannel() {
        return channel;
    }
}