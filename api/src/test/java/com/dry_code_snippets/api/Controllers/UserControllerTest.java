package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testLogin_ShouldReturnOkWithJwt() throws Exception {
        String authCode = "validAuthCode";
        String mockJwt = "mockJwtToken";

        when(userService.login(authCode)).thenReturn(mockJwt);

        mockMvc.perform(post("/api/login")
                .content(authCode)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(mockJwt));

        verify(userService, times(1)).login(authCode);
    }

    @Test
    void testLogin_WhenLoginFails_ShouldReturnInternalServerError() throws Exception {
        String authCode = "invalidAuthCode";

        when(userService.login(authCode)).thenReturn(null);

        mockMvc.perform(post("/api/login")
                .content(authCode)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).login(authCode);
    }
}
