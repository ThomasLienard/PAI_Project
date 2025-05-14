package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierProductDTO;
import java.util.List;

public interface SupplierProductServiceItf {
    SupplierProductDTO addProduct(SupplierProductDTO dto);
    SupplierProductDTO updateProduct(Long id, SupplierProductDTO dto);
    List<SupplierProductDTO> getProductsBySupplier(Long supplierId);
    List<SupplierProductDTO> searchProducts(String name, String category);
    void deleteProduct(Long productId);
}