package com.dry_code_snippets.api.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Services.UserService;
import com.dry_code_snippets.api.Repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService actualUserService;

    @Mock
    private Jwt jwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User(UUID.fromString("00000000-0001-0828-5955-414980969538"));
        userRepository.save(mockUser);
        userService = new UserService(userRepository);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetClaim_withValidJwt_shouldReturnUUID() {
        String validUuid = "108285955414980969538"; 

        when(jwt.getClaim("sub")).thenReturn(validUuid);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);

        SecurityContextHolder.setContext(securityContext);

        UUID result = userService.getClaim();
        assertNotNull(result);
        assertEquals(UUID.fromString("00000000-0001-0828-5955-414980969538"), result);
    }

    @Test
    public void testGetClaim_withNullJwt_shouldReturnNull() {
        when(authentication.getPrincipal()).thenReturn(null);

        UUID actualClaim = userService.getClaim();

        assertNull(actualClaim);
    }

    @Test
    public void testGetClaim_withInvalidJwtClaim_shouldThrowException() {
        String invalidUuid = "123e4567e89b12d3a4564266"; 

        when(jwt.getClaim("sub")).thenReturn(invalidUuid);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);

        SecurityContextHolder.setContext(securityContext);

        assertThrows(IllegalArgumentException.class, () -> userService.getClaim());
    }

    @Test
    public void testGetClaim_withTooLongJwtClaim_shouldThrowException() {
        String tooLongUuid = "123e4567e89b12d3a4564266141740001234"; 

        when(jwt.getClaim("sub")).thenReturn(tooLongUuid);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);

        SecurityContextHolder.setContext(securityContext);

        assertThrows(IllegalArgumentException.class, () -> userService.getClaim());
    }
}
