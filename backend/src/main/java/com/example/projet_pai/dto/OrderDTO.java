package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    
    private Long id;
    private int tableNumber;
    private String clientName;
    private LocalDateTime orderTime;
    private String status;
    private List<OrderItemDTO> items;
    private boolean isAdditional;
    
    public OrderDTO() {
    }
    
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.tableNumber = order.getTable().getNumero();
        this.clientName = order.getClientName();
        this.orderTime = order.getOrderTime();
        this.status = order.getStatus();
        this.isAdditional = order.isAdditional();
        this.items = order.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getTableNumber() {
        return tableNumber;
    }
    
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<OrderItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
    
    public boolean isAdditional() {
        return isAdditional;
    }
    
    public void setAdditional(boolean additional) {
        isAdditional = additional;
    }
    
    public static List<OrderDTO> fromOrderList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
}