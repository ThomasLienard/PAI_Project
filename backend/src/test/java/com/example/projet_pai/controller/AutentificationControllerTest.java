package com.example.projet_pai.controller;

import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.LoginResponse;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Role;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.UserServiceItf;
import com.example.projet_pai.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AutentificationControllerTest {

    @Mock
    private UserServiceItf userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AutentificationController autentificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Préparer les données
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password123");

        // Simuler le comportement du service
        doNothing().when(userService).registerUser(registerRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = autentificationController.registerUser(registerRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Utilisateur enregistré avec succès !", response.getBody());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Préparer les données
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password123");

        // Simuler une exception levée par le service
        doThrow(new RuntimeException("Email déjà utilisé !")).when(userService).registerUser(registerRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = autentificationController.registerUser(registerRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email déjà utilisé !", response.getBody());
    }

    @Test
    void testLogin_Success() {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        String token = "mocked-jwt-token";

        // Simuler le comportement du service et du JWT utilitaire
        when(userService.loginUser(loginRequest)).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().getName())).thenReturn(token);

        // Appeler le endpoint
        ResponseEntity<?> response = autentificationController.login(loginRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponse loginResponse = (LoginResponse) response.getBody();
        assertEquals("testuser", loginResponse.getUsername());
        assertEquals("CLIENT", loginResponse.getRole());
        assertEquals(token, loginResponse.getToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("WrongPassword");

        // Simuler une exception levée par le service
        when(userService.loginUser(loginRequest)).thenThrow(new RuntimeException("Email ou mot de passe incorrect"));

        // Appeler le endpoint
        ResponseEntity<?> response = autentificationController.login(loginRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email ou mot de passe incorrect", response.getBody());
    }
}