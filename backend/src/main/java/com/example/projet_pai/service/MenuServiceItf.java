package com.example.projet_pai.service;

import java.util.List;
import java.util.Map;

import com.example.projet_pai.dto.DishDTO;

public interface MenuServiceItf {

    Map<String, List<DishDTO>> getMenuGroupedByCategory();
    

}
