package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class SupplierRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score; // 1 à 5
    private String comment;
    private LocalDate date;

    @ManyToOne
    private Supplier supplier;

    // Getters et setters
}