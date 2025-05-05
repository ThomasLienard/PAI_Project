package com.example.projet_pai.repository;

import com.example.projet_pai.entite.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {
    List<SupplierProduct> findBySupplierId(Long supplierId);
    List<SupplierProduct> findByCategory(String category);
    List<SupplierProduct> findByIsPreferredTrue();
    
    @Query("SELECT p FROM SupplierProduct p WHERE p.supplier.id = :supplierId AND p.available = true")
    List<SupplierProduct> findAvailableProductsBySupplier(@Param("supplierId") Long supplierId);
    
    @Query("SELECT p FROM SupplierProduct p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.reference) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SupplierProduct> searchProducts(@Param("query") String query);
    
    @Query("SELECT p FROM SupplierProduct p WHERE p.name = :name ORDER BY p.price ASC")
    List<SupplierProduct> findAlternativeProducts(@Param("name") String productName);
}