package com.example.projet_pai.controller;

import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.service.Impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AutentificationController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class AutentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @InjectMocks
    private AutentificationController autentificationController;

    private RegisterRequest validRegisterRequest;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setUsername("testuser");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");

        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser_Success() throws Exception {

        String requestBody = objectMapper.writeValueAsString(validRegisterRequest);


        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Utilisateur enregistré avec succès !"));


        ArgumentCaptor<RegisterRequest> captor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(userService, times(1)).registerUser(captor.capture());

        RegisterRequest capturedRequest = captor.getValue();
        assertEquals(validRegisterRequest.getUsername(), capturedRequest.getUsername());
        assertEquals(validRegisterRequest.getEmail(), capturedRequest.getEmail());
        assertEquals(validRegisterRequest.getPassword(), capturedRequest.getPassword());
    }

    @Test
    void testRegisterUser_Failure_EmailAlreadyExists() throws Exception {

        String requestBody = objectMapper.writeValueAsString(validRegisterRequest);
        doThrow(new RuntimeException("Email déjà utilisé !")).when(userService).registerUser(any(RegisterRequest.class));


        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email déjà utilisé !"));


        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
    }

    @Test
    void testRegisterUser_Failure_MissingData() throws Exception {

        RegisterRequest invalidRequest = new RegisterRequest();
        invalidRequest.setUsername("testuser");
        String requestBody = objectMapper.writeValueAsString(invalidRequest);

        doThrow(new RuntimeException("Données manquantes")).when(userService).registerUser(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Données manquantes"));

        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
    }
}