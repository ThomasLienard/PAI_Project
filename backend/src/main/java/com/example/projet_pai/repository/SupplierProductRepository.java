package com.example.projet_pai.repository;

import com.example.projet_pai.entite.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {
    List<SupplierProduct> findBySupplier_Id(Long supplierId);
    List<SupplierProduct> findByCategory(String category);
    List<SupplierProduct> findByNameContainingIgnoreCase(String name);
}