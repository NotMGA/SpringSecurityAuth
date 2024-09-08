package com.openclassrooms.Repository;

import com.openclassrooms.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing {@link Rental} entities in
 * the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 * <p>
 * Custom database queries for the {@link Rental} entity can be defined here if
 * needed.
 */
public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
