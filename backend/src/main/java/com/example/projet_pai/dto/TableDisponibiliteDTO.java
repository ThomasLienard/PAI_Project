package com.example.projet_pai.dto;

public class TableDisponibiliteDTO {
    private Long id;
    private int numero;
    private int capacite;
    
    public TableDisponibiliteDTO() {
    }
    
    public TableDisponibiliteDTO(Long id, int numero, int capacite) {
        this.id = id;
        this.numero = numero;
        this.capacite = capacite;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public int getCapacite() {
        return capacite;
    }
    
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
}