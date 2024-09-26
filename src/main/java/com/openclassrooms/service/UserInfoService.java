package com.openclassrooms.service;

import com.openclassrooms.entity.User;
import com.openclassrooms.model.UserInfoResponse;
import com.openclassrooms.Repository.UserRepository;
import com.openclassrooms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserInfoService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Retrieves user information based on the provided JWT token.
     */
    public UserInfoResponse getUserInfoFromToken(String token) {
        String email = jwtTokenProvider.getUsername(token);

        // Find the user by email in the repository
        Optional<User> userOptional = userRepository.findByEmail(email);

        // If user exists, build and return a UserInfoResponse
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserInfoResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreated_at(),
                    user.getUpdated_at());
        }

        return null;
    }
}