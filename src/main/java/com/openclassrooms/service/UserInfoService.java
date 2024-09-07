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

    public UserInfoResponse getUserInfoFromToken(String token) {
        String email = jwtTokenProvider.getUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserInfoResponse(
                    user.getId(), // Assurez-vous que user.getId() renvoie un Integer
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt().toString(),
                    user.getUpdatedAt().toString());
        }

        return null;
    }
}