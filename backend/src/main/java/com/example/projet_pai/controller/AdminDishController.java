package com.example.projet_pai.controller;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.entite.Category;
import com.example.projet_pai.dto.DishCreateUpdateDTO;
import com.example.projet_pai.service.DishServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dishes")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminDishController {

    @Autowired
    private DishServiceItf dishService;

    @GetMapping
    public List<DishDTO> getAllDishes() {
        return dishService.getAllDishes();
    }

    @PostMapping
    public ResponseEntity<?> addDish(
            @RequestPart("dish") @Validated DishCreateUpdateDTO dishDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        dishService.addDish(dishDTO, photo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDish(
            @PathVariable Long id,
            @RequestPart("dish") @Validated DishCreateUpdateDTO dishDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        dishService.updateDish(id, dishDTO, photo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories")
    public List<Category> getDishCategories() {
        return dishService.getAllDishCategories();
    }
}