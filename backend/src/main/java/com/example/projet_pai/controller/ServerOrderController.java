package com.example.projet_pai.controller;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.dto.OrderRequest;
import com.example.projet_pai.service.ServerOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/server/orders")
public class ServerOrderController {

    @Autowired
    private ServerOrderServiceItf serverOrderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            OrderDTO createdOrder = serverOrderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<OrderDTO>> getCurrentOrders() {
        try {
            List<OrderDTO> orders = serverOrderService.getCurrentOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/tables/{tableId}")
    public ResponseEntity<List<OrderDTO>> getCurrentOrdersByTable(@PathVariable Long tableId) {
        try {
            List<OrderDTO> orders = serverOrderService.getCurrentOrdersByTable(tableId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        try {
            serverOrderService.cancelOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}