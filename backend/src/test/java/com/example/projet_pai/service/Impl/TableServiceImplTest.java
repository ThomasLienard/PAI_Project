package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.TableDisponibiliteDTO;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.repository.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableServiceImplTest {

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TableServiceImpl tableService;

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

        Table table1 = new Table();
        table1.setId(1L);
        table1.setNumero(1);
        table1.setCapacite(4);

        Table table2 = new Table();
        table2.setId(2L);
        table2.setNumero(2);
        table2.setCapacite(6);

        // Configurer le mock
        when(tableRepository.findAvailableTables(date, creneau, nbPersonnes))
                .thenReturn(Arrays.asList(table1, table2));

        // Exécuter le test
        List<TableDisponibiliteDTO> result = tableService.getTablesDisponibles(date, creneau, nbPersonnes);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getNumero());
        assertEquals(4, result.get(0).getCapacite());
        assertEquals(2, result.get(1).getNumero());
        assertEquals(6, result.get(1).getCapacite());

        // Vérifier que le repository a été appelé
        verify(tableRepository, times(1)).findAvailableTables(date, creneau, nbPersonnes);
    }

    @Test
    void testGetTablesDisponibles_EmptyResult() {
        // Préparer les données
        String date = "2025-04-01";
        String creneau = "midi";
        int nbPersonnes = 10;

        // Configurer le mock pour retourner une liste vide
        when(tableRepository.findAvailableTables(date, creneau, nbPersonnes))
                .thenReturn(Collections.emptyList());

        // Exécuter le test
        List<TableDisponibiliteDTO> result = tableService.getTablesDisponibles(date, creneau, nbPersonnes);

        // Vérifier les résultats
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Vérifier que le repository a été appelé
        verify(tableRepository, times(1)).findAvailableTables(date, creneau, nbPersonnes);
    }
}
