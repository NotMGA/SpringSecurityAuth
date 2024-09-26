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

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserService userService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.userService = userService;
    }

    /**
     * Creates a new rental and assigns it to the authenticated user.
     */
    public RentalModel createRental(Rental rental) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Integer userId = userService.getUserIdByEmail(userEmail);
        rental.setOwner_id(userId);
        rental.setCreated_at(LocalDateTime.now());
        rental.setUpdated_at(LocalDateTime.now());

        // Limit the description length to 2000 characters
        if (rental.getDescription() != null && rental.getDescription().length() > 2000) {
            rental.setDescription(rental.getDescription().substring(0, 2000));
        }
        System.out.println("Creating rental with owner ID: " + userId);
        Rental savedRental = rentalRepository.save(rental);

        return new RentalModel(savedRental);
    }

    /**
     * Retrieves a rental property by its ID.
     */
    public RentalModel getRentalById(Integer id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        return new RentalModel(rental);
    }

    /**
     * Retrieves all rental properties from the database.
     */
    public List<RentalModel> getRentalByAll() {
        List<Rental> rentals = rentalRepository.findAll();
        System.out.println("Rentals found: " + rentals.size());
        if (rentals.isEmpty()) {
            throw new RuntimeException("No rentals found");
        }
        return rentals.stream()
                .map(RentalModel::new)
                .toList();
    }

    /**
     * Updates an existing rental property with new values.
     */
    public RentalModel updateRental(Integer id, Rental updatedRental) {
        // Find the existing rental in the database.
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Update the rental fields.
        existingRental.setName(updatedRental.getName());
        existingRental.setSurface(updatedRental.getSurface());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setDescription(updatedRental.getDescription());
        existingRental.setUpdated_at(LocalDateTime.now());
        Rental savedRental = rentalRepository.save(existingRental);
        return new RentalModel(savedRental);
    }

    /**
     * Checks if a rental exists by its ID.
     */
    public boolean existsById(Integer id) {
        return rentalRepository.existsById(id);
    }

    public Integer getUserIdByEmail(String email) {
        return userService.getUserIdByEmail(email);
    }
}
