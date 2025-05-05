package com.example.projet_pai.controller;

import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierOrder;
import com.example.projet_pai.entite.SupplierProduct;
import com.example.projet_pai.service.SupplierServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suppliers")
@PreAuthorize("hasRole('ADMIN')")
public class SupplierController {

    @Autowired
    private SupplierServiceItf supplierService;

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        try {
            return new ResponseEntity<>(supplierService.createSupplier(supplier), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        try {
            return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleSupplierStatus(@PathVariable Long id) {
        try {
            supplierService.toggleSupplierStatus(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String sortBy) {
        try {
            List<Supplier> suppliers;
            if (query != null && filterBy != null) {
                suppliers = supplierService.searchSuppliers(query, filterBy);
            } else if (sortBy != null) {
                suppliers = supplierService.sortSuppliers(sortBy);
            } else {
                suppliers = supplierService.getAllSuppliers();
            }
            return ResponseEntity.ok(suppliers);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{supplierId}/products")
    public ResponseEntity<SupplierProduct> addProduct(
            @PathVariable Long supplierId,
            @RequestBody SupplierProduct product) {
        try {
            return new ResponseEntity<>(
                supplierService.addProduct(supplierId, product),
                HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/products/{productId}/toggle-preferred")
    public ResponseEntity<Void> toggleProductPreferred(@PathVariable Long productId) {
        try {
            supplierService.toggleProductPreferred(productId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{supplierId}/orders")
    public ResponseEntity<SupplierOrder> createOrder(
            @PathVariable Long supplierId,
            @RequestBody SupplierOrder order) {
        try {
            return new ResponseEntity<>(
                supplierService.createOrder(supplierId, order),
                HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
}