package com.example.projet_pai.service;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.dto.OrderRequest;

import java.util.List;

public interface ServerOrderServiceItf {
    
    /**
     * Crée une nouvelle commande
     * @param orderRequest Les détails de la commande
     * @return La commande créée
     */
    OrderDTO createOrder(OrderRequest orderRequest);
    
    /**
     * Récupère toutes les commandes en cours
     * @return Liste des commandes en cours
     */
    List<OrderDTO> getCurrentOrders();
    
    /**
     * Récupère les commandes en cours pour une table
     * @param tableId Identifiant de la table
     * @return Liste des commandes pour cette table
     */
    List<OrderDTO> getCurrentOrdersByTable(Long tableId);
    
    /**
     * Met à jour le statut d'une commande
     * @param orderId Identifiant de la commande
     * @param status Nouveau statut
     * @return La commande mise à jour
     */
    OrderDTO updateOrderStatus(Long orderId, String status);
    
    /**
     * Annule une commande
     * @param orderId Identifiant de la commande
     */
    void cancelOrder(Long orderId);
}