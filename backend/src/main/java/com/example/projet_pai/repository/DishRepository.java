package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByCategory_Name(String categoryName);
    List<Dish> findByTagsIn(List<String> tags);
    List<Dish> findByCategory_NameAndTagsIn(String categoryName, List<String> tags);
    Optional<Dish> findByName(String name);
}
