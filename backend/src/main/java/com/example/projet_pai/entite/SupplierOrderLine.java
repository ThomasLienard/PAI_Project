package com.example.projet_pai.entite;

import jakarta.persistence.*;

@Entity
public class SupplierOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double unitPrice;

    @ManyToOne
    private SupplierOrder order;

    @ManyToOne
    private SupplierProduct product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public SupplierOrder getOrder() {
        return order;
    }

    public void setOrder(SupplierOrder order) {
        this.order = order;
    }

    public SupplierProduct getProduct() {
        return product;
    }

    public void setProduct(SupplierProduct product) {
        this.product = product;
    }

    // Getters et setters
    
}