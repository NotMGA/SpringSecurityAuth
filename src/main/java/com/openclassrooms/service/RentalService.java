package com.openclassrooms.service;

import com.openclassrooms.Repository.RentalRepository;
import com.openclassrooms.entity.Rental;
import com.openclassrooms.model.RentalModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;

/**
 * Service responsible for managing rental properties.
 * This service handles the creation, retrieval, and updating of rental
 * properties.
 */
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserService userService;

    /**
     * Constructor for RentalService.
     *
     * @param rentalRepository Repository to interact with rental data.
     * @param userService      Service to manage user-related operations.
     */
    @Autowired
    public RentalService(RentalRepository rentalRepository, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.userService = userService;
    }

    /**
     * Creates a new rental property and assigns it to the authenticated user.
     *
     * @param rental The rental property to be created.
     * @return The created and saved rental property.
     */
    public RentalModel createRental(Rental rental) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Retrieve the authenticated user's email.
        Integer userId = userService.getUserIdByEmail(userEmail); // Get user ID from email.
        rental.setOwner_id(userId); // Assign the owner ID to the rental.
        rental.setCreated_at(LocalDateTime.now()); // Set the creation timestamp.
        rental.setUpdated_at(LocalDateTime.now()); // Set the update timestamp.

        // Limit the description length to 2000 characters if necessary.
        if (rental.getDescription() != null && rental.getDescription().length() > 2000) {
            rental.setDescription(rental.getDescription().substring(0, 2000));
        }
        System.out.println("Creating rental with owner ID: " + userId);
        Rental savedRental = rentalRepository.save(rental);

        return new RentalModel(savedRental); // Save the rental in the database.
    }

    /**
     * Retrieves a rental property by its ID.
     *
     * @param id The ID of the rental property to retrieve.
     * @return The rental property found by the provided ID.
     * @throws RuntimeException If the rental property is not found.
     */
    public RentalModel getRentalById(Integer id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Convert to RentalModel
        return new RentalModel(rental);
    }

    /**
     * Retrieves all rental properties from the database.
     *
     * @return A list of all rental properties.
     * @throws RuntimeException If no rental properties are found.
     */
    public List<RentalModel> getRentalByAll() {
        List<Rental> rentals = rentalRepository.findAll();
        System.out.println("Rentals found: " + rentals.size());
        if (rentals.isEmpty()) {
            throw new RuntimeException("No rentals found");
        }
        return rentals.stream()
                .map(RentalModel::new) // Mapping Rental to RentalModel
                .toList();
    }

    /**
     * Updates an existing rental property with new values.
     *
     * @param id            The ID of the rental property to update.
     * @param updatedRental The updated rental property data.
     * @return The updated rental property.
     * @throws RuntimeException If the rental property is not found.
     */
    public RentalModel updateRental(Integer id, Rental updatedRental) {
        // Find the existing rental in the database.
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Update the rental fields (excluding image handling).
        existingRental.setName(updatedRental.getName());
        existingRental.setSurface(updatedRental.getSurface());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setDescription(updatedRental.getDescription());
        existingRental.setUpdated_at(LocalDateTime.now()); // Update the timestamp.

        // Save and return the updated rental.
        Rental savedRental = rentalRepository.save(existingRental);
        return new RentalModel(savedRental);
    }

    /**
     * Checks if a rental property exists by its ID.
     *
     * @param id The ID of the rental property to check.
     * @return true if the rental property exists, false otherwise.
     */
    public boolean existsById(Integer id) {
        return rentalRepository.existsById(id);
    }

    public Integer getUserIdByEmail(String email) {
        return userService.getUserIdByEmail(email);
    }
}
