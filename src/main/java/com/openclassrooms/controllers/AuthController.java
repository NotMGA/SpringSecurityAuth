package com.openclassrooms.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.entity.User;
import com.openclassrooms.model.LoginRequest;
import com.openclassrooms.model.RegisterRequest;
import com.openclassrooms.service.AuthService;
import com.openclassrooms.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginUser(loginRequest);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

}
