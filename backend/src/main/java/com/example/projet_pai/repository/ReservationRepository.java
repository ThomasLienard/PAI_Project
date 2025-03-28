package com.example.projet_pai.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.projet_pai.entite.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    
}
