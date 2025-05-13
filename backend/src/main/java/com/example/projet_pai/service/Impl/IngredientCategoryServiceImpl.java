package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.IngredientCategoryDTO;
import com.example.projet_pai.entite.IngredientCategory;
import com.example.projet_pai.repository.IngredientCategoryRepository;
import com.example.projet_pai.service.IngredientCategoryServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientCategoryServiceImpl implements IngredientCategoryServiceItf {

    @Autowired
    private IngredientCategoryRepository categoryRepository;

    private final String categoryUploadDir = "uploads/images/category-icons/";

    @Override
    public List<IngredientCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public IngredientCategoryDTO createCategory(String name, MultipartFile iconFile) throws Exception {
        IngredientCategory category = new IngredientCategory();
        category.setName(name);

        if (iconFile != null && !iconFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + iconFile.getOriginalFilename();
            Path path = Paths.get(categoryUploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(iconFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            category.setIcon("/images/category-icons/" + filename);
        }

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public IngredientCategoryDTO updateCategory(Long id, String name, MultipartFile iconFile) throws Exception {
        IngredientCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
        category.setName(name);
        if (iconFile != null && !iconFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + iconFile.getOriginalFilename();
            Path path = Paths.get(categoryUploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.copy(iconFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            category.setIcon("/images/category-icons/" + filename);
        }
        return toDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private IngredientCategoryDTO toDTO(IngredientCategory cat) {
        IngredientCategoryDTO dto = new IngredientCategoryDTO();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        String icon = cat.getIcon();
        if (icon != null && !icon.startsWith("http")) {
            icon = "http://localhost:8080" + icon;
        }
        dto.setIcon(icon);
        return dto;
    }
}