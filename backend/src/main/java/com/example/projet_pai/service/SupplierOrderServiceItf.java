package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierOrderDTO;
import com.example.projet_pai.dto.SupplierOrderLineDTO;

import java.util.List;

public interface SupplierOrderServiceItf {
    /**
     * Retourne l'historique des commandes, trié par date décroissante (plus récentes d'abord)
     */
    List<SupplierOrderDTO> getOrderHistory();

    /**
     * Retourne l'historique des commandes pour un fournisseur donné
     */
    List<SupplierOrderDTO> getOrderHistoryBySupplier(Long supplierId);

    /**
     * Renouvelle une commande à partir d'une commande existante (copie les lignes, statut = EN_ATTENTE)
     */
    SupplierOrderDTO renewOrder(Long previousOrderId);

    /**
     * Crée une nouvelle commande fournisseur
     */
    SupplierOrderDTO createOrder(SupplierOrderDTO dto);

    /**
     * Retourne une commande par son id
     */
    SupplierOrderDTO getOrder(Long orderId);

    /**
     * Valide une commande (change le statut à VALIDEE)
     */
    SupplierOrderDTO validateOrder(Long orderId);

    /**
     * Met à jour les lignes d'une commande existante
     * @param status 
     */
    SupplierOrderDTO updateOrderLines(Long orderId, List<SupplierOrderLineDTO> lines, String status);

    List<SupplierOrderDTO> getPendingOrders();

}