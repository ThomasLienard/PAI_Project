package com.example.projet_pai.repository;

import com.example.projet_pai.dto.SupplierOrderLineDTO;
import com.example.projet_pai.entite.SupplierOrder;
import com.example.projet_pai.entite.SupplierOrder.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Long> {
    List<SupplierOrder> findBySupplier_Id(Long supplierId);

}