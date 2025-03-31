package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@jakarta.persistence.Table(name = "restaurant_table") // Renommer la table en "restaurant_table"
public class Table {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int numero;
    
    private int capacite;
    
    @OneToMany(mappedBy = "table")
    private List<Reservation> reservations = new ArrayList<>();
    
    public Table() {
    }
    
    public Table(int numero, int capacite) {
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
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}