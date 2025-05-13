package com.example.projet_pai.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.projet_pai.dto.RecipeDTO;
import com.example.projet_pai.entite.Recipe;
import org.springframework.stereotype.Service;
import com.example.projet_pai.repository.RecipeRepository;
import com.example.projet_pai.service.RecipeServiceItf;

@Service 
public class RecipeServiceImpl implements RecipeServiceItf {


    @Autowired
    private RecipeRepository recipeRepository;


    @Override
    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setUnite(recipeDTO.getUnite());
        recipe.setQuantite(recipeDTO.getQuantite());
        recipe.setIngredient(recipeDTO.getIngredient());

        Recipe savedRecipe = recipeRepository.save(recipe);

        return new RecipeDTO(
            savedRecipe.getId(),
            savedRecipe.getUnite(),
            savedRecipe.getQuantite(),
            savedRecipe.getIngredient()
        );

    }
    
}
