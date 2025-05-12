package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.status != 'servi' ORDER BY o.orderTime DESC")
    List<Order> findCurrentOrders();
    
    @Query("SELECT o FROM Order o WHERE o.table = :table AND o.status != 'servi' ORDER BY o.orderTime DESC")
    List<Order> findCurrentOrdersByTable(Table table);
}