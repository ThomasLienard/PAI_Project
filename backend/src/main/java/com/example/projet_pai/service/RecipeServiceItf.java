package com.example.projet_pai.service;

import java.util.List;

import com.example.projet_pai.dto.RecipeDTO;

public interface RecipeServiceItf {
    RecipeDTO createRecipe(RecipeDTO recipeDTO);
    List<RecipeDTO> getAllRecipes();
    RecipeDTO getRecipeById(Long id);
    RecipeDTO updateRecipe(Long id, RecipeDTO recipeDTO);
    boolean deleteRecipe(Long id);
    public void updateRecipeAvailability();
    public List<RecipeDTO> getAvailableRecipes();
}
