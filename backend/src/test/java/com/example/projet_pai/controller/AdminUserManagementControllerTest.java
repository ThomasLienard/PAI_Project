package com.example.projet_pai.controller;

import com.example.projet_pai.dto.AdminRegisterRequest;
import com.example.projet_pai.service.AdminUserManagementServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminUserManagementControllerTest {

    @Mock
    private AdminUserManagementServiceItf adminUserManagementService;

    @InjectMocks
    private AdminUserManagementController adminUserManagementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("ADMIN");

        // Simuler le comportement du service
        doNothing().when(adminUserManagementService).createUserWithRole(adminRegisterRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = adminUserManagementController.createUser(adminRegisterRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Utilisateur créé avec succès !", response.getBody());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("ADMIN");

        // Simuler une exception levée par le service
        doThrow(new RuntimeException("Email déjà utilisé !")).when(adminUserManagementService).createUserWithRole(adminRegisterRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = adminUserManagementController.createUser(adminRegisterRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email déjà utilisé !", response.getBody());
    }

    @Test
    void testCreateUser_InvalidPassword() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("weak");
        adminRegisterRequest.setRole("ADMIN");

        // Simuler une exception levée par le service
        doThrow(new RuntimeException("Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule et un chiffre."))
                .when(adminUserManagementService).createUserWithRole(adminRegisterRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = adminUserManagementController.createUser(adminRegisterRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule et un chiffre.", response.getBody());
    }

    @Test
    void testCreateUser_RoleNotFound() {
        // Préparer les données
        AdminRegisterRequest adminRegisterRequest = new AdminRegisterRequest();
        adminRegisterRequest.setUsername("adminuser");
        adminRegisterRequest.setEmail("admin@example.com");
        adminRegisterRequest.setPassword("Password123");
        adminRegisterRequest.setRole("UNKNOWN_ROLE");

        // Simuler une exception levée par le service
        doThrow(new RuntimeException("Role not found")).when(adminUserManagementService).createUserWithRole(adminRegisterRequest);

        // Appeler le endpoint
        ResponseEntity<String> response = adminUserManagementController.createUser(adminRegisterRequest);

        // Vérifier la réponse
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role not found", response.getBody());
    }
}