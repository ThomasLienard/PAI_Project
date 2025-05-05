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

    // Getters et setters
}