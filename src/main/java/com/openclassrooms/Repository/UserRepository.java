package com.openclassrooms.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.entity.User;

/**
 * Repository interface for accessing and managing {@link User} entities in the
 * database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 * <p>
 * Provides custom methods for querying users based on specific fields, such as
 * email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a {@link User} by their email address.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, or empty if
     *         not
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a {@link User} exists by their email address.
     *
     * @param email the email address to check
     * @return {@code true} if a user with the specified email exists, {@code false}
     *         otherwise
     */
    boolean existsByEmail(String email);
}
