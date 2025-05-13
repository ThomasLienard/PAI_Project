package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.SupplierProductDTO;
import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierProduct;
import com.example.projet_pai.repository.SupplierProductRepository;
import com.example.projet_pai.repository.SupplierRepository;
import com.example.projet_pai.service.SupplierProductServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierProductServiceImpl implements SupplierProductServiceItf {

    @Autowired
    private SupplierProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    private SupplierProductDTO toDTO(SupplierProduct p) {
        SupplierProductDTO dto = new SupplierProductDTO();
        dto.id = p.getId();
        dto.reference = p.getReference();
        dto.name = p.getName();
        dto.category = p.getCategory();
        dto.price = p.getPrice();
        dto.packaging = p.getPackaging();
        dto.usualDeliveryTime = p.getUsualDeliveryTime();
        dto.supplierId = p.getSupplier() != null ? p.getSupplier().getId() : null;
        dto.alternativeToId = p.getAlternativeTo() != null ? p.getAlternativeTo().getId() : null;
        return dto;
    }

    private SupplierProduct toEntity(SupplierProductDTO dto) {
        SupplierProduct p = new SupplierProduct();
        p.setId(dto.id);
        p.setReference(dto.reference);
        p.setName(dto.name);
        p.setCategory(dto.category);
        p.setPrice(dto.price);
        p.setPackaging(dto.packaging);
        p.setUsualDeliveryTime(dto.usualDeliveryTime);
        if (dto.supplierId != null) {
            Supplier supplier = supplierRepository.findById(dto.supplierId).orElseThrow();
            p.setSupplier(supplier);
        }
        if (dto.alternativeToId != null) {
            SupplierProduct alt = productRepository.findById(dto.alternativeToId).orElse(null);
            p.setAlternativeTo(alt);
        }
        return p;
    }

    @Override
    public SupplierProductDTO addProduct(SupplierProductDTO dto) {
        SupplierProduct p = toEntity(dto);
        return toDTO(productRepository.save(p));
    }

    @Override
    public SupplierProductDTO updateProduct(Long id, SupplierProductDTO dto) {
        SupplierProduct p = productRepository.findById(id).orElseThrow();
        p.setReference(dto.reference);
        p.setName(dto.name);
        p.setCategory(dto.category);
        p.setPrice(dto.price);
        p.setPackaging(dto.packaging);
        p.setUsualDeliveryTime(dto.usualDeliveryTime);
        if (dto.alternativeToId != null) {
            SupplierProduct alt = productRepository.findById(dto.alternativeToId).orElse(null);
            p.setAlternativeTo(alt);
        }
        return toDTO(productRepository.save(p));
    }

    @Override
    public List<SupplierProductDTO> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplier_Id(supplierId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SupplierProductDTO> searchProducts(String name, String category) {
        List<SupplierProduct> products = productRepository.findAll();
        return products.stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(p -> (category == null || p.getCategory().equalsIgnoreCase(category)))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierProductDTO> getAlternatives(Long productId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getAlternativeTo() != null && p.getAlternativeTo().getId().equals(productId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}