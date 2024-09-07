package com.openclassrooms.service;

import com.openclassrooms.Repository.RentalRepository;
import com.openclassrooms.entity.Rental;
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

    public Rental createRental(Rental rental) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assurez-vous que getName() vous donne l'ID utilisateur, sinon
        Integer userId = userService.getUserIdByEmail(userEmail); // adaptez selon votre implémentation d'UserDetails
        rental.setOwnerId(userId);
        rental.setCreatedAt(LocalDateTime.now()); // Set creation date
        rental.setUpdatedAt(LocalDateTime.now()); // Set update date

        if (rental.getDescription() != null && rental.getDescription().length() > 2000) {
            rental.setDescription(rental.getDescription().substring(0, 2000));
        }
        System.out.println("Creating rental with owner ID: " + userId);

        return rentalRepository.save(rental);

    }

    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id).orElseThrow(() -> new RuntimeException("Rental not found"));
    }

    public List<Rental> getRentalByAll() {
        List<Rental> rentals = rentalRepository.findAll();
        System.out.println("Rentals found: " + rentals.size());
        if (rentals.isEmpty()) {
            throw new RuntimeException("No rentals found");
        }
        return rentals;
    }

    public Rental updateRental(Integer id, Rental updatedRental) {
        // Rechercher la location existante dans la base de données
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Mettre à jour les champs de la location (sans toucher à l'image)
        existingRental.setName(updatedRental.getName());
        existingRental.setSurface(updatedRental.getSurface());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setDescription(updatedRental.getDescription());
        existingRental.setUpdatedAt(LocalDateTime.now()); // Mettre à jour la date de modification

        // Sauvegarder les modifications
        return rentalRepository.save(existingRental);
    }

    public boolean existsById(Integer id) {
        return rentalRepository.existsById(id);
    }
    // Additional methods to update, delete, etc.
}
