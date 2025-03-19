package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SharedServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private SnippetRepository snippetRepository;

    @InjectMocks
    private SharedService sharedService;

    private User mockUser;
    private Snippet mockSnippet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        mockUser.setUserId(1L);

        mockSnippet = new Snippet(1L, "Test Snippet", "Test Description", 101l);
        mockSnippet.setUserId(1L);
    }

    @Test
    void testOwnsResource_WhenUserOwnsSnippet_ShouldReturnTrue() {
        when(userService.getClaim()).thenReturn(mockUser.getUserGuid());
        when(userRepository.findByUserGuid(mockUser.getUserGuid())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));

        boolean result = sharedService.ownsResource(1L);

        assertTrue(result);
        verify(userRepository, times(1)).findByUserGuid(mockUser.getUserGuid());
        verify(snippetRepository, times(1)).findById(1L);
    }

    @Test
    void testOwnsResource_WhenUserDoesNotOwnSnippet_ShouldReturnFalse() {
        User anotherUser = new User(UUID.randomUUID());
        anotherUser.setUserId(2L);

        when(userService.getClaim()).thenReturn(anotherUser.getUserGuid());
        when(userRepository.findByUserGuid(anotherUser.getUserGuid())).thenReturn(Optional.of(anotherUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));

        boolean result = sharedService.ownsResource(1L);

        assertFalse(result);
        verify(userRepository, times(1)).findByUserGuid(anotherUser.getUserGuid());
        verify(snippetRepository, times(1)).findById(1L);
    }

    @Test
    void testResourceExists_WhenSnippetExists_ShouldReturnTrue() {
        
        String[] myArray1 = {"Java"};
        SnippetDTO snippetDTO = new SnippetDTO(2L, 1L, "some title", "some description", "java", LocalDateTime.now().toString(),
                "this is the code",BigDecimal.ONE,myArray1);

        when(snippetRepository.findSnippetDtoById(1L)).thenReturn(Optional.of(snippetDTO));

        boolean result = sharedService.resourceExists(1L);

        assertTrue(result);
        verify(snippetRepository, times(1)).findSnippetDtoById(1L);
    }

    @Test
    void testResourceExists_WhenSnippetDoesNotExist_ShouldReturnFalse() {
        when(snippetRepository.findSnippetDtoById(1L)).thenReturn(Optional.empty());

        boolean result = sharedService.resourceExists(1L);

        assertFalse(result);
        verify(snippetRepository, times(1)).findSnippetDtoById(1L);
    }
}
