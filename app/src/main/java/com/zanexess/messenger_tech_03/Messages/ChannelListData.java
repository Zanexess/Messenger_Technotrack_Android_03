package com.zanexess.messenger_tech_03.Messages;

public class ChannelListData implements Data {
    private String cid;
    private String sid;

    public ChannelListData() {

    }

    public ChannelListData(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }
}
