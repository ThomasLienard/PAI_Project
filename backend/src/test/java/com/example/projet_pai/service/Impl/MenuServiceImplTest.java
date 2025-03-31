package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.entite.Category;
import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.entite.Tag;
import com.example.projet_pai.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MenuServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Préparer les données simulées
        Category category1 = new Category();
        category1.setName("Entrées");

        Category category2 = new Category();
        category2.setName("Plats");

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Végétarien");
        tag1.setIcon("vegetarian-icon.png");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Sans Gluten");
        tag2.setIcon("gluten-free-icon.png");

        dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Salade César");
        dish1.setDescription("Une salade classique avec du poulet.");
        dish1.setPrice(12.5);
        dish1.setImageUrl("https://example.com/images/salade-cesar.png");
        dish1.setCategory(category1);
        dish1.setTags(Set.of(tag1));

        dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Steak Frites");
        dish2.setDescription("Un steak juteux avec des frites.");
        dish2.setPrice(18.0);
        dish2.setImageUrl("https://example.com/images/steak-frites.jpg");
        dish2.setCategory(category2);
        dish2.setTags(Set.of(tag2));
    }

    @Test
    void testGroupByCategory() {
        // Simuler le comportement du repository
        when(dishRepository.findAll()).thenReturn(Arrays.asList(dish1, dish2));

        // Appeler la méthode à tester
        Map<String, List<DishDTO>> result = menuService.getMenuGroupedByCategory();

        // Vérifier que les catégories sont correctement regroupées
        assertEquals(2, result.size());
        assertEquals(1, result.get("Entrées").size());
        assertEquals(1, result.get("Plats").size());
    }

    @Test
    void testDishDTOConversionForEntrées() {
        // Simuler le comportement du repository
        when(dishRepository.findAll()).thenReturn(Arrays.asList(dish1));

        // Appeler la méthode à tester
        Map<String, List<DishDTO>> result = menuService.getMenuGroupedByCategory();

        // Vérifier les propriétés du DishDTO pour "Entrées"
        DishDTO dishDTO = result.get("Entrées").get(0);
        assertEquals("Salade César", dishDTO.getName());
        assertEquals("Une salade classique avec du poulet.", dishDTO.getDescription());
        assertEquals(12.5, dishDTO.getPrice());
        assertEquals("https://example.com/images/salade-cesar.png", dishDTO.getImageUrl());
        assertEquals("Entrées", dishDTO.getCategoryName());
        assertEquals(1, dishDTO.getTags().size());
    }

    @Test
    void testDishDTOConversionForPlats() {
        // Simuler le comportement du repository
        when(dishRepository.findAll()).thenReturn(Arrays.asList(dish2));

        // Appeler la méthode à tester
        Map<String, List<DishDTO>> result = menuService.getMenuGroupedByCategory();

        // Vérifier les propriétés du DishDTO pour "Plats"
        DishDTO dishDTO = result.get("Plats").get(0);
        assertEquals("Steak Frites", dishDTO.getName());
        assertEquals("Un steak juteux avec des frites.", dishDTO.getDescription());
        assertEquals(18.0, dishDTO.getPrice());
        assertEquals("https://example.com/images/steak-frites.jpg", dishDTO.getImageUrl());
        assertEquals("Plats", dishDTO.getCategoryName());
        assertEquals(1, dishDTO.getTags().size());
    }
}
