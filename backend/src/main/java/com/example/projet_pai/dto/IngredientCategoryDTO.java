package com.example.projet_pai.dto;

import com.example.projet_pai.entite.IngredientCategory; 
public class IngredientCategoryDTO {
    private Long id;
    private String name;
    private String icon;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
