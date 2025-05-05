package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByNameContainingIgnoreCase(String name);
    List<Supplier> findByLocationContainingIgnoreCase(String location);
    List<Supplier> findByProducts_Category(String category);
}