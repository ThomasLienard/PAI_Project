package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierOrderDTO;
import com.example.projet_pai.service.SupplierOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-orders")
public class SupplierOrderController {

    @Autowired
    private SupplierOrderServiceItf orderService;

    // Créer une commande
    @PostMapping
    public SupplierOrderDTO createOrder(@RequestBody SupplierOrderDTO dto) {
        return orderService.createOrder(dto);
    }

    // Consulter une commande par id
    @GetMapping("/{id}")
    public SupplierOrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    // Historique complet (toutes commandes, triées du plus récent au plus ancien)
    @GetMapping
    public List<SupplierOrderDTO> getOrderHistory() {
        return orderService.getOrderHistory();
    }

    // Historique pour un fournisseur donné
    @GetMapping("/supplier/{supplierId}")
    public List<SupplierOrderDTO> getOrderHistoryBySupplier(@PathVariable Long supplierId) {
        return orderService.getOrderHistoryBySupplier(supplierId);
    }

    // Renouveler une commande existante
    @PostMapping("/{previousOrderId}/renew")
    public SupplierOrderDTO renewOrder(@PathVariable Long previousOrderId) {
        return orderService.renewOrder(previousOrderId);
    }
}