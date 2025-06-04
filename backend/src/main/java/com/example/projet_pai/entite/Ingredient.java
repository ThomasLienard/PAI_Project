package com.example.projet_pai.entite;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String unit;
    private String photoUrl;
    private Double stock; // anciennement initialStock
    private Double alertThreshold;
    private Double recommendedOrderQty;
    private Integer shelfLifeDays;

    @ManyToOne
    private IngredientCategory category;

    @OneToMany(mappedBy = "ingredient")
    private List<SupplierProduct> supplierProducts = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(Double alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public Double getRecommendedOrderQty() {
        return recommendedOrderQty;
    }

    public void setRecommendedOrderQty(Double recommendedOrderQty) {
        this.recommendedOrderQty = recommendedOrderQty;
    }

    public Integer getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientCategory category) {
        this.category = category;
    }

    public List<SupplierProduct> getSupplierProducts() {
        return supplierProducts;
    }

    public void setSupplierProducts(List<SupplierProduct> supplierProducts) {
        this.supplierProducts = supplierProducts;
    }
}