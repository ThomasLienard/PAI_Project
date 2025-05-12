package com.example.projet_pai.service.Impl;

import com.example.projet_pai.entite.Ingredient;
import com.example.projet_pai.repository.IngredientRepository;
import com.example.projet_pai.service.StockAlertServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAlertServiceImpl implements StockAlertServiceItf {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public void checkStockLevels() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getInitialStock() <= ingredient.getAlertThreshold() && !ingredient.isAlertSent()) {
                sendAlert(ingredient);
                ingredient.setAlertSent(true);
                ingredientRepository.save(ingredient);
            }
        }
    }

    private void sendAlert(Ingredient ingredient) {
        // Logique pour envoyer une alerte (email, notification, etc.)
        System.out.println("Alerte : Le stock de " + ingredient.getName() + " est critique !");
    }
}