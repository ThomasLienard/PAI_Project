package com.example.projet_pai.controller;

import com.example.projet_pai.dto.DishDTO;
import com.example.projet_pai.service.Impl.MenuServiceImpl;
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
    private MenuServiceImpl menuService;

    @GetMapping("/grouped")
    public Map<String, List<DishDTO>> getMenuGroupedByCategory() {
        return menuService.getMenuGroupedByCategory();
    }
}
