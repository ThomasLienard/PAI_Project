package com.example.projet_pai.controller;

import com.example.projet_pai.dto.IngredientCategoryDTO;
import com.example.projet_pai.dto.IngredientDTO;
import com.example.projet_pai.service.IngredientCategoryServiceItf;
import com.example.projet_pai.service.IngredientServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stocks")
public class StockController {

    @Autowired
    private IngredientCategoryServiceItf categoryService;
    @Autowired
    private IngredientServiceItf ingredientService;

    // --- Catégories ---
    @GetMapping("/categories")
    public List<IngredientCategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping(value = "/categories", consumes = {"multipart/form-data"})
    public IngredientCategoryDTO createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "icon", required = false) MultipartFile iconFile
    ) throws Exception {
        return categoryService.createCategory(name, iconFile);
    }

    @PutMapping(value = "/categories/{id}", consumes = {"multipart/form-data"})
    public IngredientCategoryDTO updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "icon", required = false) MultipartFile iconFile
    ) throws Exception {
        return categoryService.updateCategory(id, name, iconFile);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // --- Ingrédients ---
    @GetMapping("/ingredients")
    public List<IngredientDTO> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping(value = "/ingredients", consumes = {"multipart/form-data"})
    public IngredientDTO createIngredient(
            @RequestParam("name") String name,
            @RequestParam("unit") String unit,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "initialStock", required = false) Double initialStock,
            @RequestParam(value = "alertThreshold", required = false) Double alertThreshold,
            @RequestParam(value = "recommendedOrderQty", required = false) Double recommendedOrderQty,
            @RequestParam(value = "shelfLifeDays", required = false) Integer shelfLifeDays,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "photo", required = false) MultipartFile photoFile
    ) throws Exception {
        return ingredientService.createIngredient(name, unit, description, initialStock, alertThreshold, recommendedOrderQty, shelfLifeDays, categoryId, photoFile);
    }

    @PutMapping(value = "/ingredients/{id}", consumes = {"multipart/form-data"})
    public IngredientDTO updateIngredient(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("unit") String unit,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "initialStock", required = false) Double initialStock,
            @RequestParam(value = "alertThreshold", required = false) Double alertThreshold,
            @RequestParam(value = "recommendedOrderQty", required = false) Double recommendedOrderQty,
            @RequestParam(value = "shelfLifeDays", required = false) Integer shelfLifeDays,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "photo", required = false) MultipartFile photoFile
    ) throws Exception {
        return ingredientService.updateIngredient(id, name, unit, description, initialStock, alertThreshold, recommendedOrderQty, shelfLifeDays, categoryId, photoFile);
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok().build();
    }
}