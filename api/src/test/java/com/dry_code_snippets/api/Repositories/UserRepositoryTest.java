package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private UUID userGuid;

    @BeforeEach
    void setUp() {
        // Setup test data before each test
        userGuid = UUID.randomUUID();
        user = new User(userGuid);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        userRepository.deleteAll();
    }

    @Test
    void testFindByUserGuid_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findByUserGuid(userGuid);

        assertTrue(foundUser.isPresent());
        assertEquals(userGuid, foundUser.get().getUserGuid());
    }

    @Test
    void testFindByUserGuid_WhenUserDoesNotExist_ShouldReturnEmpty() {
        UUID nonExistingGuid = UUID.randomUUID();
        Optional<User> foundUser = userRepository.findByUserGuid(nonExistingGuid);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUser_ShouldPersistUser() {
        UUID newGuid = UUID.randomUUID();
        User newUser = new User(newGuid);
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getUserId());
        assertEquals(newGuid, savedUser.getUserGuid());
    }
}
