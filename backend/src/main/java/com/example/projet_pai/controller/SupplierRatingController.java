package com.example.projet_pai.controller;

import com.example.projet_pai.dto.SupplierRatingDTO;
import com.example.projet_pai.service.SupplierRatingServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-ratings")
public class SupplierRatingController {

    @Autowired
    private SupplierRatingServiceItf ratingService;

    @PostMapping
    public SupplierRatingDTO rateSupplier(@RequestBody SupplierRatingDTO dto) {
        return ratingService.rateSupplier(dto);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<SupplierRatingDTO> getRatingsBySupplier(@PathVariable Long supplierId) {
        return ratingService.getRatingsBySupplier(supplierId);
    }
}