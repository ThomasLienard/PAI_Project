package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierDTO;
import java.util.List;

public interface SupplierServiceItf {
    SupplierDTO createSupplier(SupplierDTO dto);
    SupplierDTO updateSupplier(Long id, SupplierDTO dto);
    void deactivateSupplier(Long id);
    SupplierDTO getSupplier(Long id);
    List<SupplierDTO> searchSuppliers(String name, String location, String productCategory);
    List<SupplierDTO> getAllSuppliers();
}