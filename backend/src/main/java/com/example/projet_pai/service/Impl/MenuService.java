package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.dto.TagDTO;
import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.entite.Tag;
import com.example.projet_pai.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private DishRepository dishRepository;

    public Map<String, List<DishDTO>> getMenuGroupedByCategory() {
        List<Dish> dishes = dishRepository.findAll();

        // Grouper les plats par catégorie et les convertir en DTOs
        return dishes.stream()
                .collect(Collectors.groupingBy(
                        dish -> dish.getCategory().getName(),
                        Collectors.mapping(this::convertToDishDTO, Collectors.toList())
                ));
    }

    private DishDTO convertToDishDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setImageUrl(dish.getImageUrl());
        dto.setCategoryName(dish.getCategory().getName());
        dto.setTags(convertToTagDTOs(dish.getTags())); // Convertir les tags en DTOs
        return dto;
    }

    private Set<TagDTO> convertToTagDTOs(Set<Tag> tags) {
        return tags.stream().map(tag -> {
            TagDTO dto = new TagDTO();
            dto.setId(tag.getId());
            dto.setName(tag.getName());
            dto.setIcon(tag.getIcon());
            return dto;
        }).collect(Collectors.toSet());
    }
}