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
        System.out.println("=== [addDish] DTO reçu ===");
        System.out.println("Nom: " + dto.getName());
        System.out.println("Description: " + dto.getDescription());
        System.out.println("Prix: " + dto.getPrice());
        System.out.println("CategoryId: " + dto.getCategoryId());
        System.out.println("TagIds: " + dto.getTagIds());
        System.out.println("Photo: " + (photo != null ? photo.getOriginalFilename() : "Aucune"));

        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());

        if (dto.getCategoryId() == null) {
            System.out.println("!!! ERREUR: categoryId est null !!!");
        }
        dish.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> {
            System.out.println("!!! ERREUR: Catégorie introuvable pour id=" + dto.getCategoryId());
            return new RuntimeException("Catégorie introuvable");
        }));

        if (dto.getTagIds() == null) {
            System.out.println("!!! ERREUR: tagIds est null !!!");
        }
        dish.setTags(tagRepository.findAllById(dto.getTagIds()).stream().collect(Collectors.toSet()));

        if (photo != null && !photo.isEmpty()) {
            String url = savePhoto(photo);
            dish.setImageUrl(url);
            System.out.println("Photo enregistrée à: " + url);
        }

        dishRepository.save(dish);
        System.out.println("=== [addDish] Plat enregistré avec succès ===");
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
            Path path = Paths.get(dishUploadDir, filename);
            try {
                Files.createDirectories(path.getParent());
                Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la mise à jour de la photo", e);
            }
            dish.setImageUrl("/images/dish-photos/" + filename);
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
            return "/images/dishes/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la photo", e);
        }
    }
    @Override
    public List<Category> getAllDishCategories() {
    return categoryRepository.findAll();
}
}