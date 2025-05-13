package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.SupplierDTO;
import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.repository.SupplierRepository;
import com.example.projet_pai.service.SupplierServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierServiceItf {

    @Autowired
    private SupplierRepository supplierRepository;

    private SupplierDTO toDTO(Supplier s) {
        SupplierDTO dto = new SupplierDTO();
        dto.id = s.getId();
        dto.name = s.getName();
        dto.email = s.getEmail();
        dto.location = s.getLocation();
        dto.active = s.isActive();
        dto.paymentTerms = s.getPaymentTerms();
        dto.deliveryFee = s.getDeliveryFee();
        dto.rating = s.getRating();
        return dto;
    }

    private Supplier toEntity(SupplierDTO dto) {
        Supplier s = new Supplier();
        s.setId(dto.id);
        s.setName(dto.name);
        s.setEmail(dto.email);
        s.setLocation(dto.location);
        s.setActive(dto.active);
        s.setPaymentTerms(dto.paymentTerms);
        s.setDeliveryFee(dto.deliveryFee);
        s.setRating(dto.rating);
        return s;
    }

    @Override
    public SupplierDTO createSupplier(SupplierDTO dto) {
        Supplier s = toEntity(dto);
        s.setActive(true);
        return toDTO(supplierRepository.save(s));
    }

    @Override
    public SupplierDTO updateSupplier(Long id, SupplierDTO dto) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setName(dto.name);
        s.setEmail(dto.email);
        s.setLocation(dto.location);
        s.setPaymentTerms(dto.paymentTerms);
        s.setDeliveryFee(dto.deliveryFee);
        return toDTO(supplierRepository.save(s));
    }

    @Override
    public void deactivateSupplier(Long id) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setActive(false);
        supplierRepository.save(s);
    }

    @Override
    public void activateSupplier(Long id) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setActive(true);
        supplierRepository.save(s);
    }

    @Override
    public SupplierDTO getSupplier(Long id) {
        return supplierRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public List<SupplierDTO> searchSuppliers(String name, String location, String productCategory) {
        // Recherche simple, à adapter selon besoins
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .filter(s -> (name == null || s.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(s -> (location == null || s.getLocation().toLowerCase().contains(location.toLowerCase())))
                .collect(Collectors.toList())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SupplierDTO rateSupplier(Long id, double rate) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setRating(rate);
        return toDTO(supplierRepository.save(s));
    }


}