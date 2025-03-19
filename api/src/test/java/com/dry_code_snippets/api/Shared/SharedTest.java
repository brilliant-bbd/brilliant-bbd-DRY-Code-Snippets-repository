package com.dry_code_snippets.api.Shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SharedTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetClaim_WithValidJwt_ShouldReturnUUID() {
        String validUuid = "108285955414980969538";

        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(jwt.getClaim("sub")).thenReturn(validUuid);

        UUID result = Shared.getClaim();

        assertNotNull(result);
        assertEquals(UUID.fromString("00000000-0001-0828-5955-414980969538"), result);
    }

    @Test
    void testGetClaim_WithNullClaim_ShouldReturnNull() {
        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getToken()).thenReturn(jwt);
        when(jwt.getClaim("sub")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Shared.getClaim();
        });

        assertEquals("String is too long or invalid for a valid UUID.", exception.getMessage());
    }

    @Test
    void testConvertStringToUUID_ValidString_ShouldReturnUUID() {
        String validUuid = "108285955414980969538";
        UUID result = Shared.convertStringToUUID(validUuid);
        assertEquals(UUID.fromString("00000000-0001-0828-5955-414980969538"), result);
    }

    @Test
    void testConvertStringToUUID_TooLongString_ShouldThrowException() {
        String tooLongUuid = "123e4567e89b12d3a4564266141740001234";
        assertThrows(IllegalArgumentException.class, () -> Shared.convertStringToUUID(tooLongUuid));
    }

    @Test
    void testConvertStringToUUID_NullString_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Shared.convertStringToUUID(null));
    }
}
