package com.example.projet_pai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projet_pai.entite.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
