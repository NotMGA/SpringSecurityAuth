package com.openclassrooms.service;

import com.openclassrooms.entity.User;
import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.Repository.UserRepository;
import com.openclassrooms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for retrieving user information from a JWT token.
 */
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor for UserInfoService.
     *
     * @param userRepository   Repository for interacting with user data.
     * @param jwtTokenProvider Service for handling JWT token operations.
     */
    @Autowired
    public UserInfoService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Retrieves user information based on the provided JWT token.
     *
     * @param token The JWT token from which to extract user information.
     * @return A UserInfoResponse object containing the user's information, or null
     *         if the user is not found.
     */
    public UserInfoResponse getUserInfoFromToken(String token) {
        // Extract email (username) from the JWT token
        String email = jwtTokenProvider.getUsername(token);

        // Find the user by email in the repository
        Optional<User> userOptional = userRepository.findByEmail(email);

        // If user exists, build and return a UserInfoResponse
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserInfoResponse(
                    user.getId(), // Ensure that user.getId() returns an Integer
                    user.getName(),
                    user.getEmail(),
                    user.getCreated_at(),
                    user.getUpdated_at());
        }

        // Return null if user is not found
        return null;
    }
}