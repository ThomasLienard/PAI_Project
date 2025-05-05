package com.example.projet_pai.service;

import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierOrder;
import com.example.projet_pai.entite.SupplierProduct;
import java.util.List;

public interface SupplierServiceItf {
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier supplier);
    void toggleSupplierStatus(Long id);
    List<Supplier> getAllSuppliers();
    List<Supplier> searchSuppliers(String query, String filterBy);
    List<Supplier> sortSuppliers(String sortBy);
    
    SupplierProduct addProduct(Long supplierId, SupplierProduct product);
    void updateProduct(Long productId, SupplierProduct product);
    void toggleProductPreferred(Long productId);
    List<SupplierProduct> getSupplierProducts(Long supplierId);
    List<SupplierProduct> searchProducts(String query, String category);
    
    SupplierOrder createOrder(Long supplierId, SupplierOrder order);
    void updateOrderStatus(Long orderId, String status);
    List<SupplierOrder> getSupplierOrders(Long supplierId);
}