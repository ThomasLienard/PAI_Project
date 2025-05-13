package com.example.projet_pai.dto;

public class RecipeIngredientDTO {
    private Long ingredientId;
    private String ingredientName; // Ajout du nom pour l'affichage
    private double quantite;
    private String unite;

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(Long ingredientId, String ingredientName, double quantite, String unite) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantite = quantite;
        this.unite = unite;
    }

    // Getters & setters
    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
}
