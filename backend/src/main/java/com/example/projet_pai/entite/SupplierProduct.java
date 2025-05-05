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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public int getUsualDeliveryTime() {
        return usualDeliveryTime;
    }

    public void setUsualDeliveryTime(int usualDeliveryTime) {
        this.usualDeliveryTime = usualDeliveryTime;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public SupplierProduct getAlternativeTo() {
        return alternativeTo;
    }

    public void setAlternativeTo(SupplierProduct alternativeTo) {
        this.alternativeTo = alternativeTo;
    }

    // Getters et setters
    
}