package com.openclassrooms.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.model.LoginRequest;
import com.openclassrooms.service.AuthService;

/**
 * AuthController handles authentication requests such as login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor that injects AuthService for authentication operations.
     * 
     * @param authService the authentication service
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles the login request and returns a JWT token upon successful
     * authentication.
     * 
     * @param loginRequest the login request containing username and password
     * @return a ResponseEntity containing a JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginUser(loginRequest);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
