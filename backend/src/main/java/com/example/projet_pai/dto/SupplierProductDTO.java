package com.example.projet_pai.dto;

public class SupplierProductDTO {
    public Long id;
    public String name;
    public String category;
    public double price;
    public int usualDeliveryTime;
    public Long supplierId;

    public void setSupplierId(Long supplierId2) {
        this.supplierId = supplierId2;
        }
    public void setId(Long productId) {
        this.id = productId;
        }
}