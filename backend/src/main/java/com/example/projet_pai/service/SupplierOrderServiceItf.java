package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierOrderDTO;
import java.util.List;

public interface SupplierOrderServiceItf {
    SupplierOrderDTO createOrder(SupplierOrderDTO dto);
    SupplierOrderDTO getOrder(Long id);
    List<SupplierOrderDTO> getOrdersBySupplier(Long supplierId);
    SupplierOrderDTO renewOrder(Long previousOrderId);
}