package com.openclassrooms.Repository;

import com.openclassrooms.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Message} entities in
 * the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
