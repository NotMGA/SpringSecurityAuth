package com.openclassrooms.service;

import com.openclassrooms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.entity.User;
import com.openclassrooms.model.RegisterRequest;

/**
 * Service responsible for user registration.
 * Handles the creation of new users and ensures proper validation.
 */
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for RegistrationService.
     *
     * @param userRepository  The repository to interact with User data.
     * @param passwordEncoder The encoder used to securely hash user passwords.
     */
    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user based on the provided request model.
     *
     * @param registerRequest The data needed to create a new user.
     * @return The created and saved User entity.
     * @throws IllegalArgumentException if the email is already in use.
     */
    public User registerUser(RegisterRequest registerRequest) {
        // Check if a user with the provided email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Create a new user entity and set the required fields
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Save and return the created user
        return userRepository.save(user);
    }
}