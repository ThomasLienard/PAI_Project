package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Dish;

public class DishDTOMapper {
    public static DishDTO toDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setImageUrl(dish.getImageUrl());
        dto.setCategoryName(dish.getCategory() != null ? dish.getCategory().getName() : null);
        dto.setTags(dish.getTags() != null ? dish.getTags().stream().map(TagDTO::fromEntity).collect(java.util.stream.Collectors.toSet()) : null);
        return dto;
    }
}