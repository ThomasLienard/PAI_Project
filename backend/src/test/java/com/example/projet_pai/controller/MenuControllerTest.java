package com.example.projet_pai.controller;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.service.Impl.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MenuControllerTest {

    @Mock
    private MenuServiceImpl menuService;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMenuGroupedByCategory_Success() {
        // Préparer les données simulées
        DishDTO dish1 = new DishDTO();
        dish1.setId(1L);
        dish1.setName("Salade César");
        dish1.setDescription("Une salade classique avec du poulet.");
        dish1.setPrice(12.5);
        dish1.setImageUrl("https://example.com/images/salade-cesar.png");
        dish1.setCategoryName("Entrées");

        DishDTO dish2 = new DishDTO();
        dish2.setId(2L);
        dish2.setName("Steak Frites");
        dish2.setDescription("Un steak juteux avec des frites.");
        dish2.setPrice(18.0);
        dish2.setImageUrl("https://example.com/images/steak-frites.jpg");
        dish2.setCategoryName("Plats");

        Map<String, List<DishDTO>> groupedMenu = new HashMap<>();
        groupedMenu.put("Entrées", Collections.singletonList(dish1));
        groupedMenu.put("Plats", Collections.singletonList(dish2));

        // Simuler le comportement du service
        when(menuService.getMenuGroupedByCategory()).thenReturn(groupedMenu);

        // Appeler le endpoint
        Map<String, List<DishDTO>> response = menuController.getMenuGroupedByCategory();

        // Vérifier la réponse
        assertEquals(2, response.size());
        assertEquals(1, response.get("Entrées").size());
        assertEquals(1, response.get("Plats").size());
        assertEquals("Salade César", response.get("Entrées").get(0).getName());
        assertEquals("Steak Frites", response.get("Plats").get(0).getName());
    }

    @Test
    void testGetMenuGroupedByCategory_EmptyMenu() {
        // Simuler un menu vide
        when(menuService.getMenuGroupedByCategory()).thenReturn(Collections.emptyMap());

        // Appeler le endpoint
        Map<String, List<DishDTO>> response = menuController.getMenuGroupedByCategory();

        // Vérifier la réponse
        assertEquals(0, response.size());
    }
}
