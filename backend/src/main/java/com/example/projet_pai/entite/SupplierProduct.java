package com.example.projet_pai.entite;

import jakarta.persistence.*;

@Entity
public class SupplierProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String name;
    private String category;
    private double price;
    private String packaging;
    private int usualDeliveryTime; // jours

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private SupplierProduct alternativeTo; // produit alternatif (optionnel)

    // Getters et setters
}