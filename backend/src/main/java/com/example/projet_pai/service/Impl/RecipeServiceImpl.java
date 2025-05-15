package com.example.projet_pai.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.projet_pai.dto.RecipeDTO;
import com.example.projet_pai.dto.RecipeIngredientDTO;
import com.example.projet_pai.entite.Recipe;
import com.example.projet_pai.entite.RecipeIngredient;
import com.example.projet_pai.entite.Ingredient;
import com.example.projet_pai.repository.RecipeRepository;
import com.example.projet_pai.repository.IngredientRepository;
import com.example.projet_pai.repository.RecipeIngredientRepository;
import com.example.projet_pai.service.RecipeServiceItf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeServiceItf {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setQuantite(recipeDTO.getQuantite());
        recipe.setUnite(recipeDTO.getUnite());

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (RecipeIngredientDTO ridto : recipeDTO.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ridto.getIngredientId()).orElseThrow();
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredient);
            ri.setQuantite(ridto.getQuantite());
            ri.setUnite(ridto.getUnite());
            recipeIngredients.add(ri);
        }
        recipe.setRecipeIngredients(recipeIngredients);

        Recipe savedRecipe = recipeRepository.save(recipe);

        // Mapping pour le retour
        List<RecipeIngredientDTO> ingredientDTOs = savedRecipe.getRecipeIngredients().stream()
            .map(ri -> new RecipeIngredientDTO(
                ri.getIngredient().getId(),
                ri.getIngredient().getName(),
                ri.getQuantite(),
                ri.getUnite()
            )).collect(Collectors.toList());

        return new RecipeDTO(
            savedRecipe.getId(),
            savedRecipe.getName(),
            savedRecipe.getQuantite(),
            savedRecipe.getUnite(),
            ingredientDTOs,
            recipe.isDisponible()
        );
    }

    @Override
    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream().map(recipe -> {
            List<RecipeIngredientDTO> ingredientDTOs = recipe.getRecipeIngredients().stream()
                .map(ri -> new RecipeIngredientDTO(
                    ri.getIngredient().getId(),
                    ri.getIngredient().getName(),
                    ri.getQuantite(),
                    ri.getUnite()
                )).collect(Collectors.toList());
            return new RecipeDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getQuantite(),
                recipe.getUnite(),
                ingredientDTOs,
                recipe.isDisponible()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public RecipeDTO getRecipeById(Long id) {
        return recipeRepository.findById(id).map(recipe -> {
            List<RecipeIngredientDTO> ingredientDTOs = recipe.getRecipeIngredients().stream()
                .map(ri -> new RecipeIngredientDTO(
                    ri.getIngredient().getId(),
                    ri.getIngredient().getName(),
                    ri.getQuantite(),
                    ri.getUnite()
                )).collect(Collectors.toList());
            return new RecipeDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getQuantite(),
                recipe.getUnite(),
                ingredientDTOs,
                recipe.isDisponible()
            );
        }).orElse(null);
    }

    @Override
    public RecipeDTO updateRecipe(Long id, RecipeDTO recipeDTO) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.setName(recipeDTO.getName());
            recipe.setQuantite(recipeDTO.getQuantite());
            recipe.setUnite(recipeDTO.getUnite());

            // Supprime les anciens RecipeIngredient (pour l'orphanRemoval)
            recipe.getRecipeIngredients().clear();

            List<RecipeIngredient> recipeIngredients = new ArrayList<>();
            for (RecipeIngredientDTO ridto : recipeDTO.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ridto.getIngredientId()).orElseThrow();
                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipe(recipe);
                ri.setIngredient(ingredient);
                ri.setQuantite(ridto.getQuantite());
                ri.setUnite(ridto.getUnite());
                recipeIngredients.add(ri);
            }
            recipe.getRecipeIngredients().addAll(recipeIngredients);

            Recipe updated = recipeRepository.save(recipe);

            List<RecipeIngredientDTO> ingredientDTOs = updated.getRecipeIngredients().stream()
                .map(ri -> new RecipeIngredientDTO(
                    ri.getIngredient().getId(),
                    ri.getIngredient().getName(),
                    ri.getQuantite(),
                    ri.getUnite()
                )).collect(Collectors.toList());

            this.updateRecipeAvailability();

            return new RecipeDTO(
                updated.getId(),
                updated.getName(),
                updated.getQuantite(),
                updated.getUnite(),
                ingredientDTOs,
                recipe.isDisponible()
            );
        }).orElse(null);
    }

    @Override
    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void updateRecipeAvailability() {
        List<Recipe> recipes = recipeRepository.findAll();

        for (Recipe recipe : recipes) {
            boolean isAvailable = true;

            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                Ingredient ingredient = recipeIngredient.getIngredient();
                double requiredQuantity = recipeIngredient.getQuantite();

                if (ingredient.getStock() <= requiredQuantity) {
                    isAvailable = false;
                    break;
                }
            }
            recipe.setDisponible(isAvailable);
            recipeRepository.save(recipe);
        }
    }

    @Override
    public List<RecipeDTO> getAvailableRecipes() {
        List<Recipe> availableRecipes = recipeRepository.findByDisponibleTrue();
        return availableRecipes.stream().map(recipe -> {
            List<RecipeIngredientDTO> ingredientDTOs = recipe.getRecipeIngredients().stream()
                .map(ri -> new RecipeIngredientDTO(
                    ri.getIngredient().getId(),
                    ri.getIngredient().getName(),
                    ri.getQuantite(),
                    ri.getUnite()
                )).collect(Collectors.toList());
            return new RecipeDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getQuantite(),
                recipe.getUnite(),
                ingredientDTOs,
                recipe.isDisponible()
            );
        }).collect(Collectors.toList());

    }
}
