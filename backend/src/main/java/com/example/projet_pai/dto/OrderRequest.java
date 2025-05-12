package com.example.projet_pai.dto;

import java.util.List;

public class OrderRequest {
    
    private Long tableId;
    private String clientName;
    private boolean isAdditional;
    private List<OrderItemRequest> items;
    
    // Getters et Setters
    public Long getTableId() {
        return tableId;
    }
    
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public boolean isAdditional() {
        return isAdditional;
    }
    
    public void setAdditional(boolean additional) {
        isAdditional = additional;
    }
    
    public List<OrderItemRequest> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}