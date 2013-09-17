package com.kl.nts.json;

public class AuthMessage {
    private String userName;
    private String password;

    public AuthMessage() {
    }

    public AuthMessage(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
