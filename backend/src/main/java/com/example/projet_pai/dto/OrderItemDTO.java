package com.example.projet_pai.dto;

import com.example.projet_pai.entite.OrderItem;

public class OrderItemDTO {
    
    private Long id;
    private Long dishId;
    private String name;
    private double price;
    private Integer quantity;
    private String specialInstructions;
    
    public OrderItemDTO() {
    }
    
    public OrderItemDTO(OrderItem item) {
        this.id = item.getId();
        this.dishId = item.getDish().getId();
        this.name = item.getDish().getName();
        this.price = item.getDish().getPrice();
        this.quantity = item.getQuantity();
        this.specialInstructions = item.getSpecialInstructions();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getDishId() {
        return dishId;
    }
    
    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}