package com.zanexess.messenger_tech_03.Messages;

public class UserInfoResponceData implements Data {
    private String status;
    private String error;
    private String nick;
    private String user_status;

    public UserInfoResponceData() {
    }

    public UserInfoResponceData(String status, String error, String nick, String user_status) {
        this.status = status;
        this.error = error;
        this.nick = nick;
        this.user_status = user_status;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getNick() {
        return nick;
    }

    public String getUser_status() {
        return user_status;
    }
}
