package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Role;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.RoleRepository;
import com.example.projet_pai.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

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

        Role clientRole = new Role();
        clientRole.setName("CLIENT");

        // Simuler les comportements des dépendances
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        // Appeler la méthode à tester
        assertDoesNotThrow(() -> userService.registerUser(registerRequest));

        // Vérifier que le repository a été appelé avec les bonnes données
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Préparer les données
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password123");

        // Simuler un email déjà existant
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new Utilisateur()));

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));
        assertEquals("Email déjà utilisé !", exception.getMessage());
    }

    @Test
    void testRegisterUser_InvalidPassword() {
        // Préparer les données
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("weak");

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));
        assertEquals("Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule et un chiffre.", exception.getMessage());
    }

    @Test
    void testLoginUser_Success() {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        Utilisateur user = new Utilisateur();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        // Simuler les comportements des dépendances
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

        // Appeler la méthode à tester
        Utilisateur result = userService.loginUser(loginRequest);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testLoginUser_EmailNotFound() {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        // Simuler un email non trouvé
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.loginUser(loginRequest));
        assertEquals("Email ou mot de passe incorrect", exception.getMessage());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("WrongPassword");

        Utilisateur user = new Utilisateur();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        // Simuler les comportements des dépendances
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.loginUser(loginRequest));
        assertEquals("Email ou mot de passe incorrect", exception.getMessage());
    }
}
