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

    @PostMapping
    public SupplierOrderDTO createOrder(@RequestBody SupplierOrderDTO dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping("/{id}")
    public SupplierOrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<SupplierOrderDTO> getOrdersBySupplier(@PathVariable Long supplierId) {
        return orderService.getOrdersBySupplier(supplierId);
    }

    @PostMapping("/{previousOrderId}/renew")
    public SupplierOrderDTO renewOrder(@PathVariable Long previousOrderId) {
        return orderService.renewOrder(previousOrderId);
    }
}