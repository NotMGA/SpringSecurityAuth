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

@RestController
@RequestMapping("/api/auth")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * Get user info
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
        String token = authorizationHeader.substring(7); // Remove the "Bearer "
        UserInfoResponse userInfoResponse = userInfoService.getUserInfoFromToken(token);
        return ResponseEntity.ok(userInfoResponse);
    }
}
