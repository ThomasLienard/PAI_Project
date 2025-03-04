package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");
    }

    @Test
    void registerUser_ShouldSaveUser_WhenDataIsValid() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");
        userService.registerUser(request);
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Utilisateur()));
        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Email déjà utilisé !", exception.getMessage());
        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenDataIsMissing() {
        RegisterRequest invalidRequest = new RegisterRequest();
        invalidRequest.setUsername("testuser");
        invalidRequest.setEmail(null);
        invalidRequest.setPassword(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(invalidRequest));
        assertEquals("Données manquantes", exception.getMessage());
    }

    @Test
    void registerUser_ShouldEncryptPasswordBeforeSaving() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");
        userService.registerUser(request);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    void registerUser_ShouldSaveUserInDatabase_WhenDataIsValid() {
        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setEmail(request.getEmail());
        expectedUser.setPassword("hashed_password");
        expectedUser.setUsername(request.getUsername());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");

        userService.registerUser(request);
        
        verify(userRepository).save(argThat(user -> user.getEmail().equals(request.getEmail()) &&
                user.getUsername().equals(request.getUsername()) &&
                user.getPassword().equals("hashed_password")));
    }

}
