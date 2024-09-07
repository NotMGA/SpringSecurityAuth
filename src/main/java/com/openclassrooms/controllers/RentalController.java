package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openclassrooms.entity.Rental;
import com.openclassrooms.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<?> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) throws IOException {

        String uploadDirectory = "D:\\Formation\\Openclassroom\\projet3\\SpringSecurityAuth\\src\\main\\resources\\image";
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.write(filePath, picture.getBytes()); // Enregistrer le fichier sur le disque

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setPicture("/images/" + fileName); // Enregistrer le chemin de l'image
        rental.setDescription(description);

        Rental savedRental = rentalService.createRental(rental);
        return ResponseEntity.ok(savedRental);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Integer id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @GetMapping
    public ResponseEntity<?> getRentalAll() {
        List<Rental> rentals = rentalService.getRentalByAll();

        if (rentals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Créer une réponse similaire à Mockoon
        Map<String, Object> response = new HashMap<>();
        response.put("rentals", rentals);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("description") String description) {

        // Rechercher la location à mettre à jour
        Rental updatedRental = new Rental();
        updatedRental.setName(name);
        updatedRental.setSurface(surface);
        updatedRental.setPrice(price);
        updatedRental.setDescription(description);

        // Appeler le service pour mettre à jour la location (sans gérer l'image)
        Rental savedRental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(savedRental);
    }

}

// Add more endpoints as needed for update, delete, list, etc.
