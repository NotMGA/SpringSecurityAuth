package com.openclassrooms.Repository;

import com.openclassrooms.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Custom database queries can be defined here
}
