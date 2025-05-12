package com.example.projet_pai.controller;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.dto.OrderRequest;
import com.example.projet_pai.service.ServerOrderServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ServerOrderControllerTest {

    @Mock
    private ServerOrderServiceItf serverOrderService;

    @InjectMocks
    private ServerOrderController serverOrderController;

    private OrderDTO mockOrderDTO;
    private OrderRequest mockOrderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'un OrderDTO de test
        mockOrderDTO = new OrderDTO();
        mockOrderDTO.setId(1L);
        mockOrderDTO.setTableNumber(5);
        mockOrderDTO.setClientName("Client Test");
        mockOrderDTO.setOrderTime(LocalDateTime.now());
        mockOrderDTO.setStatus("en_preparation");
        mockOrderDTO.setAdditional(false);
        mockOrderDTO.setItems(Collections.emptyList());

        // Création d'une OrderRequest de test
        mockOrderRequest = new OrderRequest();
        mockOrderRequest.setTableId(5L);
        mockOrderRequest.setClientName("Client Test");
        mockOrderRequest.setAdditional(false);
        mockOrderRequest.setItems(Collections.emptyList());
    }

    @Test
    void testCreateOrder_Success() {
        // Configuration des mocks
        when(serverOrderService.createOrder(any(OrderRequest.class))).thenReturn(mockOrderDTO);

        // Exécution du test
        ResponseEntity<OrderDTO> response = serverOrderController.createOrder(mockOrderRequest);

        // Vérification des résultats
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(5, response.getBody().getTableNumber());
        assertEquals("Client Test", response.getBody().getClientName());

        // Vérification que le service a été appelé avec les bons paramètres
        verify(serverOrderService, times(1)).createOrder(any(OrderRequest.class));
    }

    @Test
    void testCreateOrder_Error() {
        // Configuration des mocks pour simuler une erreur
        when(serverOrderService.createOrder(any(OrderRequest.class)))
            .thenThrow(new RuntimeException("Erreur lors de la création de la commande"));

        // Exécution du test
        ResponseEntity<OrderDTO> response = serverOrderController.createOrder(mockOrderRequest);

        // Vérification des résultats
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).createOrder(any(OrderRequest.class));
    }

    @Test
    void testGetCurrentOrders_Success() {
        // Configuration des mocks
        List<OrderDTO> mockOrders = Arrays.asList(mockOrderDTO);
        when(serverOrderService.getCurrentOrders()).thenReturn(mockOrders);

        // Exécution du test
        ResponseEntity<List<OrderDTO>> response = serverOrderController.getCurrentOrders();

        // Vérification des résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).getCurrentOrders();
    }

    @Test
    void testGetCurrentOrders_Error() {
        // Configuration des mocks pour simuler une erreur
        when(serverOrderService.getCurrentOrders())
            .thenThrow(new RuntimeException("Erreur lors de la récupération des commandes"));

        // Exécution du test
        ResponseEntity<List<OrderDTO>> response = serverOrderController.getCurrentOrders();

        // Vérification des résultats
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).getCurrentOrders();
    }

    @Test
    void testGetCurrentOrdersByTable_Success() {
        // Configuration des mocks
        Long tableId = 5L;
        List<OrderDTO> mockOrders = Arrays.asList(mockOrderDTO);
        when(serverOrderService.getCurrentOrdersByTable(tableId)).thenReturn(mockOrders);

        // Exécution du test
        ResponseEntity<List<OrderDTO>> response = serverOrderController.getCurrentOrdersByTable(tableId);

        // Vérification des résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).getCurrentOrdersByTable(tableId);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Configuration des mocks
        Long orderId = 1L;
        String newStatus = "pret";
        Map<String, String> statusUpdate = Map.of("status", newStatus);
        
        when(serverOrderService.updateOrderStatus(orderId, newStatus)).thenReturn(mockOrderDTO);

        // Exécution du test
        ResponseEntity<OrderDTO> response = serverOrderController.updateOrderStatus(orderId, statusUpdate);

        // Vérification des résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void testUpdateOrderStatus_InvalidStatus() {
        // Configuration des mocks
        Long orderId = 1L;
        Map<String, String> statusUpdate = Collections.emptyMap(); // Statut manquant
        
        // Exécution du test
        ResponseEntity<OrderDTO> response = serverOrderController.updateOrderStatus(orderId, statusUpdate);

        // Vérification des résultats
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        // Vérification que le service n'a pas été appelé
        verify(serverOrderService, never()).updateOrderStatus(anyLong(), anyString());
    }

    @Test
    void testCancelOrder_Success() {
        // Configuration des mocks
        Long orderId = 1L;
        doNothing().when(serverOrderService).cancelOrder(orderId);

        // Exécution du test
        ResponseEntity<Void> response = serverOrderController.cancelOrder(orderId);

        // Vérification des résultats
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).cancelOrder(orderId);
    }

    @Test
    void testCancelOrder_Error() {
        // Configuration des mocks pour simuler une erreur
        Long orderId = 1L;
        doThrow(new RuntimeException("Erreur lors de l'annulation de la commande"))
            .when(serverOrderService).cancelOrder(orderId);

        // Exécution du test
        ResponseEntity<Void> response = serverOrderController.cancelOrder(orderId);

        // Vérification des résultats
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Vérification que le service a été appelé
        verify(serverOrderService, times(1)).cancelOrder(orderId);
    }
}