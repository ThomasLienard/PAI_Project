package com.example.projet_pai.dto;

public class RecipeDTO {

    private Long id;
    private String unite;
    private int quantite;
    private String ingredient;

    // Constructeurs
    public RecipeDTO() {
    }

    public RecipeDTO(Long id, String unite, int quantite, String ingredient) {
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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}