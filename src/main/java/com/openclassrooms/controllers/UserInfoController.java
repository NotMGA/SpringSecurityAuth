package com.openclassrooms.controllers;

import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.service.UserInfoService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized ")
    })
    public ResponseEntity<?> getUserInfo(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Check if the authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}"); // Return 401 if token is missing
        }
        // Extract the JWT token from the header
        String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix
        // Use the service to get user information from the token
        UserInfoResponse userInfoResponse = userInfoService.getUserInfoFromToken(token);
        // Return the user information in the response
        return ResponseEntity.ok(userInfoResponse);
    }
}
