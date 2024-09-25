package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.entity.User;
import com.openclassrooms.model.RegisterRequest;
import com.openclassrooms.service.RegistrationService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.openclassrooms.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * RegistrationController handles API requests for user registration.
 */
@RestController
@RequestMapping("/api/auth/register")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor to inject dependencies for RegistrationService and
     * JwtTokenProvider.
     *
     * @param registrationService the service to handle user registration
     * @param jwtTokenProvider    the provider to generate JWT tokens
     */
    @Autowired
    public RegistrationController(RegistrationService registrationService, JwtTokenProvider jwtTokenProvider) {
        this.registrationService = registrationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Handles the registration of a new user. Validates the required fields and
     * generates a JWT token if the registration is successful.
     * 
     * @param registerRequest the request body containing the user's name, email,
     *                        and password
     * @return a ResponseEntity containing the JWT token or an error response if
     *         validation fails
     */
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized ")
    })
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