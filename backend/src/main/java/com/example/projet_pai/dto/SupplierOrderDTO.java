package com.example.projet_pai.dto;

import java.time.LocalDate;
import java.util.List;

public class SupplierOrderDTO {
    public Long id;
    public LocalDate orderDate;
    public double totalAmount;
    public String status;
    public Long supplierId;
    public List<SupplierOrderLineDTO> lines;
}