package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        UUID userGuid = UUID.randomUUID();
        User user = new User(userGuid);

        assertNotNull(user);
        assertEquals(userGuid, user.getUserGuid());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        UUID userGuid = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 19, 12, 0);

        user.setUserId(1L);
        user.setUserGuid(userGuid);
        user.setCreatedAt(createdAt);

        assertEquals(1L, user.getUserId());
        assertEquals(userGuid, user.getUserGuid());
        assertEquals(createdAt, user.getCreatedAt());
    }
}
