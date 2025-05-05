package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String location;

    private Integer paymentTerms;

    private Double minimumOrder;

    private Double deliveryFee;

    @Column(nullable = false)
    private boolean active = true;

    private Double rating;

    private Integer averageDeliveryTime;

    @ElementCollection
    @CollectionTable(name = "supplier_product_types")
    private List<String> productTypes;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierProduct> products;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierOrder> orders;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(Integer paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public Double getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(Double minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getAverageDeliveryTime() {
        return averageDeliveryTime;
    }

    public void setAverageDeliveryTime(Integer averageDeliveryTime) {
        this.averageDeliveryTime = averageDeliveryTime;
    }

    public List<String> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<String> productTypes) {
        this.productTypes = productTypes;
    }

    public List<SupplierProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SupplierProduct> products) {
        this.products = products;
    }

    public List<SupplierOrder> getOrders() {
        return orders;
    }
    public SupplierOrder addSupplierOrder(SupplierOrder order) {
        this.orders.add(order);
        order.setSupplier(this);
        return order;
    }
    public SupplierOrder removeSupplierOrder(SupplierOrder order) {
        this.orders.remove(order);
        order.setSupplier(null);
        return order;
    }

    public void setOrders(List<SupplierOrder> orders) {
        this.orders = orders;
    }
    
}