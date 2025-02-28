package com.example.projet_pai.controller;

import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AutentificationController.class)
class AutentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AutentificationController autentificationController;

    private RegisterRequest validRegisterRequest;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setUsername("testuser");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");
        mockMvc = MockMvcBuilders.standaloneSetup(autentificationController).build();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        ArgumentCaptor<RegisterRequest> captor = ArgumentCaptor.forClass(RegisterRequest.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Utilisateur enregistré avec succès !"));

        verify(userService, times(1)).registerUser(captor.capture());

        RegisterRequest capturedRequest = captor.getValue();
        assertEquals(request.getUsername(), capturedRequest.getUsername());
        assertEquals(request.getEmail(), capturedRequest.getEmail());
        assertEquals(request.getPassword(), capturedRequest.getPassword());
    }

    @Test
    void testRegisterUser_Failure_EmailAlreadyExists() throws Exception {
        // Simule l'exception générique lancée par le service
        doThrow(new RuntimeException("Email déjà utilisé !")).when(userService).registerUser(validRegisterRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // On attend un status 400 (Bad Request)
                .andExpect(MockMvcResultMatchers.content().string("Email déjà utilisé !")); // Le message d'erreur

        verify(userService).registerUser(validRegisterRequest); // Vérifie que la méthode registerUser a bien été
                                                                // appelée
    }

    @Test
    void testRegisterUser_Failure_MissingData() throws Exception {
        // Simule une exception pour données manquantes
        doThrow(new RuntimeException("Données manquantes")).when(userService).registerUser(any(RegisterRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\"}")) // Données manquantes : email, password
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // On attend un status 400 (Bad Request)
                .andExpect(MockMvcResultMatchers.content().string("Données manquantes")); // Le message d'erreur

        verify(userService).registerUser(any(RegisterRequest.class)); // Vérifie que la méthode registerUser a bien été
                                                                      // appelée
    }

}
