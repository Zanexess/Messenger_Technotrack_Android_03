package com.zanexess.messenger_tech_03.Messages;

public class CreateChannelResponceData implements Data {
    private String status;
    private String error;
    private String chid;

    public CreateChannelResponceData() {
    }

    public CreateChannelResponceData(String status, String error, String chid) {
        this.status = status;
        this.error = error;
        this.chid = chid;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getChid() {
        return chid;
    }
}
