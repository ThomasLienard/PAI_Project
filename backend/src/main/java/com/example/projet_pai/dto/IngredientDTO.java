package com.example.projet_pai.dto;

public class IngredientDTO {
    private Long id;
    private String name;
    private String unit;
    private String description;
    private String photoUrl;
    private Double initialStock;
    private Double alertThreshold;
    private Double recommendedOrderQty;
    private Integer shelfLifeDays;
    private Long categoryId;

    private boolean alertSent;

    private String alertLevel; // "OK", "BAS", "CRITIQUE"

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
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public Double getInitialStock() {
        return initialStock;
    }
    public void setInitialStock(Double initialStock) {
        this.initialStock = initialStock;
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
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isAlertSent() {
        return alertSent;
    }
    public void setAlertSent(boolean alertSent) {
        this.alertSent = alertSent;
    }

    public String getAlertLevel() {
        return alertLevel;
    }
    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    // Getters & setters
}
