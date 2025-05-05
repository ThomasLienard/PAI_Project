package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.SupplierRatingDTO;
import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierRating;
import com.example.projet_pai.repository.SupplierRatingRepository;
import com.example.projet_pai.repository.SupplierRepository;
import com.example.projet_pai.service.SupplierRatingServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierRatingServiceImpl implements SupplierRatingServiceItf {

    @Autowired
    private SupplierRatingRepository ratingRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    private SupplierRatingDTO toDTO(SupplierRating r) {
        SupplierRatingDTO dto = new SupplierRatingDTO();
        dto.id = r.getId();
        dto.score = r.getScore();
        dto.comment = r.getComment();
        dto.date = r.getDate();
        dto.supplierId = r.getSupplier() != null ? r.getSupplier().getId() : null;
        return dto;
    }

    private SupplierRating toEntity(SupplierRatingDTO dto) {
        SupplierRating r = new SupplierRating();
        r.setId(dto.id);
        r.setScore(dto.score);
        r.setComment(dto.comment);
        r.setDate(dto.date != null ? dto.date : LocalDate.now());
        if (dto.supplierId != null) {
            Supplier supplier = supplierRepository.findById(dto.supplierId).orElseThrow();
            r.setSupplier(supplier);
        }
        return r;
    }

    @Override
    public SupplierRatingDTO rateSupplier(SupplierRatingDTO dto) {
        SupplierRating r = toEntity(dto);
        return toDTO(ratingRepository.save(r));
    }

    @Override
    public List<SupplierRatingDTO> getRatingsBySupplier(Long supplierId) {
        return ratingRepository.findBySupplier_Id(supplierId).stream().map(this::toDTO).collect(Collectors.toList());
    }
}