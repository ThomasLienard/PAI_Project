package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.AdminRegisterRequest;
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

class AdminUserManagementServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserManagementServiceImpl adminUserManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserWithRole_Success() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("ADMIN");

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        // Simuler les comportements des dépendances
        when(userRepository.findByEmail(adminRegisterRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(adminRegisterRequest.getRole())).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(adminRegisterRequest.getPassword())).thenReturn("encodedPassword");

        // Appeler la méthode à tester
        assertDoesNotThrow(() -> adminUserManagementService.createUserWithRole(adminRegisterRequest));

        // Vérifier que le repository a été appelé avec les bonnes données
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testCreateUserWithRole_EmailAlreadyExists() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("ADMIN");

        // Simuler un email déjà existant
        when(userRepository.findByEmail(adminRegisterRequest.getEmail())).thenReturn(Optional.of(new Utilisateur()));

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminUserManagementService.createUserWithRole(adminRegisterRequest));
        assertEquals("Email déjà utilisé !", exception.getMessage());
    }

    @Test
    void testCreateUserWithRole_InvalidPassword() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("weak");
        adminRegisterRequest.setRole("ADMIN");

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminUserManagementService.createUserWithRole(adminRegisterRequest));
        assertEquals("Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule et un chiffre.", exception.getMessage());
    }

    @Test
    void testCreateUserWithRole_RoleNotFound() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("UNKNOWN_ROLE");

        // Simuler un rôle non trouvé
        when(userRepository.findByEmail(adminRegisterRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(adminRegisterRequest.getRole())).thenReturn(Optional.empty());

        // Appeler la méthode à tester et vérifier l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminUserManagementService.createUserWithRole(adminRegisterRequest));
        assertEquals("Role not found", exception.getMessage());
    }
}
