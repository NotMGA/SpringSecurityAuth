package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openclassrooms.entity.Rental;
import com.openclassrooms.model.RentalModel;
import com.openclassrooms.service.RentalService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller for managing rental properties. It provides endpoints for
 * creating, retrieving,
 * and updating rentals.
 */
@RestController
@RequestMapping("/api/rentals")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful "),
        @ApiResponse(responseCode = "401", description = "Unauthorized ")
})
public class RentalController {

    private final RentalService rentalService;

    /**
     * Constructor that injects RentalService.
     * 
     * @param rentalService the service responsible for handling rental operations
     */
    // Inject the upload directory from application.properties
    @Value("${file.image-dir}")
    private String imageDir;

    // Inject server URL (e.g., http://localhost:3001)
    @Value("${server.url:http://localhost}")
    private String serverUrl;

    @Value("${server.port:3001}")
    private String serverPort;

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
            @RequestParam("description") String description,
            Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Return 401 Unauthorized if the user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }
        // Define the path to store the image in the upload directory
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get(imageDir, fileName);

        // Ensure the directories exist, or create them if not
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Save the picture on disk
        Files.write(filePath, picture.getBytes());

        // Create a new rental object and set its properties
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);

        rental.setDescription(description);

        // Construct the full image URL including http://localhost:8080
        String imageUrl = String.format("%s:%s/image/%s", serverUrl, serverPort, fileName);
        rental.setPicture(imageUrl);

        // Save the rental
        RentalModel rentalModel = rentalService.createRental(rental); // Save and return RentalModel
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rental created!");
        response.put("rental", rentalModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint to retrieve a rental by its ID.
     * 
     * @param id the ID of the rental
     * @return ResponseEntity containing the Rental object
     */
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized ")
    })
    public ResponseEntity<RentalModel> getRentalById(@PathVariable Integer id, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            // Return 401 Unauthorized if the user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        RentalModel rentalModel = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalModel);
    }

    /**
     * Endpoint to retrieve all rentals.
     * 
     * @return ResponseEntity containing a list of rentals, or a 204 No Content
     *         status if no rentals exist
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized ")
    })
    public ResponseEntity<?> getRentalAll(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            // Return 401 Unauthorized if the user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<RentalModel> rentals = rentalService.getRentalByAll();
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
        RentalModel rentalModel = rentalService.updateRental(id, updatedRental); // Convert to RentalModel
        return ResponseEntity.ok(rentalModel);
    }
}
