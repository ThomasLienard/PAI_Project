package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByNameContainingIgnoreCase(String name);
    List<Supplier> findByLocationContainingIgnoreCase(String location);
    
    @Query("SELECT s FROM Supplier s JOIN s.productTypes pt WHERE LOWER(pt) LIKE LOWER(CONCAT('%', :type, '%'))")
    List<Supplier> findByProductTypesContainingIgnoreCase(@Param("type") String type);
    
    List<Supplier> findAllByOrderByRatingDesc();
    List<Supplier> findAllByOrderByAverageDeliveryTimeAsc();
    List<Supplier> findByActiveTrue();
}