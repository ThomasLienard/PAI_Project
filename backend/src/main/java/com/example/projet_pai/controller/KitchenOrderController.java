package com.example.projet_pai.controller;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.service.KitchenOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chef/orders")
public class KitchenOrderController {

    @Autowired
    private KitchenOrderServiceItf kitchenOrderService;

    @GetMapping
    public List<OrderDTO> getOrdersToPrepare() {
        return kitchenOrderService.getOrdersToPrepare();
    }

    @PutMapping("/{orderId}/items/{itemId}/in-preparation")
    public void markItemInPreparation(@PathVariable Long orderId, @PathVariable Long itemId) {
        kitchenOrderService.markItemInPreparation(orderId, itemId);
    }

    @PatchMapping("/{orderId}/items/{itemId}/ready")
    public void markItemReady(@PathVariable Long orderId, @PathVariable Long itemId) {
        kitchenOrderService.markItemReady(orderId, itemId);
    }

    @PatchMapping("/{orderId}/complete")
    public void markOrderCompleted(@PathVariable Long orderId) {
        kitchenOrderService.markOrderCompleted(orderId);
    }
}