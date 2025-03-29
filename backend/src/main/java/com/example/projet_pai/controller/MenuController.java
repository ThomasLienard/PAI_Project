package com.example.projet_pai.controller;

import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.service.Impl.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/grouped")
    public Map<String, List<Dish>> getMenuGroupedByCategory() {
        return menuService.getMenuGroupedByCategory();
    }
}
