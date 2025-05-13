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

    private SupplierOrder toEntity(SupplierOrderDTO dto) {
        SupplierOrder o = new SupplierOrder();
        o.setId(dto.id);
        o.setOrderDate(dto.orderDate);
        o.setTotalAmount(dto.totalAmount);
        o.setStatus(SupplierOrder.OrderStatus.valueOf(dto.status));
        if (dto.supplierId != null) {
            Supplier supplier = supplierRepository.findById(dto.supplierId).orElseThrow();
            o.setSupplier(supplier);
        }
        if (dto.lines != null) {
            List<SupplierOrderLine> lines = dto.lines.stream().map(this::toLineEntity).collect(Collectors.toList());
            lines.forEach(l -> l.setOrder(o));
            o.setLines(lines);
        }
        return o;
    }

    private SupplierOrderLine toLineEntity(SupplierOrderLineDTO dto) {
        SupplierOrderLine l = new SupplierOrderLine();
        l.setId(dto.id);
        l.setQuantity(dto.quantity);
        l.setUnitPrice(dto.unitPrice);
        if (dto.productId != null) {
            SupplierProduct p = productRepository.findById(dto.productId).orElseThrow();
            l.setProduct(p);
        }
        return l;
    }

    @Override
    public SupplierOrderDTO createOrder(SupplierOrderDTO dto) {
        SupplierOrder o = toEntity(dto);
        return toDTO(orderRepository.save(o));
    }

    @Override
    public SupplierOrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public List<SupplierOrderDTO> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplier_Id(supplierId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SupplierOrderDTO renewOrder(Long previousOrderId) {
        SupplierOrder previous = orderRepository.findById(previousOrderId).orElseThrow();
        SupplierOrderDTO dto = toDTO(previous);
        dto.id = null; // nouvelle commande
        dto.status = SupplierOrder.OrderStatus.EN_ATTENTE.name();
        return createOrder(dto);
    }
}