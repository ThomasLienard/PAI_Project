package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierDTO;
import com.example.projet_pai.dto.SupplierRatingDTO;
import com.example.projet_pai.service.SupplierServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suppliers")
public class SupplierController {

    @Autowired
    private SupplierServiceItf supplierService;

    @PostMapping
    public SupplierDTO createSupplier(@RequestBody SupplierDTO dto) {
        return supplierService.createSupplier(dto);
    }

    @PutMapping("/{id}")
    public SupplierDTO updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO dto) {
        return supplierService.updateSupplier(id, dto);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateSupplier(@PathVariable Long id) {
        supplierService.deactivateSupplier(id);
    }

    @PatchMapping("/{id}/activate")
    public void activateSupplier(@PathVariable Long id) {
        supplierService.activateSupplier(id);
    }

    @GetMapping("/{id}")
    public SupplierDTO getSupplier(@PathVariable Long id) {
        return supplierService.getSupplier(id);
    }

    @PatchMapping("/{id}/rate")
    public SupplierDTO rateSupplier(@PathVariable Long id, @RequestBody SupplierRatingDTO ratingDTO) {
        return supplierService.rateSupplier(id, ratingDTO.getRating());
    }

    @GetMapping
    public List<SupplierDTO> searchSuppliers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String productCategory
    ) {
        return supplierService.searchSuppliers(name, location, productCategory);
    }

    @GetMapping("/all")
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
}