package com.example.projet_pai.service;

import com.example.projet_pai.dto.SupplierRatingDTO;
import java.util.List;

public interface SupplierRatingServiceItf {
    SupplierRatingDTO rateSupplier(SupplierRatingDTO dto);
    List<SupplierRatingDTO> getRatingsBySupplier(Long supplierId);
}