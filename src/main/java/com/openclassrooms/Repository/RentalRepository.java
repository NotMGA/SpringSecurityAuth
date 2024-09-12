package com.openclassrooms.Repository;

import com.openclassrooms.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Rental} entities in
 * the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 * <p>
 * Custom database queries for the {@link Rental} entity can be defined here if
 * needed.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
