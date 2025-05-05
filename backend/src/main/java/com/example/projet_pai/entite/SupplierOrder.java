package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class SupplierOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate orderDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private Supplier supplier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<SupplierOrderLine> lines;

    // Getters et setters

    public enum OrderStatus {
        EN_ATTENTE, CONFIRMEE, LIVREE, PARTIELLEMENT_LIVREE, PROBLEME
    }
}