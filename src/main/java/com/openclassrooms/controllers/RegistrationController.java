package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.entity.User;
import com.openclassrooms.model.RegisterRequest;
import com.openclassrooms.service.RegistrationService;
import com.openclassrooms.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/register")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public RegistrationController(RegistrationService registrationService, JwtTokenProvider jwtTokenProvider) {
        this.registrationService = registrationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Check for missing fields (name, email, password)
        if (registerRequest.getName() == null || registerRequest.getEmail() == null
                || registerRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new HashMap<>()); // Return 400 if any field is missing
        }

        // Register the user
        User user = registrationService.registerUser(registerRequest);

        // Generate JWT token (without roles)
        String token = jwtTokenProvider.createToken(user.getEmail());

        // Return the token in the response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response); // Return 200 with the token
    }
}