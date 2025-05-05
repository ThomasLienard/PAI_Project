package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierDTO;
import com.example.projet_pai.service.SupplierServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierControllerTest {

    @InjectMocks
    private SupplierController controller;

    @Mock
    private SupplierServiceItf supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSupplier() {
        SupplierDTO dto = new SupplierDTO();
        dto.id = 1L;
        dto.name = "Test Supplier";
        when(supplierService.createSupplier(any())).thenReturn(dto);

        SupplierDTO result = controller.createSupplier(dto);

        assertNotNull(result);
        assertEquals("Test Supplier", result.name);
        verify(supplierService).createSupplier(dto);
    }

    @Test
    void testUpdateSupplier() {
        SupplierDTO dto = new SupplierDTO();
        dto.id = 1L;
        dto.name = "Updated Supplier";
        when(supplierService.updateSupplier(eq(1L), any())).thenReturn(dto);

        SupplierDTO result = controller.updateSupplier(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Supplier", result.name);
        verify(supplierService).updateSupplier(1L, dto);
    }

    @Test
    void testDeactivateSupplier() {
        doNothing().when(supplierService).deactivateSupplier(1L);

        controller.deactivateSupplier(1L);

        verify(supplierService).deactivateSupplier(1L);
    }

    @Test
    void testGetSupplier() {
        SupplierDTO dto = new SupplierDTO();
        dto.id = 1L;
        dto.name = "Test Supplier";
        when(supplierService.getSupplier(1L)).thenReturn(dto);

        SupplierDTO result = controller.getSupplier(1L);

        assertNotNull(result);
        assertEquals("Test Supplier", result.name);
        verify(supplierService).getSupplier(1L);
    }

    @Test
    void testSearchSuppliers() {
        when(supplierService.searchSuppliers(any(), any(), any())).thenReturn(Collections.emptyList());

        List<SupplierDTO> result = controller.searchSuppliers("name", "loc", "cat");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierService).searchSuppliers("name", "loc", "cat");
    }

    @Test
    void testGetAllSuppliers() {
        when(supplierService.getAllSuppliers()).thenReturn(Collections.emptyList());

        List<SupplierDTO> result = controller.getAllSuppliers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierService).getAllSuppliers();
    }
}