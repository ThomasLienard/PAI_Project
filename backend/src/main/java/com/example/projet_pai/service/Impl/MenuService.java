package com.example.projet_pai.service.Impl;

import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private DishRepository dishRepository;

    public Map<String, List<Dish>> getMenuGroupedByCategory() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .collect(Collectors.groupingBy(dish -> dish.getCategory().getName()));
    }
}