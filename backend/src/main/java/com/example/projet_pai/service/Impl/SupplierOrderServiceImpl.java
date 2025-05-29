package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.SupplierOrderDTO;
import com.example.projet_pai.dto.SupplierOrderLineDTO;
import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierOrder;
import com.example.projet_pai.entite.SupplierOrderLine;
import com.example.projet_pai.entite.SupplierProduct;
import com.example.projet_pai.repository.SupplierOrderRepository;
import com.example.projet_pai.repository.SupplierProductRepository;
import com.example.projet_pai.repository.SupplierRepository;
import com.example.projet_pai.service.SupplierOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierOrderServiceImpl implements SupplierOrderServiceItf {

    @Autowired
    private SupplierOrderRepository orderRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierProductRepository productRepository;

    private SupplierOrderDTO toDTO(SupplierOrder o) {
        SupplierOrderDTO dto = new SupplierOrderDTO();
        dto.id = o.getId();
        dto.orderDate = o.getOrderDate();
        dto.totalAmount = o.getTotalAmount();
        dto.status = o.getStatus().name();
        dto.supplierId = o.getSupplier() != null ? o.getSupplier().getId() : null;
        dto.lines = o.getLines() != null ? o.getLines().stream().map(this::toLineDTO).collect(Collectors.toList()) : null;
        return dto;
    }

    private SupplierOrderLineDTO toLineDTO(SupplierOrderLine l) {
        SupplierOrderLineDTO dto = new SupplierOrderLineDTO();
        dto.id = l.getId();
        dto.quantity = l.getQuantity();
        dto.unitPrice = l.getUnitPrice();
        dto.productId = l.getProduct() != null ? l.getProduct().getId() : null;
        return dto;
    }

    private SupplierOrder toEntity(SupplierOrderDTO dto, boolean ignoreIds) {
        SupplierOrder o = new SupplierOrder();
        o.setId(ignoreIds || dto.id == null ? null : dto.id);
        o.setOrderDate(dto.orderDate);
        o.setTotalAmount(dto.totalAmount);

        if (dto.status != null && !dto.status.trim().isEmpty()) {
            try {
                o.setStatus(SupplierOrder.OrderStatus.valueOf(dto.status.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Statut invalide fourni dans le DTO : " + dto.status);
            }
        }

        if (dto.supplierId != null) {
            Supplier supplier = supplierRepository.findById(dto.supplierId)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec ID: " + dto.supplierId));
            o.setSupplier(supplier);
        }
        if (dto.lines != null) {
            List<SupplierOrderLine> lines = dto.lines.stream()
                .map(lineDto -> {
                    SupplierOrderLine lineEntity = toLineEntity(lineDto, ignoreIds);
                    lineEntity.setOrder(o);
                    return lineEntity;
                })
                .collect(Collectors.toList());
            o.setLines(lines);
        }
        return o;
    }

    private SupplierOrderLine toLineEntity(SupplierOrderLineDTO dto, boolean ignoreIds) {
        SupplierOrderLine l = new SupplierOrderLine();
        l.setId(ignoreIds || dto.id == null ? null : dto.id);
        l.setQuantity(dto.quantity);
        l.setUnitPrice(dto.unitPrice);
        if (dto.productId != null) {
            SupplierProduct p = productRepository.findById(dto.productId)
                .orElseThrow(() -> new RuntimeException("Produit fournisseur introuvable avec ID: " + dto.productId));
            l.setProduct(p);
        }
        return l;
    }

    @Override
    public SupplierOrderDTO createOrder(SupplierOrderDTO dto) {
        if (dto.supplierId == null) {
            throw new IllegalArgumentException("Un fournisseur doit être sélectionné.");
        }
        if (dto.lines == null || dto.lines.isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins un produit.");
        }

        double totalFromDtoLines = dto.lines.stream()
            .mapToDouble(line -> line.unitPrice * line.quantity)
            .sum();

        SupplierOrder o = toEntity(dto, true);

        o.setOrderDate(LocalDate.now());
        o.setStatus(SupplierOrder.OrderStatus.EN_ATTENTE);

        double finalTotalAmount = 0;
        if (o.getLines() != null) {
            finalTotalAmount = o.getLines().stream()
                .mapToDouble(line -> line.getUnitPrice() * line.getQuantity())
                .sum();
        }
        o.setTotalAmount(finalTotalAmount);

        SupplierOrder savedOrder = orderRepository.save(o);
        return toDTO(savedOrder);
    }

    @Override
    public SupplierOrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public List<SupplierOrderDTO> getOrderHistory() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(SupplierOrder::getOrderDate).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierOrderDTO> getOrderHistoryBySupplier(Long supplierId) {
        return orderRepository.findBySupplier_Id(supplierId).stream()
                .sorted(Comparator.comparing(SupplierOrder::getOrderDate).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierOrderDTO renewOrder(Long previousOrderId) {
        SupplierOrder previous = orderRepository.findById(previousOrderId).orElseThrow();
        SupplierOrder newOrder = new SupplierOrder();
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setSupplier(previous.getSupplier());
        newOrder.setStatus(SupplierOrder.OrderStatus.EN_ATTENTE);
        newOrder.setTotalAmount(previous.getTotalAmount());

        newOrder.setId(null);
        
        
        List<SupplierOrderLine> newLines = new ArrayList<>();
        for (SupplierOrderLine oldLine : previous.getLines()) {
            SupplierOrderLine newLine = new SupplierOrderLine();
            newLine.setProduct(oldLine.getProduct());
            newLine.setQuantity(oldLine.getQuantity());
            newLine.setOrder(newOrder); 
            newLine.setId(null);
            newLines.add(newLine);
        }
        newOrder.setLines(newLines);
       
        
        SupplierOrder saved = orderRepository.save(newOrder);
        return toDTO(saved);
    }
}