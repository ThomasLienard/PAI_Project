package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.IngredientDTO;
import com.example.projet_pai.entite.Ingredient;
import com.example.projet_pai.entite.IngredientCategory;
import com.example.projet_pai.repository.IngredientCategoryRepository;
import com.example.projet_pai.repository.IngredientRepository;
import com.example.projet_pai.service.IngredientServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientServiceItf {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientCategoryRepository categoryRepository;

    private final String ingredientUploadDir = "uploads/images/ingredient-photos/";

    @Override
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public IngredientDTO createIngredient(String name, String unit, String description, Double initialStock,
                                          Double alertThreshold, Double recommendedOrderQty, Integer shelfLifeDays,
                                          Long categoryId, MultipartFile photoFile) throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setDescription(description);
        ingredient.setInitialStock(initialStock);
        ingredient.setAlertThreshold(alertThreshold);
        ingredient.setRecommendedOrderQty(recommendedOrderQty);
        ingredient.setShelfLifeDays(shelfLifeDays);

        Optional<IngredientCategory> catOpt = categoryRepository.findById(categoryId);
        catOpt.ifPresent(ingredient::setCategory);

        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path path = Paths.get(ingredientUploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            ingredient.setPhotoUrl("/images/ingredient-photos/" + filename);
        }

        return toDTO(ingredientRepository.save(ingredient));
    }

    @Override
    public IngredientDTO updateIngredient(Long id, String name, String unit, String description, Double initialStock,
                                          Double alertThreshold, Double recommendedOrderQty, Integer shelfLifeDays,
                                          Long categoryId, MultipartFile photoFile) throws Exception {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé"));
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setDescription(description);
        ingredient.setInitialStock(initialStock);
        ingredient.setAlertThreshold(alertThreshold);
        ingredient.setRecommendedOrderQty(recommendedOrderQty);
        ingredient.setShelfLifeDays(shelfLifeDays);

        Optional<IngredientCategory> catOpt = categoryRepository.findById(categoryId);
        catOpt.ifPresent(ingredient::setCategory);

        if (photoFile != null && !photoFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path path = Paths.get(ingredientUploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            ingredient.setPhotoUrl("/images/ingredient-photos/" + filename);
        }

        return toDTO(ingredientRepository.save(ingredient));
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    private String computeAlertLevel(Ingredient ing) {
        if (ing.getInitialStock() == null || ing.getAlertThreshold() == null) return "OK";
        double stock = ing.getInitialStock();
        double threshold = ing.getAlertThreshold();
        if (stock <= threshold * 0.5) {
            return "CRITIQUE";
        } else if (stock <= threshold) {
            return "BAS";
        } else {
            return "OK";
        }
    }

    private IngredientDTO toDTO(Ingredient ing) {
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ing.getId());
        dto.setName(ing.getName());
        dto.setUnit(ing.getUnit());
        dto.setDescription(ing.getDescription());
        String photoUrl = ing.getPhotoUrl();
        if (photoUrl != null && !photoUrl.startsWith("http")) {
            photoUrl = "http://localhost:8080" + photoUrl;
        }
        dto.setPhotoUrl(photoUrl);
        dto.setInitialStock(ing.getInitialStock());
        dto.setAlertThreshold(ing.getAlertThreshold());
        dto.setRecommendedOrderQty(ing.getRecommendedOrderQty());
        dto.setShelfLifeDays(ing.getShelfLifeDays());
        dto.setCategoryId(ing.getCategory() != null ? ing.getCategory().getId() : null);
        dto.setAlertLevel(computeAlertLevel(ing));
        return dto;
    }
}
