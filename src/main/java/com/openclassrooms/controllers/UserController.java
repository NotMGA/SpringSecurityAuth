package com.openclassrooms.controllers;

import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.service.UserService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint GET /user/{id}
    @GetMapping("/user/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized ")
    })
    public ResponseEntity<?> getUserById(@PathVariable Integer id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Vérifier la présence et validité du token JWT
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header error");
        }

        // Utiliser le service pour obtenir les informations de l'utilisateur par ID
        UserInfoResponse userInfo = userService.getUserById(id);
        // Retourner les informations de l'utilisateur
        return ResponseEntity.ok(userInfo);
    }
}
