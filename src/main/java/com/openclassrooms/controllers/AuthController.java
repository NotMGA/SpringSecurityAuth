package com.openclassrooms.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.model.LoginRequest;
import com.openclassrooms.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "User login", description = "Authenticates the user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login, returns JWT token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid username or password")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.loginUser(loginRequest);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid username or password"));
        }
    }
}
