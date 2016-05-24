package com.zanexess.messenger_tech_03.Messages;

public class SetUserInfoResponceData implements Data {
    private String status;
    private String error;

    public SetUserInfoResponceData() {

    }

    public SetUserInfoResponceData(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
