package com.openclassrooms.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String name;
    private String password;

    // Getters and Setters

}