package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierOrderDTO;
import com.example.projet_pai.service.SupplierOrderServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierOrderControllerTest {

    @InjectMocks
    private SupplierOrderController controller;

    @Mock
    private SupplierOrderServiceItf orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        SupplierOrderDTO dto = new SupplierOrderDTO();
        dto.id = 1L;
        when(orderService.createOrder(any())).thenReturn(dto);

        SupplierOrderDTO result = controller.createOrder(dto);

        assertNotNull(result);
        assertEquals(1L, result.id);
        verify(orderService).createOrder(dto);
    }

    @Test
    void testGetOrder() {
        SupplierOrderDTO dto = new SupplierOrderDTO();
        dto.id = 1L;
        when(orderService.getOrder(1L)).thenReturn(dto);

        SupplierOrderDTO result = controller.getOrder(1L);

        assertNotNull(result);
        assertEquals(1L, result.id);
        verify(orderService).getOrder(1L);
    }

    @Test
    void testGetOrdersBySupplier() {
        when(orderService.getOrdersBySupplier(1L)).thenReturn(Collections.emptyList());

        List<SupplierOrderDTO> result = controller.getOrdersBySupplier(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderService).getOrdersBySupplier(1L);
    }

    @Test
    void testRenewOrder() {
        SupplierOrderDTO dto = new SupplierOrderDTO();
        dto.id = 2L;
        when(orderService.renewOrder(1L)).thenReturn(dto);

        SupplierOrderDTO result = controller.renewOrder(1L);

        assertNotNull(result);
        assertEquals(2L, result.id);
        verify(orderService).renewOrder(1L);
    }
}