package com.example.projet_pai.service;

import com.example.projet_pai.dto.OrderDTO;

import java.util.List;

public interface KitchenOrderServiceItf {

    /**
     * Récupère toutes les commandes à préparer (statut "validée" ou "en préparation")
     */
    List<OrderDTO> getOrdersToPrepare();

    /**
     * Marque un plat comme "en préparation"
     */
    void markItemInPreparation(Long orderId);

    /**
     * Marque un plat comme "prêt"
     */
    void markItemReady(Long orderId);

    /**
     * Marque une commande comme "terminée"
     */
    void markOrderCompleted(Long orderId);
}