package com.example.projet_pai.dto;

import java.util.List;

public class RecipeDTO {

    private Long id;
    private String name; // Nom de la recette
    private int quantite; // Quantité totale (ex: 200)
    private String unite; // Unité totale (ex: gramme)
    private List<RecipeIngredientDTO> ingredients; // Liste des ingrédients avec quantité/unité
    private boolean disponible; // Indique si la recette est disponible

    public RecipeDTO() {}

    public RecipeDTO(Long id, String name, int quantite, String unite, List<RecipeIngredientDTO> ingredients, boolean disponible) {
        this.id = id;
        this.name = name;
        this.quantite = quantite;
        this.unite = unite;
        this.ingredients = ingredients;
        this.disponible = disponible;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public List<RecipeIngredientDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredientDTO> ingredients) { this.ingredients = ingredients; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}