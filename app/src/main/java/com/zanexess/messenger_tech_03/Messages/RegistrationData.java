package com.zanexess.messenger_tech_03.Messages;

public class RegistrationData implements Data {
    private String login;
    private String pass;
    private String nick;

    public RegistrationData() {

    }

    public RegistrationData(String login, String pass, String nick) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }
}
