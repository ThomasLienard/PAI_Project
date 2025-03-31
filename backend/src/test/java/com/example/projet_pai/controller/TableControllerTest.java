package com.example.projet_pai.controller;

import com.example.projet_pai.dto.TableDisponibiliteDTO;
import com.example.projet_pai.service.TableServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableControllerTest {

    @Mock
    private TableServiceItf tableService;

    @InjectMocks
    private TableController tableController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTablesDisponibles_Success() {
        // Préparer les données
        String date = "2025-04-01";
        String creneau = "midi";
        int nbPersonnes = 4;

        TableDisponibiliteDTO table1 = new TableDisponibiliteDTO(1L, 1, 4);
        TableDisponibiliteDTO table2 = new TableDisponibiliteDTO(2L, 2, 6);

        // Configurer le mock
        when(tableService.getTablesDisponibles(date, creneau, nbPersonnes))
                .thenReturn(Arrays.asList(table1, table2));

        // Exécuter le test
        ResponseEntity<List<TableDisponibiliteDTO>> response = 
                tableController.getTablesDisponibles(date, creneau, nbPersonnes);

        // Vérifier les résultats
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getNumero());
        assertEquals(4, response.getBody().get(0).getCapacite());

        // Vérifier que le service a été appelé
        verify(tableService, times(1)).getTablesDisponibles(date, creneau, nbPersonnes);
    }

    @Test
    void testGetTablesDisponibles_NoTablesAvailable() {
        // Préparer les données
        String date = "2025-04-01";
        String creneau = "midi";
        int nbPersonnes = 10;

        // Configurer le mock pour retourner une liste vide
        when(tableService.getTablesDisponibles(date, creneau, nbPersonnes))
                .thenReturn(Collections.emptyList());

        // Exécuter le test
        ResponseEntity<List<TableDisponibiliteDTO>> response = 
                tableController.getTablesDisponibles(date, creneau, nbPersonnes);

        // Vérifier les résultats
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Vérifier que le service a été appelé
        verify(tableService, times(1)).getTablesDisponibles(date, creneau, nbPersonnes);
    }
}