package com.example.projet_pai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.projet_pai.entite.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :userId")
    public List<Reservation> findReservationForUserId(@Param("userId") Long userId);
    
}
