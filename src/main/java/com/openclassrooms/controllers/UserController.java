package com.openclassrooms.controllers;

import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.service.UserService;
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
    public ResponseEntity<?> getUserById(@PathVariable Integer id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Vérifier la présence et validité du token JWT
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        try {

            // Utiliser le service pour obtenir les informations de l'utilisateur par ID
            UserInfoResponse userInfo = userService.getUserById(id);

            // Si aucun utilisateur trouvé, retourner une erreur 404
            if (userInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Retourner les informations de l'utilisateur
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // En cas d'erreur de validation de token ou autre
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
        }
    }
}
