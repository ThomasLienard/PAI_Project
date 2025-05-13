package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierProductDTO;
import com.example.projet_pai.service.SupplierProductServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-products")
public class SupplierProductController {

    @Autowired
    private SupplierProductServiceItf productService;

    @PostMapping
    public SupplierProductDTO addProduct(@RequestBody SupplierProductDTO dto) {
        return productService.addProduct(dto);
    }

    @PutMapping("/{id}")
    public SupplierProductDTO updateProduct(@PathVariable Long id, @RequestBody SupplierProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<SupplierProductDTO> getProductsBySupplier(@PathVariable Long supplierId) {
        return productService.getProductsBySupplier(supplierId);
    }

    @GetMapping("/search")
    public List<SupplierProductDTO> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category
    ) {
        return productService.searchProducts(name, category);
    }

    @GetMapping("/{productId}/alternatives")
    public List<SupplierProductDTO> getAlternatives(@PathVariable Long productId) {
        return productService.getAlternatives(productId);
    }
}