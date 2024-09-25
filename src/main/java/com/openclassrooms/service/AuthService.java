package com.openclassrooms.service;

import com.openclassrooms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.entity.User;
import com.openclassrooms.model.LoginRequest;
import com.openclassrooms.security.JwtTokenProvider;

/**
 * Service responsible for authentication logic.
 * This service handles the login process, including validating user credentials
 * and generating JWT tokens.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor for AuthService.
     *
     * @param userRepository   The repository to interact with User data.
     * @param passwordEncoder  The password encoder to verify user passwords.
     * @param jwtTokenProvider The provider to generate JWT tokens.
     */
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticates the user based on the provided login request.
     * If the credentials are valid, it generates a JWT token.
     *
     * @param loginRequest The login request containing the email and password.
     * @return A JWT token if the authentication is successful.
     * @throws UsernameNotFoundException If the user is not found.
     * @throws BadCredentialsException   If the provided credentials are invalid.
     */
    public String loginUser(LoginRequest loginRequest) {
        // Fetch user by email (login)
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT token with the user's email (no roles needed)
        return jwtTokenProvider.createToken(user.getEmail());
    }
}