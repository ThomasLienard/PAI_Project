package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "restaurant_order")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;
    
    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;
    
    @Column(nullable = false)
    private String status; // "en_attente", "en_preparation", "pret", "servi"
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(name = "is_additional")
    private boolean isAdditional;
    
    // Constructeurs
    public Order() {
    }
    
    public Order(Table table, String clientName) {
        this.table = table;
        this.clientName = clientName;
        this.orderTime = LocalDateTime.now();
        this.status = "en_attente";
        this.isAdditional = false;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Table getTable() {
        return table;
    }
    
    public void setTable(Table table) {
        this.table = table;
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
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
    
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
    
    public boolean isAdditional() {
        return isAdditional;
    }
    
    public void setAdditional(boolean additional) {
        isAdditional = additional;
    }
}