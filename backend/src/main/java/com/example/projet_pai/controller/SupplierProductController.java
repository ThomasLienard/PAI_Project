package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierProductDTO;
import com.example.projet_pai.service.SupplierProductServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suppliers/{supplierId}/products")
public class SupplierProductController {

    @Autowired
    private SupplierProductServiceItf productService;

    // Liste des produits d'un fournisseur
    @GetMapping
    public List<SupplierProductDTO> getProductsBySupplier(@PathVariable Long supplierId) {
        return productService.getProductsBySupplier(supplierId);
    }

    // Ajout d'un produit au catalogue du fournisseur
    @PostMapping
    public SupplierProductDTO addProduct(@PathVariable Long supplierId, @RequestBody SupplierProductDTO dto) {
        dto.setSupplierId(supplierId);
        return productService.addProduct(dto);
    }

    // Modification d'un produit du catalogue du fournisseur
    @PutMapping("/{productId}")
    public SupplierProductDTO updateProduct(@PathVariable Long supplierId, @PathVariable Long productId, @RequestBody SupplierProductDTO dto) {
        dto.setSupplierId(supplierId);
        dto.setId(productId);
        return productService.updateProduct(productId, dto);
    }

    // Suppression d'un produit du catalogue du fournisseur
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long supplierId, @PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}