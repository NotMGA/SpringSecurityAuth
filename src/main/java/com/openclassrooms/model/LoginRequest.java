package com.openclassrooms.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;

    // Getters and Setters
    public String getLogin() {
        return email;
    }

    public void setLogin(String login) {
        this.email = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
