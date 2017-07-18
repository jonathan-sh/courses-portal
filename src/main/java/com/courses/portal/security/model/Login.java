package com.courses.portal.security.model;

import com.courses.portal.security.Encryption;

public class Login {

    private String username;
    private String password;

    public Login() {
        super();
    }

    public Login(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = Encryption.generateHash(password);
    }

}
