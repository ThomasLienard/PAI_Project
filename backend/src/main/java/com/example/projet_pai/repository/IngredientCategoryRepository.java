package com.example.projet_pai.repository;

import com.example.projet_pai.entite.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    Optional<IngredientCategory> findByName(String name);
}