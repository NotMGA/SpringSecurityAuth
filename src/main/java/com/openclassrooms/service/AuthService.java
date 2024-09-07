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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String loginUser(LoginRequest loginRequest) {
        // Fetch user by email (login)
        User user = userRepository.findByEmail(loginRequest.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT token with just the email (username), no roles needed
        String token = jwtTokenProvider.createToken(user.getEmail());

        return token; // Return the generated token
    }
}