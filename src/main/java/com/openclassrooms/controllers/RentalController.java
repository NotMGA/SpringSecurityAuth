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

/**
 * Controller for managing rental properties. It provides endpoints for creating, retrieving, 
 * and updating rentals.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    /**
     * Constructor that injects RentalService.
     * 
     * @param rentalService the service responsible for handling rental operations
     */
    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Endpoint to create a new rental.
     * 
     * @param name        the name of the rental
     * @param surface     the surface area of the rental
     * @param price       the price of the rental
     * @param picture     the picture file of the rental
     * @param description the description of the rental
     * @return ResponseEntity containing the created Rental object
     * @throws IOException if there's an issue saving the picture file
     */
    @PostMapping
    public ResponseEntity<?> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) throws IOException {

        // Define the directory to store images
        String uploadDirectory = "D:\\Formation\\Openclassroom\\projet3\\SpringSecurityAuth\\src\\main\\resources\\image";
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.write(filePath, picture.getBytes()); // Save the picture on disk

        // Create a new rental object and set its properties
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setPicture("/images/" + fileName); // Save the image path
        rental.setDescription(description);

        // Save the rental and return the response
        Rental savedRental = rentalService.createRental(rental);
        return ResponseEntity.ok(savedRental);
    }

    /**
     * Endpoint to retrieve a rental by its ID.
     * 
     * @param id the ID of the rental
     * @return ResponseEntity containing the Rental object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Integer id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    /**
     * Endpoint to retrieve all rentals.
     * 
     * @return ResponseEntity containing a list of rentals, or a 204 No Content status if no rentals exist
     */
    @GetMapping
    public ResponseEntity<?> getRentalAll() {
        List<Rental> rentals = rentalService.getRentalByAll();

        if (rentals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Create a response similar to the Mockoon API format
        Map<String, Object> response = new HashMap<>();
        response.put("rentals", rentals);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update a rental by its ID.
     * 
     * @param id          the ID of the rental to update
     * @param name        the updated name of the rental
     * @param surface     the updated surface area of the rental
     * @param price       the updated price of the rental
     * @param description the updated description of the rental
     * @return ResponseEntity containing the updated Rental object
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("description") String description) {

        // Create a rental object with updated details
        Rental updatedRental = new Rental();
        updatedRental.setName(name);
        updatedRental.setSurface(surface);
        updatedRental.setPrice(price);
        updatedRental.setDescription(description);

        // Update the rental and return the response
        Rental savedRental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(savedRental);
    }
}
