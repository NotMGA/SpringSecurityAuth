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

@RestController
@RequestMapping("/api/rentals")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful "),
        @ApiResponse(responseCode = "401", description = "Unauthorized ")
})
public class RentalController {

    private final RentalService rentalService;

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
     * create a new rental.
     * 
     * @return ResponseEntity containing the created Rental object
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

        // Construct the full image URL
        String imageUrl = String.format("%s:%s/image/%s", serverUrl, serverPort, fileName);
        rental.setPicture(imageUrl);

        // Save the rental
        RentalModel rentalModel = rentalService.createRental(rental);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rental created!");
        response.put("rental", rentalModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
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
        RentalModel rentalModel = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(rentalModel);
    }
}
