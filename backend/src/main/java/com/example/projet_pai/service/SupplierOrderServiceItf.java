package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierOrderDTO;
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
}