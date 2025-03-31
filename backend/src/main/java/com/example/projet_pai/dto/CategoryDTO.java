// filepath: c:\Users\Evann\Desktop\Nouveau dossier (2)\PAI_Project\backend\src\main\java\com\example\projet_pai\dto\CategoryDTO.java
package com.example.projet_pai.dto;

import java.util.List;

public class CategoryDTO {
    private String name; // Nom de la catégorie
    private List<DishDTO> dishes; // Liste des plats associés

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }
}

