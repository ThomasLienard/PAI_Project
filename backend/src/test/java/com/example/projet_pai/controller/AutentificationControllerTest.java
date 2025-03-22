package com.example.projet_pai.controller;

import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.entite.Role;
import com.example.projet_pai.service.UserServiceItf;
import com.example.projet_pai.service.Impl.UserDetailsServiceImpl;
import com.example.projet_pai.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AutentificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AutentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

     @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean 
    private UserServiceItf userService;

    @MockBean
    private JwtUtil jwtUtil;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setUsername("testuser");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@example.com");
        validLoginRequest.setPassword("password123");

        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        String requestBody = objectMapper.writeValueAsString(validRegisterRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<RegisterRequest> registerRequestCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(userService, times(1)).registerUser(registerRequestCaptor.capture());
        assertEquals("testuser", registerRequestCaptor.getValue().getUsername());
    }

    @Test
    void testRegisterUser_Failure_EmailAlreadyExists() throws Exception {
        doThrow(new RuntimeException("Email déjà utilisé !")).when(userService).registerUser(any(RegisterRequest.class));

        String requestBody = objectMapper.writeValueAsString(validRegisterRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email déjà utilisé !"));
    }

    @Test
    void testRegisterUser_Failure_MissingData() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();
        System.out.println("email: " + invalidRequest.getEmail());
        System.out.println("username: " + invalidRequest.getEmail());
        System.out.println("mdp: " + invalidRequest.getEmail());

        String requestBody = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUser_Success() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(new Role("ADMIN"));

        when(userService.loginUser(any(LoginRequest.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mocked-jwt-token");

        String requestBody = objectMapper.writeValueAsString(validLoginRequest);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"testuser\",\"role\":\"ADMIN\",\"token\":\"mocked-jwt-token\"}"));
    }

    @Test
    void testLoginUser_Failure_InvalidCredentials() throws Exception {
        when(userService.loginUser(any(LoginRequest.class))).thenReturn(null);

        String requestBody = objectMapper.writeValueAsString(validLoginRequest);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Échec de la connexion !"));
    }
}