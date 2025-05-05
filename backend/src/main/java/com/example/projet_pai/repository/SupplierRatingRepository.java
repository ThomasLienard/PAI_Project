package com.example.projet_pai.repository;

import com.example.projet_pai.entite.SupplierRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRatingRepository extends JpaRepository<SupplierRating, Long> {
    List<SupplierRating> findBySupplier_Id(Long supplierId);
}