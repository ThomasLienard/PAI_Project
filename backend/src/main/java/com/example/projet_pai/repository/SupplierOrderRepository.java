package com.example.projet_pai.repository;

import com.example.projet_pai.entite.SupplierOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Long> {
    List<SupplierOrder> findBySupplierId(Long supplierId);
    List<SupplierOrder> findByStatus(SupplierOrder.OrderStatus status);
    
    @Query("SELECT o FROM SupplierOrder o WHERE o.supplier.id = :supplierId AND o.date BETWEEN :start AND :end")
    List<SupplierOrder> findSupplierOrdersByDateRange(
        @Param("supplierId") Long supplierId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    @Query("SELECT o FROM SupplierOrder o WHERE o.date >= :date ORDER BY o.date DESC")
    List<SupplierOrder> findRecentOrders(@Param("date") LocalDateTime date);
}