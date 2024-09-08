package com.openclassrooms.service;

import com.openclassrooms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.entity.User;
import com.openclassrooms.model.LoginRequest;
import com.openclassrooms.model.RegisterRequest;
import com.openclassrooms.security.JwtTokenProvider;

import java.util.Collections;

/**
 * Service responsible for managing users and handling authentication via Spring
 * Security.
 * This class implements the UserDetailsService interface, which is used for
 * loading user-specific data.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository Repository for interacting with user data.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email address, returning the user's details for
     * authentication.
     *
     * @param email The email address of the user.
     * @return UserDetails containing the user's email, password, and role.
     * @throws UsernameNotFoundException if the user with the given email is not
     *                                   found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create a UserDetails object with the user's email, password, and a default
        // role.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    /**
     * Retrieves the user ID based on their email.
     *
     * @param email The email address of the user.
     * @return The user ID as an Integer.
     * @throws UsernameNotFoundException if the user with the given email is not
     *                                   found.
     */
    public Integer getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Checks whether a user with the given ID exists in the repository.
     *
     * @param id The ID of the user.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }
}