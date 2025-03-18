package com.dry_code_snippets.api.Snippets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.dry_code_snippets.api.Services.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @Mock
    private Jwt jwt;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and the UserService instance
        MockitoAnnotations.openMocks(this);
        userService = new UserService(null);

        // Mock SecurityContextHolder to return the mocked authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetClaim_withValidJwt_shouldReturnUUID() {
        // Arrange
        String mockClaim = "123e4567e89b12d3a456426614174000"; // This is a mock UUID string in hex
        UUID expectedUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // Mock the JWT token and its "sub" claim
        when(authentication.getPrincipal()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(jwt.getClaim("sub")).thenReturn(mockClaim);

        // Act
        UUID actualClaim = userService.getClaim();

        // Assert
        assertEquals(expectedUUID, actualClaim);
    }

    @Test
    public void testGetClaim_withNullJwt_shouldReturnNull() {
        // Arrange: No JwtAuthenticationToken mock, authentication is not valid.
        when(authentication.getPrincipal()).thenReturn(null);

        // Act
        UUID actualClaim = userService.getClaim();

        // Assert
        assertNull(actualClaim);
    }

}
