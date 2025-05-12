package com.example.projet_pai.service;

import com.example.projet_pai.dto.IngredientCategoryDTO;
import com.example.projet_pai.entite.IngredientCategory;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface IngredientCategoryServiceItf {
    List<IngredientCategoryDTO> getAllCategories();
    IngredientCategoryDTO createCategory(String name, MultipartFile iconFile) throws Exception;
    IngredientCategoryDTO updateCategory(Long id, String name, MultipartFile iconFile) throws Exception;
    void deleteCategory(Long id);
}