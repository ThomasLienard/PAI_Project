package com.example.projet_pai.dto;

import java.util.Set;

public class DishCreateUpdateDTO {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private Long categoryId;
    private Set<Long> tagIds;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public Set<Long> getTagIds() {
        return tagIds;
    }
    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }

    // Getters et setters...
}