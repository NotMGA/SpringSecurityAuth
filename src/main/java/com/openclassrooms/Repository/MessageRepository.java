package com.openclassrooms.Repository;

import com.openclassrooms.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing {@link Message} entities in
 * the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
}
