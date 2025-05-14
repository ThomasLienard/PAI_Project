package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.dto.DishCreateUpdateDTO;
import com.example.projet_pai.entite.*;
import com.example.projet_pai.repository.*;
import com.example.projet_pai.service.DishServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.projet_pai.dto.DishDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

@Service
public class DishServiceImpl implements DishServiceItf {

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    private final String dishUploadDir = "uploads/images/dish-photos/";

    @Override
    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream().map(DishDTOMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void addDish(DishCreateUpdateDTO dto, MultipartFile photo) {
        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());

        // Gestion obligatoire de la catégorie
        if (dto.getCategoryId() == null) {
            throw new RuntimeException("Une catégorie est obligatoire pour le plat.");
        }
        dish.setCategory(categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Catégorie introuvable")));

        // Les tags peuvent être vides
        if (dto.getTagIds() == null) {
            dish.setTags(new java.util.HashSet<>());
        } else {
            dish.setTags(tagRepository.findAllById(dto.getTagIds()).stream().collect(Collectors.toSet()));
        }

        if (photo != null && !photo.isEmpty()) {
            String url = savePhoto(photo);
            dish.setImageUrl(url);
        }

        dishRepository.save(dish);
    }

    @Override
    public void updateDish(Long id, DishCreateUpdateDTO dto, MultipartFile photo) {
        Dish dish = dishRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Plat non trouvé"));
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());

        if (photo != null && !photo.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            String url = savePhoto(photo, filename);
            dish.setImageUrl(url);
        }

        dish.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        dish.setTags(tagRepository.findAllById(dto.getTagIds()).stream().collect(Collectors.toSet()));
        dishRepository.save(dish);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    private String savePhoto(MultipartFile photo) {
        try {
            Files.createDirectories(Paths.get(dishUploadDir));
            String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(dishUploadDir, filename);
            Files.write(filePath, photo.getBytes());
            return "/images/dish-photos/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la photo", e);
        }
    }
    @Override
    public List<Category> getAllDishCategories() {
    return categoryRepository.findAll();
}
}