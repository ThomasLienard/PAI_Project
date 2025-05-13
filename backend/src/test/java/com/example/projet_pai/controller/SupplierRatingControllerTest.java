package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierRatingDTO;
import com.example.projet_pai.service.SupplierRatingServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierRatingControllerTest {

    @InjectMocks
    private SupplierRatingController controller;

    @Mock
    private SupplierRatingServiceItf ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRateSupplier() {
        SupplierRatingDTO dto = new SupplierRatingDTO();
        dto.id = 1L;
        dto.score = 5;
        when(ratingService.rateSupplier(any())).thenReturn(dto);

        SupplierRatingDTO result = controller.rateSupplier(dto);

        assertNotNull(result);
        assertEquals(5, result.score);
        verify(ratingService).rateSupplier(dto);
    }

    @Test
    void testGetRatingsBySupplier() {
        when(ratingService.getRatingsBySupplier(1L)).thenReturn(Collections.emptyList());

        List<SupplierRatingDTO> result = controller.getRatingsBySupplier(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ratingService).getRatingsBySupplier(1L);
    }
}