package com.example.projet_pai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.projet_pai.dto.RecipeDTO;
import com.example.projet_pai.service.RecipeServiceItf;
import com.example.projet_pai.service.ServerOrderServiceItf;

@RestController
@RequestMapping("/api/server/orders")
@PreAuthorize("hasRole('CUISINIER')")
public class RecipeController {
    
    @Autowired
    private RecipeServiceItf service;

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            RecipeDTO createdRecipe = service.createRecipe(recipeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    }
}
