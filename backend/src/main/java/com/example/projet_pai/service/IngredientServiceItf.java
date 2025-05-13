package com.example.projet_pai.service;

import com.example.projet_pai.dto.IngredientDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IngredientServiceItf {
    List<IngredientDTO> getAllIngredients();
    IngredientDTO createIngredient(String name, String unit, String description, Double initialStock,
                                   Double alertThreshold, Double recommendedOrderQty, Integer shelfLifeDays,
                                   Long categoryId, MultipartFile photoFile) throws Exception;
    IngredientDTO updateIngredient(Long id, String name, String unit, String description, Double initialStock,
                                   Double alertThreshold, Double recommendedOrderQty, Integer shelfLifeDays,
                                   Long categoryId, MultipartFile photoFile) throws Exception;
    void deleteIngredient(Long id);
}
