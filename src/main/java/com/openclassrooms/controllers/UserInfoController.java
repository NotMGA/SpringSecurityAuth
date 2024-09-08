package com.openclassrooms.controllers;

import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user information requests.
 * Provides an endpoint to retrieve user information based on the provided JWT
 * token.
 */
@RestController
@RequestMapping("/api/auth")
public class UserInfoController {

    private final UserInfoService userInfoService;

    /**
     * Constructor for injecting dependencies.
     * 
     * @param userInfoService the service responsible for fetching user info from
     *                        the token
     */
    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * Endpoint to retrieve user information based on a JWT token.
     * The token must be passed in the "Authorization" header with the "Bearer"
     * scheme.
     * 
     * @param authorizationHeader the authorization header containing the JWT token
     * @return ResponseEntity containing the user info or an error if the token is
     *         invalid
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Check if the authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}"); // Return 401 if token is missing
        }

        // Extract the JWT token from the header
        String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix

        try {
            // Use the service to get user information from the token
            UserInfoResponse userInfoResponse = userInfoService.getUserInfoFromToken(token);

            // Check if the service returned null (invalid token)
            if (userInfoResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token response null");
            }

            // Return the user information in the response
            return ResponseEntity.ok(userInfoResponse);

        } catch (Exception e) {
            // Catch any exceptions related to the token validation process
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token: " + e.getMessage());
        }
    }
}
