package com.zanexess.messenger_tech_03.Objects;

public class User {
    private String uid;
    private String nick;

    public User() {
    }

    public User(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }
}
