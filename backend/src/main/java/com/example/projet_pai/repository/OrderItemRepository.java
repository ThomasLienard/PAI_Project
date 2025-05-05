package com.example.projet_pai.repository;

import com.example.projet_pai.entite.SupplierOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<SupplierOrderItem, Long> {
    List<SupplierOrderItem> findByOrderId(Long orderId);
    
    @Query("SELECT oi FROM SupplierOrderItem oi WHERE oi.product.id = :productId ORDER BY oi.order.date DESC")
    List<SupplierOrderItem> findOrderHistoryByProduct(@Param("productId") Long productId);
    
    @Query("SELECT oi FROM SupplierOrderItem oi WHERE oi.order.supplier.id = :supplierId " +
           "GROUP BY oi.product.id ORDER BY COUNT(oi) DESC")
    List<SupplierOrderItem> findMostOrderedProducts(@Param("supplierId") Long supplierId);
}