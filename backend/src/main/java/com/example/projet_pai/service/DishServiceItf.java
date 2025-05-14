package com.example.projet_pai.service;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.entite.Category;
import com.example.projet_pai.dto.DishCreateUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DishServiceItf {
    List<DishDTO> getAllDishes();
    void addDish(DishCreateUpdateDTO dishDTO, MultipartFile photo);
    void updateDish(Long id, DishCreateUpdateDTO dishDTO, MultipartFile photo);
    void deleteDish(Long id);
    List<Category> getAllDishCategories();
}