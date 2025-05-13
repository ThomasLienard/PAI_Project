package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.OrderItem;
import com.example.projet_pai.repository.OrderRepository;
import com.example.projet_pai.repository.IngredientRepository;
import com.example.projet_pai.repository.OrderItemRepository;
import com.example.projet_pai.service.KitchenOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KitchenOrderServiceImpl implements KitchenOrderServiceItf {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<OrderDTO> getOrdersToPrepare() {
        // On suppose que le repository a une méthode pour trouver les commandes à préparer
        List<Order> orders = orderRepository.findOrdersToPrepare();
        return orders.stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markItemInPreparation(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        order.setStatus("en_preparation");
        orderRepository.save(order);
    }

    public void deductIngredientsStock(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        for (OrderItem item : order.getItems()) {
            if (item.getDish() != null && item.getDish().getRecipe() != null) {
                int nbPortions = item.getQuantity();
                for (var ri : item.getDish().getRecipe().getRecipeIngredients()) {
                    var ingredient = ri.getIngredient();
                    double totalADeduire = ri.getQuantite() * nbPortions;
                    if (ingredient.getStock() < totalADeduire) {
                        throw new RuntimeException("Stock insuffisant pour l'ingrédient : " + ingredient.getName());
                    }
                    ingredient.setStock(ingredient.getStock() - totalADeduire);
                    ingredientRepository.save(ingredient);
                }
            }
        }
    }

    @Override
    @Transactional
    public void markItemReady(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        order.setStatus("prête");
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void markOrderCompleted(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        order.setStatus("servie");
        orderRepository.save(order);
    }
}