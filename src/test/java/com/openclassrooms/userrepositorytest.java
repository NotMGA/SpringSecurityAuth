package com.openclassrooms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.entity.User;
import com.openclassrooms.Repository.UserRepository;

import java.util.Optional;
import java.util.List;

@SpringBootTest
public class userrepositorytest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("test@test.com");
        assert (user.isPresent());
    }

    /*
     * @Test
     * public void testFindUsersWithRentals() {
     * List<User> users = userRepository.findUsersWithRentals();
     * assert (!users.isEmpty());
     * }
     */
}
