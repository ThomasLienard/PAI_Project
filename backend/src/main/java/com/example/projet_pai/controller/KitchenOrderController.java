package com.example.projet_pai.controller;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.service.KitchenOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuisinier/orders")
public class KitchenOrderController {

    @Autowired
    private KitchenOrderServiceItf kitchenOrderService;

    @GetMapping
    public List<OrderDTO> getOrdersToPrepare() {
        return kitchenOrderService.getOrdersToPrepare();
    }

    @PatchMapping("/{orderId}/in-preparation")
    public void markItemInPreparation(@PathVariable Long orderId) {
        kitchenOrderService.markItemInPreparation(orderId);
    }

    @PatchMapping("/{orderId}/ready")
    public void markItemReady(@PathVariable Long orderId) {
        kitchenOrderService.markItemReady(orderId);
    }

    @PatchMapping("/{orderId}/complete")
    public void markOrderCompleted(@PathVariable Long orderId) {
        kitchenOrderService.markOrderCompleted(orderId);
    }
}