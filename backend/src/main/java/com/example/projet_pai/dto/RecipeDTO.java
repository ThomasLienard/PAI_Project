package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Ingredient;

public class RecipeDTO {

    private Long id;
    private String unite;
    private int quantite;
    private Ingredient ingredient;

    // Constructeurs
    public RecipeDTO() {
    }

    public RecipeDTO(Long id, String unite, int quantite, Ingredient ingredient) {
        this.id = id;
        this.unite = unite;
        this.quantite = quantite;
        this.ingredient = ingredient;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}