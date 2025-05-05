package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.dto.OrderItemRequest;
import com.example.projet_pai.dto.OrderRequest;
import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.OrderItem;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.repository.DishRepository;
import com.example.projet_pai.repository.OrderItemRepository;
import com.example.projet_pai.repository.OrderRepository;
import com.example.projet_pai.repository.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ServerOrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private ServerOrderServiceImpl serverOrderService;

    private Table mockTable;
    private Dish mockDish;
    private Order mockOrder;
    private OrderRequest mockOrderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'une table de test
        mockTable = new Table();
        mockTable.setId(1L);
        mockTable.setNumero(5);
        mockTable.setCapacite(4);

        // Création d'un plat de test
        mockDish = new Dish();
        mockDish.setId(1L);
        mockDish.setName("Test Dish");
        mockDish.setPrice(15.0);

        // Création d'une commande de test
        mockOrder = new Order(mockTable, "Client Test");
        mockOrder.setId(1L);
        mockOrder.setOrderTime(LocalDateTime.now());
        mockOrder.setStatus("en_preparation");
        mockOrder.setAdditional(false);

        OrderItem orderItem = new OrderItem(mockDish, 2, "Sans oignons");
        mockOrder.addItem(orderItem);

        // Création d'une requête de commande de test
        mockOrderRequest = new OrderRequest();
        mockOrderRequest.setTableId(1L);
        mockOrderRequest.setClientName("Client Test");
        mockOrderRequest.setAdditional(false);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setItemId(1L);
        itemRequest.setQuantity(2);
        itemRequest.setSpecialInstructions("Sans oignons");

        mockOrderRequest.setItems(Collections.singletonList(itemRequest));
    }

    @Test
    void testCreateOrder_Success() {
        // Configuration des mocks
        when(tableRepository.findById(mockOrderRequest.getTableId())).thenReturn(Optional.of(mockTable));
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(mockDish));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Exécution du test
        OrderDTO result = serverOrderService.createOrder(mockOrderRequest);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getTableNumber());
        assertEquals("Client Test", result.getClientName());

        // Vérification que les repositories ont été appelés
        verify(tableRepository, times(1)).findById(mockOrderRequest.getTableId());
        verify(dishRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_TableNotFound() {
        // Configuration des mocks
        when(tableRepository.findById(mockOrderRequest.getTableId())).thenReturn(Optional.empty());

        // Exécution du test et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverOrderService.createOrder(mockOrderRequest);
        });

        assertEquals("Table non trouvée: 1", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(tableRepository, times(1)).findById(mockOrderRequest.getTableId());
        verify(dishRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCreateOrder_DishNotFound() {
        // Configuration des mocks
        when(tableRepository.findById(mockOrderRequest.getTableId())).thenReturn(Optional.of(mockTable));
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Exécution du test et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverOrderService.createOrder(mockOrderRequest);
        });

        assertEquals("Plat non trouvé: 1", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(tableRepository, times(1)).findById(mockOrderRequest.getTableId());
        verify(dishRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCreateOrder_InvalidData() {
        // Préparer les données avec des valeurs invalides
        OrderRequest invalidRequest = new OrderRequest();
        // TableId est null

        // Exécution du test et vérification de l'exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serverOrderService.createOrder(invalidRequest);
        });

        assertEquals("La commande doit avoir une table et au moins un article", exception.getMessage());

        // Vérification que les repositories n'ont pas été appelés
        verify(tableRepository, never()).findById(anyLong());
        verify(dishRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetCurrentOrders() {
        // Configuration des mocks
        when(orderRepository.findCurrentOrders()).thenReturn(Collections.singletonList(mockOrder));

        // Exécution du test
        List<OrderDTO> result = serverOrderService.getCurrentOrders();

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(5, result.get(0).getTableNumber());
        assertEquals("Client Test", result.get(0).getClientName());

        // Vérification que le repository a été appelé
        verify(orderRepository, times(1)).findCurrentOrders();
    }

    @Test
    void testGetCurrentOrdersByTable() {
        // Configuration des mocks
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(mockTable));
        when(orderRepository.findCurrentOrdersByTable(mockTable)).thenReturn(Collections.singletonList(mockOrder));

        // Exécution du test
        List<OrderDTO> result = serverOrderService.getCurrentOrdersByTable(1L);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(5, result.get(0).getTableNumber());

        // Vérification que les repositories ont été appelés
        verify(tableRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).findCurrentOrdersByTable(mockTable);
    }

    @Test
    void testGetCurrentOrdersByTable_TableNotFound() {
        // Configuration des mocks
        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Exécution du test et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverOrderService.getCurrentOrdersByTable(1L);
        });

        assertEquals("Table non trouvée: 1", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(tableRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).findCurrentOrdersByTable(any(Table.class));
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Configuration des mocks
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Exécution du test
        OrderDTO result = serverOrderService.updateOrderStatus(1L, "pret");

        // Vérification des résultats
        assertNotNull(result);
        assertEquals("pret", mockOrder.getStatus()); // Vérifier que le statut a été mis à jour

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_InvalidStatus() {
        // Configuration des mocks
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        // Exécution du test et vérification de l'exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serverOrderService.updateOrderStatus(1L, "statut_invalide");
        });

        assertEquals("Statut invalide: statut_invalide", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus_OrderNotFound() {
        // Configuration des mocks
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Exécution du test et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverOrderService.updateOrderStatus(1L, "pret");
        });

        assertEquals("Commande non trouvée: 1", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCancelOrder_Success() {
        // Configuration des mocks
        mockOrder.setStatus("en_preparation");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        doNothing().when(orderRepository).delete(any(Order.class));

        // Exécution du test
        assertDoesNotThrow(() -> serverOrderService.cancelOrder(1L));

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).delete(mockOrder);
    }

    @Test
    void testCancelOrder_OrderNotFound() {
        // Configuration des mocks
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Exécution du test et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverOrderService.cancelOrder(1L);
        });

        assertEquals("Commande non trouvée: 1", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).delete(any(Order.class));
    }

    @Test
    void testCancelOrder_InvalidStatus() {
        // Configuration des mocks
        mockOrder.setStatus("servi"); // Une commande déjà servie
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        // Exécution du test et vérification de l'exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            serverOrderService.cancelOrder(1L);
        });

        assertEquals("Impossible d'annuler une commande qui est déjà servi", exception.getMessage());

        // Vérification que les repositories ont été appelés
        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).delete(any(Order.class));
    }
}