package com.zanexess.messenger_tech_03.Messages;

public class LeaveResponceData implements Data {
    private String status;
    private String error;

    public LeaveResponceData() {
    }

    public LeaveResponceData(String status, String error) {
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
