package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierProductDTO;
import com.example.projet_pai.service.SupplierProductServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierProductControllerTest {

    @InjectMocks
    private SupplierProductController controller;

    @Mock
    private SupplierProductServiceItf productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        SupplierProductDTO dto = new SupplierProductDTO();
        dto.id = 1L;
        dto.name = "Produit Test";
        when(productService.addProduct(any())).thenReturn(dto);

        SupplierProductDTO result = controller.addProduct(dto);

        assertNotNull(result);
        assertEquals("Produit Test", result.name);
        verify(productService).addProduct(dto);
    }

    @Test
    void testUpdateProduct() {
        SupplierProductDTO dto = new SupplierProductDTO();
        dto.id = 1L;
        dto.name = "Produit Modifié";
        when(productService.updateProduct(eq(1L), any())).thenReturn(dto);

        SupplierProductDTO result = controller.updateProduct(1L, dto);

        assertNotNull(result);
        assertEquals("Produit Modifié", result.name);
        verify(productService).updateProduct(1L, dto);
    }

    @Test
    void testGetProductsBySupplier() {
        when(productService.getProductsBySupplier(1L)).thenReturn(Collections.emptyList());

        List<SupplierProductDTO> result = controller.getProductsBySupplier(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productService).getProductsBySupplier(1L);
    }

    @Test
    void testSearchProducts() {
        when(productService.searchProducts(any(), any())).thenReturn(Collections.emptyList());

        List<SupplierProductDTO> result = controller.searchProducts("name", "cat");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productService).searchProducts("name", "cat");
    }

    @Test
    void testGetAlternatives() {
        when(productService.getAlternatives(1L)).thenReturn(Collections.emptyList());

        List<SupplierProductDTO> result = controller.getAlternatives(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productService).getAlternatives(1L);
    }
}