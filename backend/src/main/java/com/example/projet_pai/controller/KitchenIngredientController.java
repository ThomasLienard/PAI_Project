package com.example.projet_pai.controller;

import com.example.projet_pai.dto.IngredientDTO;
import com.example.projet_pai.service.IngredientServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuisinier/ingredients")
@PreAuthorize("hasRole('CUISINIER')")
public class KitchenIngredientController {

    @Autowired
    private IngredientServiceItf ingredientService;

    @GetMapping
    public List<IngredientDTO> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }
}
