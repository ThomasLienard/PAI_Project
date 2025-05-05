package com.example.projet_pai.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.projet_pai.dto.ServerReservationDTO;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.ReservationRepository;

class ServerReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    
    @InjectMocks
    private ServerReservationServiceImpl serverReservationService;
    
    private Reservation reservation1;
    private Reservation reservation2;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Préparer les données de test
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Créer un utilisateur
        Utilisateur client = new Utilisateur();
        client.setId(1L);
        client.setUsername("Jean Dupont");
        client.setEmail("jean@example.com");
        
        // Créer une table
        Table table1 = new Table();
        table1.setId(1L);
        table1.setNumero(5);
        table1.setCapacite(4);
        
        Table table2 = new Table();
        table2.setId(2L);
        table2.setNumero(10);
        table2.setCapacite(6);
        
        // Créer une réservation
        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setDateReservation(today);
        reservation1.setCreneauHoraire("midi");
        reservation1.setNbPersonne(3);
        reservation1.setClient(client);
        reservation1.setTable(table1);
        
        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setDateReservation(today);
        reservation2.setCreneauHoraire("soir");
        reservation2.setNbPersonne(5);
        reservation2.setClient(client);
        reservation2.setTable(table2);
    }
    
    @Test
    void testGetTodayReservations_WithData() {
        // Arrange
        when(reservationRepository.findByDateReservation(anyString()))
            .thenReturn(Arrays.asList(reservation1, reservation2));
        
        // Act
        List<ServerReservationDTO> result = serverReservationService.getTodayReservations();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Vérifier les détails du premier DTO
        ServerReservationDTO dto1 = result.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("midi", dto1.getCreneauHoraire());
        assertEquals(3, dto1.getNbPersonne());
        assertEquals("Jean Dupont", dto1.getClientName());
        assertNotNull(dto1.getTable());
        assertEquals(5, dto1.getTable().getNumero());
        
        // Vérifier les détails du second DTO
        ServerReservationDTO dto2 = result.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("soir", dto2.getCreneauHoraire());
        assertEquals(5, dto2.getNbPersonne());
    }
    
    @Test
    void testGetTodayReservations_NoData() {
        // Arrange
        when(reservationRepository.findByDateReservation(anyString()))
            .thenReturn(Collections.emptyList());
        
        // Act
        List<ServerReservationDTO> result = serverReservationService.getTodayReservations();
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}